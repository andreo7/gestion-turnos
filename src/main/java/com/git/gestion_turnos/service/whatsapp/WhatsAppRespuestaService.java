package com.git.gestion_turnos.service.whatsapp;

import com.git.gestion_turnos.dto.whatsapp.WhatsAppWebhookDTO;
import com.git.gestion_turnos.entity.Notificacion;
import com.git.gestion_turnos.entity.Persona;
import com.git.gestion_turnos.entity.Turno;
import com.git.gestion_turnos.enums.TipoNotificacion;
import com.git.gestion_turnos.repository.NotificacionRepository;
import com.git.gestion_turnos.repository.PersonaRepository;
import com.git.gestion_turnos.service.notificacion.INotificacion;
import com.git.gestion_turnos.service.turno.ITurnoReserva;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Servicio para procesar respuestas de usuarios vía WhatsApp.
 *
 * Maneja respuestas a recordatorios de turnos:
 * - "SI" / "SÍ" / "OK" → Confirma el turno
 * - "NO" / "CANCELAR" → Cancela el turno
 */
@Service
public class WhatsAppRespuestaService {
    private static final Logger log = LoggerFactory.getLogger(WhatsAppRespuestaService.class);

    private final NotificacionRepository notificacionRepository;
    private final PersonaRepository personaRepository;
    private final INotificacion notificacionService;
    private final ITurnoReserva turnoService;

    // Patrones para detectar confirmación/cancelación
    private static final Pattern CONFIRMACION_PATTERN =
            Pattern.compile("^(SI|SÍ|DALE|OK|CONFIRMAR|CONFIRMO|ACEPTO|1)$", Pattern.CASE_INSENSITIVE);

    private static final Pattern CANCELACION_PATTERN =
            Pattern.compile("^(NO|CANCELAR|CANCELO|RECHAZAR|RECHAZO|2)$", Pattern.CASE_INSENSITIVE);

    public WhatsAppRespuestaService(NotificacionRepository notificacionRepository, PersonaRepository personaRepository,
                                    INotificacion notificacionService, ITurnoReserva turnoService){
        this.notificacionRepository = notificacionRepository;
        this.personaRepository = personaRepository;
        this.notificacionService = notificacionService;
        this.turnoService = turnoService;
    }
    /**
     * Procesa una respuesta recibida vía webhook de WhatsApp.
     *
     * @param webhook Datos del webhook de Twilio
     * @return Mensaje de respuesta para enviar al usuario
     */
    @Transactional
    public String procesarRespuesta(WhatsAppWebhookDTO webhook) {
        log.info("📨 Procesando respuesta WhatsApp de {}: '{}'",
                webhook.getFromNumber(), webhook.getBody());

        try {
            // 1. Buscar persona por teléfono
            Persona persona = buscarPersonaPorTelefono(webhook.getFromNumber());

            if (persona == null) {
                log.warn("⚠️ Persona no encontrada con teléfono: {}", webhook.getFromNumber());
                return "No encontramos su número en nuestro sistema. " +
                        "Por favor contacte con nosotros.";
            }

            // 2. Buscar notificación pendiente de respuesta
            Notificacion notificacion = buscarNotificacionPendiente(persona);

            if (notificacion == null) {
                log.warn("⚠️ No hay notificaciones pendientes para persona {}", persona.getId());
                return "No tenemos turnos pendientes de confirmación para usted. " +
                        "Si necesita ayuda, contáctenos.";
            }

            // 3. Validar que la notificación no esté ya respondida (idempotencia)
            if (notificacion.isRespondida()) {
                log.info("ℹ️ Notificación {} ya fue respondida anteriormente", notificacion.getId());
                return "Su respuesta ya fue procesada anteriormente. Gracias.";
            }

            // 4. Procesar respuesta según contenido
            String mensaje = webhook.getBodyNormalized();
            String respuesta = procesarMensaje(mensaje, notificacion);

            // 5. Marcar como respondida
            notificacionService.marcarComoRespondida(notificacion.getId());

            log.info("✅ Respuesta procesada correctamente para notificación {}",
                    notificacion.getId());

            return respuesta;

        } catch (Exception e) {
            log.error("❌ Error procesando respuesta de WhatsApp", e);
            return "Ocurrió un error procesando su respuesta. " +
                    "Por favor intente nuevamente o contáctenos.";
        }
    }

    /**
     * Procesa el mensaje y ejecuta la acción correspondiente.
     */
    private String procesarMensaje(String mensaje, Notificacion notificacion) {

        // Confirmación
        if (CONFIRMACION_PATTERN.matcher(mensaje).matches()) {
            return procesarConfirmacion(notificacion);
        }

        // Cancelación
        if (CANCELACION_PATTERN.matcher(mensaje).matches()) {
            return procesarCancelacion(notificacion);
        }

        // Respuesta no reconocida
        log.warn("⚠️ Mensaje no reconocido: '{}'", mensaje);
        return "No entendimos su respuesta. " +
                "Por favor responda:\n" +
                "• SI para confirmar el turno\n" +
                "• NO para cancelar el turno";
    }

    /**
     * Confirma el turno asociado a la notificación.
     */
    private String procesarConfirmacion(Notificacion notificacion) {
        log.info("✅ Confirmando turno {} para persona {}",
                notificacion.getTurno().getId(),
                notificacion.getPersona().getId());

        try {
            Turno turno = notificacion.getTurno();
            turnoService.confirmarTurno(turno.getId());

            return String.format(
                    "✅ Turno CONFIRMADO\n\n" +
                            "📅 Fecha: %s\n" +
                            "🕐 Hora: %s\n\n" +
                            "Lo esperamos! Si necesita cancelar, avísenos con anticipación.",
                    turno.getFecha(),
                    turno.getHora()
            );

        } catch (Exception e) {
            log.error("❌ Error confirmando turno {}", notificacion.getTurno().getId(), e);
            return "Hubo un problema confirmando su turno. " +
                    "Por favor contáctenos directamente.";
        }
    }

    /**
     * Cancela el turno asociado a la notificación.
     */
    private String procesarCancelacion(Notificacion notificacion) {
        log.info("❌ Cancelando turno {} para persona {}",
                notificacion.getTurno().getId(),
                notificacion.getPersona().getId());

        try {
            Turno turno = notificacion.getTurno();
            turnoService.cancelarTurno(turno.getId());

            return String.format(
                    "✅ Turno CANCELADO\n\n" +
                            "📅 Fecha: %s\n" +
                            "🕐 Hora: %s\n\n" +
                            "Su turno ha sido cancelado. " +
                            "Si desea agendar uno nuevo, contáctenos.",
                    turno.getFecha(),
                    turno.getHora()
            );

        } catch (Exception e) {
            log.error("❌ Error cancelando turno {}", notificacion.getTurno().getId(), e);
            return "Hubo un problema cancelando su turno. " +
                    "Por favor contáctenos directamente.";
        }
    }

    /**
     * Busca una persona por número de teléfono.
     * Intenta diferentes formatos por si el número está guardado sin prefijos.
     */
    private Persona buscarPersonaPorTelefono(String telefono) {
        if (telefono == null) return null;

        // Intentar búsqueda exacta
        Persona persona = personaRepository.findByTelefono(telefono);
        if (persona != null) return persona;

        // Intentar sin el prefijo +54
        if (telefono.startsWith("+54")) {
            String sinPrefijo = telefono.substring(3);
            persona = personaRepository.findByTelefono(sinPrefijo);
            if (persona != null) return persona;
        }

        // Intentar con el prefijo +54 si no lo tiene
        if (!telefono.startsWith("+")) {
            String conPrefijo = "+54" + telefono;
            persona = personaRepository.findByTelefono(conPrefijo);
            if (persona != null) return persona;
        }

        return null;
    }

    /**
     * Busca la última notificación de recordatorio enviada pero no respondida.
     */
    private Notificacion buscarNotificacionPendiente(Persona persona) {
        List<Notificacion> notificaciones = notificacionRepository
                .findByPersonaIdAndEnviadaTrueAndRespondidaFalseAndTipo(
                        persona.getId(),
                        TipoNotificacion.RECORDATORIO
                );

        if (notificaciones.isEmpty()) {
            return null;
        }

        // Retornar la más reciente
        return notificaciones.stream()
                .max((n1, n2) -> n1.getFechaCreacion().compareTo(n2.getFechaCreacion()))
                .orElse(null);
    }

    /**
     * Valida si un mensaje es una respuesta válida (SI o NO).
     */
    public boolean esRespuestaValida(String mensaje) {
        if (mensaje == null) return false;
        String normalizado = mensaje.trim().toUpperCase();
        return CONFIRMACION_PATTERN.matcher(normalizado).matches()
                || CANCELACION_PATTERN.matcher(normalizado).matches();
    }
}
