package com.git.gestion_turnos;

import com.git.gestion_turnos.entity.Notificacion;
import com.git.gestion_turnos.entity.Turno;
import com.git.gestion_turnos.service.notificacion.NotificacionEnviadorService.ResultadoValidacion;
import com.git.gestion_turnos.service.notificacion.NotificacionValidadorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para NotificacionValidadorService.
 */
class NotificacionValidadorServiceTest {

    private NotificacionValidadorService validador;

    @BeforeEach
    void setUp() {
        validador = new NotificacionValidadorService();
    }

    @Test
    @DisplayName("Debe enviar cuando faltan 23 horas exactas")
    void debeEnviarCuandoFaltan23Horas() {
        // Given: Notificación con turno en 23 horas
        Notificacion notificacion = crearNotificacionConTurnoEn(23);

        // When: Validar
        ResultadoValidacion resultado = validador.validarParaEnvio(notificacion);

        // Then: Debe enviarse
        assertTrue(resultado.debeEnviarse(), "Debe enviarse cuando faltan 23h");
        assertFalse(resultado.debeMarcarse());
    }

    @Test
    @DisplayName("Debe enviar cuando faltan 24 horas exactas")
    void debeEnviarCuandoFaltan24Horas() {
        // Given: Notificación con turno en 24 horas
        Notificacion notificacion = crearNotificacionConTurnoEn(24);

        // When
        ResultadoValidacion resultado = validador.validarParaEnvio(notificacion);

        // Then
        assertTrue(resultado.debeEnviarse());
    }

    @Test
    @DisplayName("Debe esperar cuando faltan más de 24 horas")
    void debeEsperarCuandoFaltanMasDe24Horas() {
        // Given: Notificación con turno en 48 horas
        Notificacion notificacion = crearNotificacionConTurnoEn(48);

        // When
        ResultadoValidacion resultado = validador.validarParaEnvio(notificacion);

        // Then: No debe enviarse ni marcarse
        assertFalse(resultado.debeEnviarse());
        assertFalse(resultado.debeMarcarse());
    }

    @Test
    @DisplayName("Debe marcar cuando el turno ya pasó")
    void debeMarcarCuandoTurnoYaPaso() {
        // Given: Notificación con turno hace 5 horas
        Notificacion notificacion = crearNotificacionConTurnoEn(-5);

        // When
        ResultadoValidacion resultado = validador.validarParaEnvio(notificacion);

        // Then: Debe marcarse como enviada sin enviar
        assertFalse(resultado.debeEnviarse());
        assertTrue(resultado.debeMarcarse());
        assertEquals("Turno ya pasado", resultado.getRazon());
    }

    @Test
    @DisplayName("Debe marcar cuando se perdió la ventana de envío")
    void debeMarcarCuandoSePerdioVentana() {
        // Given: Notificación con turno en 10 horas (< 23)
        Notificacion notificacion = crearNotificacionConTurnoEn(10);

        // When
        ResultadoValidacion resultado = validador.validarParaEnvio(notificacion);

        // Then
        assertFalse(resultado.debeEnviarse());
        assertTrue(resultado.debeMarcarse());
        assertEquals("Fuera de ventana de envío", resultado.getRazon());
    }

    @Test
    @DisplayName("Debe marcar cuando la notificación no tiene turno")
    void debeMarcarCuandoNoHayTurno() {
        // Given: Notificación sin turno
        Notificacion notificacion = new Notificacion();
        notificacion.setId(1);
        notificacion.setTurno(null);

        // When
        ResultadoValidacion resultado = validador.validarParaEnvio(notificacion);

        // Then
        assertFalse(resultado.debeEnviarse());
        assertTrue(resultado.debeMarcarse());
        assertEquals("Sin turno asociado", resultado.getRazon());
    }

    @Test
    @DisplayName("Debe marcar cuando el turno no tiene fecha")
    void debeMarcarCuandoTurnoSinFecha() {
        // Given: Turno sin fecha
        Turno turno = new Turno();
        turno.setId(1);
        turno.setFecha(null);
        turno.setHora(LocalTime.of(10, 0));

        Notificacion notificacion = new Notificacion();
        notificacion.setId(1);
        notificacion.setTurno(turno);

        // When
        ResultadoValidacion resultado = validador.validarParaEnvio(notificacion);

        // Then
        assertTrue(resultado.debeMarcarse());
        assertEquals("Turno sin fecha/hora", resultado.getRazon());
    }

    @Test
    @DisplayName("estaEnVentanaDeEnvio debe retornar true para 23-24 horas")
    void estaEnVentanaDeEnvio() {
        // Given: Fecha en 23.5 horas
        LocalDateTime fecha = LocalDateTime.now().plusHours(23).plusMinutes(30);

        // When
        boolean resultado = validador.estaEnVentanaDeEnvio(fecha);

        // Then
        assertTrue(resultado);
    }

    @Test
    @DisplayName("calcularHorasHastaTurno debe retornar valor correcto")
    void calcularHorasHastaTurno() {
        // Given: Fecha en 48 horas
        LocalDateTime fecha = LocalDateTime.now().plusHours(48);

        // When
        long horas = validador.calcularHorasHastaTurno(fecha);

        // Then
        assertEquals(48, horas);
    }

    // --- HELPERS ---

    /**
     * Crea una notificación con turno programado en X horas desde ahora.
     *
     * @param horasDesdeAhora Horas futuras (positivo) o pasadas (negativo)
     */
    private Notificacion crearNotificacionConTurnoEn(long horasDesdeAhora) {
        LocalDateTime fechaTurno = LocalDateTime.now().plusHours(horasDesdeAhora);

        Turno turno = new Turno();
        turno.setId(1);
        turno.setFecha(fechaTurno.toLocalDate());
        turno.setHora(fechaTurno.toLocalTime());

        Notificacion notificacion = new Notificacion();
        notificacion.setId(1);
        notificacion.setTurno(turno);

        return notificacion;
    }
}