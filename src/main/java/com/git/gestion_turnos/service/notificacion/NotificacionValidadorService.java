package com.git.gestion_turnos.service.notificacion;

import com.git.gestion_turnos.entity.Notificacion;
import com.git.gestion_turnos.entity.Turno;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.git.gestion_turnos.service.notificacion.NotificacionEnviadorService.ResultadoValidacion;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


/**
 * Servicio responsable de validar si una notificación debe enviarse.
 */
@Service
public class NotificacionValidadorService {
    private static final Logger log = LoggerFactory.getLogger(NotificacionValidadorService.class);

    // Ventana de tiempo para envío de recordatorios
    private static final long HORAS_MINIMAS_ANTES = 23;
    private static final long HORAS_MAXIMAS_ANTES = 24;

    /**
     * Valida si una notificación debe enviarse en este momento.
     *
     * Reglas de negocio:
     * 1. Si el turno ya pasó → marcar como enviada sin enviar
     * 2. Si faltan entre 23-24 horas → enviar
     * 3. Si faltan más de 24 horas → esperar
     * 4. Si faltan menos de 23 horas → marcar como enviada sin enviar (se perdió la ventana)
     *
     * @param notificacion La notificación a validar
     * @return ResultadoValidacion indicando la acción a tomar
     */
    public ResultadoValidacion validarParaEnvio(Notificacion notificacion) {

        // Validación 1: Verificar que tenga turno asociado
        if (notificacion.getTurno() == null) {
            log.warn("⚠️ Notificación {} no tiene turno asociado", notificacion.getId());
            return ResultadoValidacion.marcar("Sin turno asociado");
        }

        Turno turno = notificacion.getTurno();

        // Validación 2: Verificar que el turno tenga fecha y hora
        if (turno.getFecha() == null || turno.getHora() == null) {
            log.warn("⚠️ Turno {} no tiene fecha/hora completa", turno.getId());
            return ResultadoValidacion.marcar("Turno sin fecha/hora");
        }

        // Calcular tiempo hasta el turno
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime fechaTurno = LocalDateTime.of(turno.getFecha(), turno.getHora());
        long horasHastaTurno = ChronoUnit.HOURS.between(ahora, fechaTurno);

        log.debug("Notificación {}: faltan {} horas para el turno",
                notificacion.getId(), horasHastaTurno);

        // Validación 3: Turno ya pasó
        if (horasHastaTurno < 0) {
            log.warn("⏰ Turno {} ya pasó (hace {} horas)",
                    turno.getId(), Math.abs(horasHastaTurno));
            return ResultadoValidacion.marcar("Turno ya pasado");
        }

        // Validación 4: Ventana de envío (23-24 horas antes)
        if (horasHastaTurno >= HORAS_MINIMAS_ANTES && horasHastaTurno <= HORAS_MAXIMAS_ANTES) {
            log.debug("✅ Notificación {} cumple criterio de envío ({} horas)",
                    notificacion.getId(), horasHastaTurno);
            return ResultadoValidacion.enviar();
        }

        // Validación 5: Faltan más de 24 horas - esperar
        if (horasHastaTurno > HORAS_MAXIMAS_ANTES) {
            log.trace("⏳ Notificación {}: aún faltan {} horas (> 24)",
                    notificacion.getId(), horasHastaTurno);
            return ResultadoValidacion.esperar();
        }

        // Validación 6: Faltan menos de 23 horas - se perdió la ventana
        log.warn("⚠️ Notificación {}: se perdió ventana de envío (faltan {} horas)",
                notificacion.getId(), horasHastaTurno);
        return ResultadoValidacion.marcar("Fuera de ventana de envío");
    }

    /**
     * Verifica si una notificación está dentro de la ventana de envío.
     */
    public boolean estaEnVentanaDeEnvio(LocalDateTime fechaTurno) {
        LocalDateTime ahora = LocalDateTime.now();
        long horasHastaTurno = ChronoUnit.HOURS.between(ahora, fechaTurno);

        return horasHastaTurno >= HORAS_MINIMAS_ANTES
                && horasHastaTurno <= HORAS_MAXIMAS_ANTES;
    }

    /**
     * Calcula cuántas horas faltan para un turno.
     */
    public long calcularHorasHastaTurno(LocalDateTime fechaTurno) {
        return ChronoUnit.HOURS.between(LocalDateTime.now(), fechaTurno);
    }
}
