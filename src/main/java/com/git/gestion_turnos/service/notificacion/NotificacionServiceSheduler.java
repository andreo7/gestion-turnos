package com.git.gestion_turnos.service.notificacion;

import com.git.gestion_turnos.entity.Notificacion;
import com.git.gestion_turnos.repository.NotificacionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class NotificacionServiceSheduler {

    private NotificacionRepository notificacionRepository;

    private static final Logger log = LoggerFactory.getLogger(NotificacionServiceSheduler.class);

    public NotificacionServiceSheduler(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    //Scheduled para mandar mensaje a las personas que le falten entre 24 y 23 horas
    //y no se les haya mandado ya el recordatorio.
    @Scheduled(fixedRate = 60000) // cada 1 minuto
    @Transactional
    public void enviarRecordatorios() {
        try {
            log.info("🔔 Iniciando verificación de recordatorios...");

            List<Notificacion> pendientes = notificacionRepository
                    .findByEnviadaFalse();

            log.info("📋 Notificaciones pendientes encontradas: {}", pendientes.size());

            LocalDateTime ahora = LocalDateTime.now();
            int enviados = 0;

            for (Notificacion n : pendientes) {
                try {
                    // Validar que el turno no sea null
                    if (n.getTurno() == null) {
                        log.warn("⚠️ Notificación {} no tiene turno asociado", n.getId());
                        continue;
                    }

                    LocalDateTime fechaTurno = LocalDateTime.of(
                            n.getTurno().getFecha(),
                            n.getTurno().getHora()
                    );

                    //Calcular horas que faltan para el turno
                    long horasHastaTurno = ChronoUnit.HOURS.between(ahora, fechaTurno);

                    if (horasHastaTurno <= 24 && horasHastaTurno >= 23) {
                        log.info("📨 Enviando recordatorio para turno {}: {} horas restantes",
                                n.getTurno().getId(), horasHastaTurno);

                        // enviar mensaje

                        n.setEnviada(true);
                        notificacionRepository.save(n);
                        enviados++;

                        log.info("✅ Recordatorio enviado correctamente para notificación {}", n.getId());
                    } else if (horasHastaTurno < 0) {
                        // El turno ya pasó
                        log.warn("⏰ Turno {} ya pasó. Marcando notificación como no enviada.",
                                n.getTurno().getId());
                        n.setEnviada(true);
                        notificacionRepository.save(n);
                    }
                } catch (Exception e) {
                    log.error("❌ Error procesando notificación {}: {}", n.getId(), e.getMessage(), e);
                    // Continúa con la siguiente notificación
                }
            }
            log.info("✅ Proceso completado. Recordatorios enviados: {}/{}", enviados, pendientes.size());
        } catch (Exception e) {
            log.error("❌ Error en el scheduler de notificaciones: {}", e.getMessage(), e);
        }
    }
}