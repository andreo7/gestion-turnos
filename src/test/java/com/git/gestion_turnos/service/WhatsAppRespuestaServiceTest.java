package com.git.gestion_turnos.service;

import com.git.gestion_turnos.dto.whatsapp.WhatsAppWebhookDTO;
import com.git.gestion_turnos.entity.Notificacion;
import com.git.gestion_turnos.entity.Persona;
import com.git.gestion_turnos.entity.Turno;
import com.git.gestion_turnos.enums.EstadoTurno;
import com.git.gestion_turnos.enums.TipoNotificacion;
import com.git.gestion_turnos.repository.NotificacionRepository;
import com.git.gestion_turnos.repository.PersonaRepository;
import com.git.gestion_turnos.service.notificacion.INotificacion;
import com.git.gestion_turnos.service.turno.ITurnoReserva;
import com.git.gestion_turnos.service.whatsapp.WhatsAppRespuestaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests para WhatsAppRespuestaService.
 */
@ExtendWith(MockitoExtension.class)
class WhatsAppRespuestaServiceTest {

    @Mock
    private NotificacionRepository notificacionRepository;

    @Mock
    private PersonaRepository personaRepository;

    @Mock
    private INotificacion notificacionService;

    @Mock
    private ITurnoReserva turnoService;

    @InjectMocks
    private WhatsAppRespuestaService respuestaService;

    private Persona persona;
    private Turno turno;
    private Notificacion notificacion;

    @BeforeEach
    void setUp() {
        // Setup persona
        persona = new Persona();
        persona.setId(1);
        persona.setNombre("Juan");
        persona.setApellido("Pérez");
        persona.setTelefono("+5491112345678");

        // Setup turno
        turno = new Turno();
        turno.setId(1);
        turno.setFecha(LocalDate.now().plusDays(1));
        turno.setHora(LocalTime.of(10, 0));
        turno.setEstado(EstadoTurno.RESERVADO);
        turno.setPersona(persona);

        // Setup notificación
        notificacion = new Notificacion();
        notificacion.setId(1);
        notificacion.setPersona(persona);
        notificacion.setTurno(turno);
        notificacion.setTipo(TipoNotificacion.RECORDATORIO);
        notificacion.setEnviada(true);
        notificacion.setRespondida(false);
        notificacion.setFechaCreacion(LocalDateTime.now().minusHours(1));
    }

    @Test
    @DisplayName("Debe confirmar turno cuando usuario responde SI")
    void debeConfirmarTurnoCuandoRespuestaSI() {
        // Given
        WhatsAppWebhookDTO webhook = new WhatsAppWebhookDTO();
        webhook.setFrom("whatsapp:+5491112345678");
        webhook.setBody("SI");

        when(personaRepository.findByTelefono("+5491112345678")).thenReturn(persona);
        when(notificacionRepository.findByPersonaIdAndEnviadaTrueAndRespondidaFalseAndTipo(
                1, TipoNotificacion.RECORDATORIO
        )).thenReturn(List.of(notificacion));

        // When
        String respuesta = respuestaService.procesarRespuesta(webhook);

        // Then
        verify(turnoService).confirmarTurno(1);
        verify(notificacionService).marcarComoRespondida(1);
        assertTrue(respuesta.contains("CONFIRMADO"));
    }

    @Test
    @DisplayName("Debe cancelar turno cuando usuario responde NO")
    void debeCancelarTurnoCuandoRespuestaNO() {
        // Given
        WhatsAppWebhookDTO webhook = new WhatsAppWebhookDTO();
        webhook.setFrom("whatsapp:+5491112345678");
        webhook.setBody("NO");

        when(personaRepository.findByTelefono("+5491112345678")).thenReturn(persona);
        when(notificacionRepository.findByPersonaIdAndEnviadaTrueAndRespondidaFalseAndTipo(
                1, TipoNotificacion.RECORDATORIO
        )).thenReturn(List.of(notificacion));

        // When
        String respuesta = respuestaService.procesarRespuesta(webhook);

        // Then
        verify(turnoService).cancelarTurno(1);
        verify(notificacionService).marcarComoRespondida(1);
        assertTrue(respuesta.contains("CANCELADO"));
    }

    @Test
    @DisplayName("Debe aceptar variantes de SI")
    void debeAceptarVariantesDeSI() {
        // Given
        String[] variantes = {"si", "SI", "sí", "SÍ", "daLE", "DALE", "ok", "OK", "confirmar", "1"};

        for (String variante : variantes) {
            WhatsAppWebhookDTO webhook = new WhatsAppWebhookDTO();
            webhook.setFrom("whatsapp:+5491112345678");
            webhook.setBody(variante);

            when(personaRepository.findByTelefono(any())).thenReturn(persona);
            when(notificacionRepository.findByPersonaIdAndEnviadaTrueAndRespondidaFalseAndTipo(
                    any(), any()
            )).thenReturn(List.of(notificacion));

            // When
            String respuesta = respuestaService.procesarRespuesta(webhook);

            // Then
            assertTrue(respuesta.contains("CONFIRMADO"),
                    "Variante '" + variante + "' debería confirmar");
        }
    }

    @Test
    @DisplayName("Debe retornar mensaje de ayuda si respuesta no reconocida")
    void debeRetornarAyudaSiRespuestaNoReconocida() {
        // Given
        WhatsAppWebhookDTO webhook = new WhatsAppWebhookDTO();
        webhook.setFrom("whatsapp:+5491112345678");
        webhook.setBody("HOLA");

        when(personaRepository.findByTelefono("+5491112345678")).thenReturn(persona);
        when(notificacionRepository.findByPersonaIdAndEnviadaTrueAndRespondidaFalseAndTipo(
                1, TipoNotificacion.RECORDATORIO
        )).thenReturn(List.of(notificacion));

        // When
        String respuesta = respuestaService.procesarRespuesta(webhook);

        // Then
        verify(turnoService, never()).confirmarTurno(any());
        verify(turnoService, never()).cancelarTurno(any());
        assertTrue(respuesta.contains("No entendimos"));
    }

    @Test
    @DisplayName("Debe manejar persona no encontrada")
    void debeManejarPersonaNoEncontrada() {
        // Given
        WhatsAppWebhookDTO webhook = new WhatsAppWebhookDTO();
        webhook.setFrom("whatsapp:+5499999999999");
        webhook.setBody("SI");

        when(personaRepository.findByTelefono(any())).thenReturn(null);

        // When
        String respuesta = respuestaService.procesarRespuesta(webhook);

        // Then
        assertTrue(respuesta.contains("No encontramos su número"));
        verify(turnoService, never()).confirmarTurno(any());
    }

    @Test
    @DisplayName("Debe manejar notificación ya respondida (idempotencia)")
    void debeManejarNotificacionYaRespondida() {
        // Given
        notificacion.setRespondida(true);

        WhatsAppWebhookDTO webhook = new WhatsAppWebhookDTO();
        webhook.setFrom("whatsapp:+5491112345678");
        webhook.setBody("SI");

        when(personaRepository.findByTelefono("+5491112345678")).thenReturn(persona);
        when(notificacionRepository.findByPersonaIdAndEnviadaTrueAndRespondidaFalseAndTipo(
                1, TipoNotificacion.RECORDATORIO
        )).thenReturn(List.of(notificacion));

        // When
        String respuesta = respuestaService.procesarRespuesta(webhook);

        // Then
        assertTrue(respuesta.contains("ya fue procesada"));
        verify(turnoService, never()).confirmarTurno(any());
    }

    @Test
    @DisplayName("Debe validar respuestas correctamente")
    void debeValidarRespuestas() {
        // Respuestas válidas
        assertTrue(respuestaService.esRespuestaValida("SI"));
        assertTrue(respuestaService.esRespuestaValida("NO"));
        assertTrue(respuestaService.esRespuestaValida("sí"));
        assertTrue(respuestaService.esRespuestaValida("ok"));
        assertTrue(respuestaService.esRespuestaValida("cancelar"));

        // Respuestas inválidas
        assertFalse(respuestaService.esRespuestaValida("HOLA"));
        assertFalse(respuestaService.esRespuestaValida("DALE GRACIAS"));
        assertFalse(respuestaService.esRespuestaValida("GRACIAS"));
        assertFalse(respuestaService.esRespuestaValida(null));
        assertFalse(respuestaService.esRespuestaValida(""));
    }
}


