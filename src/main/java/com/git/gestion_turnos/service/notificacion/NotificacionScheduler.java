package com.git.gestion_turnos.service.notificacion;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduler responsable ÚNICAMENTE de disparar tareas programadas.
 */
@Component
public class NotificacionScheduler {
    private static final Logger log = LoggerFactory.getLogger(NotificacionScheduler.class);

    private final NotificacionEnviadorService enviadorService;

    public NotificacionScheduler(NotificacionEnviadorService enviadorService) {
        this.enviadorService = enviadorService;
    }

    /**
     * Dispara el envío de recordatorios cada minuto.

     */
    //Se usa fixedDelayString porque se puede modificar desde application properties, si no tiene valor ahi usa 60000
    //Se usa fixedDelay y no fixedRate porque fixedDelay empieza a "contar" despues de que termino la anterior ejecucion
    @Scheduled(fixedDelayString = "${notificacion.scheduler.rate:60000}")
    public void enviarRecordatorios() {
        log.info("🔔 Disparando envío de recordatorios programados");

        try {
            enviadorService.procesarYEnviarRecordatorios();
            log.info("✅ Proceso de recordatorios completado");

        } catch (Exception e) {
            // Solo loguea - no propaga porque sino el scheduler se detiene
            log.error("❌ Error crítico en scheduler de notificaciones", e);
        }
    }

    /**
     * Limpieza de notificaciones antiguas (ejecutar diariamente a las 2am).
     *
     * Opcional: eliminar notificaciones enviadas con más de 30 días.
     */
    @Scheduled(cron = "${notificacion.scheduler.cleanup:0 0 2 * * *}")
    public void limpiarNotificacionesAntiguas() {
        log.info("🧹 Iniciando limpieza de notificaciones antiguas");

        try {
            enviadorService.eliminarNotificacionesAntiguas(30);
            log.info("✅ Limpieza completada");

        } catch (Exception e) {
            log.error("❌ Error en limpieza de notificaciones", e);
        }
    }

}
