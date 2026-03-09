package com.git.gestion_turnos.service.notificacion;

import com.git.gestion_turnos.entity.Notificacion;
import com.git.gestion_turnos.repository.NotificacionRepository;
import com.git.gestion_turnos.service.whatsapp.WhatsAppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificacionEnviadorService {

    private static final Logger log = LoggerFactory.getLogger(NotificacionEnviadorService.class);

    private final NotificacionRepository notificacionRepository;
    private final NotificacionValidadorService validadorService;
    private final WhatsAppService whatsAppService;

    public NotificacionEnviadorService(
            NotificacionRepository notificacionRepository,
            NotificacionValidadorService validadorService,
            WhatsAppService whatsAppService) {
        this.notificacionRepository = notificacionRepository;
        this.validadorService = validadorService;
        this.whatsAppService = whatsAppService;
    }

    /**
     * Procesa y envía todas las notificaciones pendientes que cumplan los criterios.
     */
    public void procesarYEnviarRecordatorios() {
        log.debug("Consultando notificaciones pendientes de envio");

        List<Notificacion> pendientes = notificacionRepository.findPendientesConTurnoYPersona();

        log.info("📋 Notificaciones pendientes encontradas: {}", pendientes.size());

        if (pendientes.isEmpty()) {
            log.debug("No hay notificaciones para procesar");
            return;
        }

        int enviados = 0;
        int omitidos = 0;
        int errores = 0;

        for (Notificacion notificacion : pendientes) {
            try {
                ResultadoValidacion resultado = validadorService.validarParaEnvio(notificacion);

                if (resultado.debeEnviarse()) {
                    enviarNotificacion(notificacion);
                    enviados++;

                } else if (resultado.debeMarcarse()) {
                    // Turno ya pasó o notificación inválida - marcar como enviada sin enviar
                    marcarComoEnviadaSinMensaje(notificacion, resultado.getRazon());
                    omitidos++;

                } else {
                    // Aún no es momento de enviar - no hacer nada
                    log.trace("Notificación {} aún no cumple criterio de envío", notificacion.getId());
                }

            } catch (Exception e) {
                errores++;
                log.error("❌ Error procesando notificación {}: {}",
                        notificacion.getId(), e.getMessage(), e);
                // Continúa con la siguiente - no detiene todo el proceso
            }
        }
        log.info("✅ Proceso finalizado - Enviados: {} | Omitidos: {} | Errores: {}",
                enviados, omitidos, errores);
    }

    /**
     * Envía una notificación individual.
     * Cada envío es una transacción independiente.
     */
    @Transactional
    protected void enviarNotificacion(Notificacion notificacion) {
        log.info("📨 Enviando notificación {} a persona {} para turno {}",
                notificacion.getId(),
                notificacion.getPersona().getId(),
                notificacion.getTurno().getId());

        try {
            //Delega el envío al servicio de WhatsApp
            whatsAppService.enviarMensaje(
                   notificacion.getPersona().getTelefono(),
                    notificacion.getMensaje()
            );

            // Actualiza el estado
            notificacion.setEnviada(true);
            notificacionRepository.save(notificacion);

            log.info("✅ Notificación {} enviada correctamente", notificacion.getId());

        } catch (Exception e) {
            log.error("❌ Error al enviar notificación {}", notificacion.getId(), e);
            throw new RuntimeException("Error enviando notificación", e);
        }
    }

    /**
     * Marca una notificación como enviada sin realmente enviar el mensaje.
     * Usado para turnos ya pasados o notificaciones inválidas.
     */
    @Transactional
    protected void marcarComoEnviadaSinMensaje(Notificacion notificacion, String razon) {
        log.warn("⏩ Marcando notificación {} como enviada sin mensaje. Razón: {}",
                notificacion.getId(), razon);

        notificacion.setEnviada(true);
        notificacionRepository.save(notificacion);
    }

    @Transactional
    public void eliminarNotificacionesAntiguas(int diasAntiguedad) {
        LocalDateTime fechaLimite = LocalDateTime.now().minusDays(diasAntiguedad);

        int eliminadas = notificacionRepository.deleteByEnviadaTrueAndFechaCreacionBefore(fechaLimite);

        log.info("🧹 Notificaciones eliminadas: {} (anteriores a {})", eliminadas, fechaLimite);
    }



    /**
     * Clase interna para encapsular el resultado de la validación.
     */
    public static class ResultadoValidacion {
        private final boolean debeEnviarse;
        private final boolean debeMarcarse;
        private final String razon;

        public ResultadoValidacion(boolean debeEnviarse, boolean debeMarcarse, String razon) {
            this.debeEnviarse = debeEnviarse;
            this.debeMarcarse = debeMarcarse;
            this.razon = razon;
        }

        public boolean debeEnviarse() { return debeEnviarse; }
        public boolean debeMarcarse() { return debeMarcarse; }
        public String getRazon() { return razon; }

        public static ResultadoValidacion enviar() {
            return new ResultadoValidacion(true, false, "Cumple criterios de envío");
        }

        public static ResultadoValidacion marcar(String razon) {
            return new ResultadoValidacion(false, true, razon);
        }

        public static ResultadoValidacion esperar() {
            return new ResultadoValidacion(false, false, "Aún no es momento");
        }
    }
}
