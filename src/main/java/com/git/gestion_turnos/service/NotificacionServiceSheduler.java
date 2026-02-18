package com.git.gestion_turnos.service;

import com.git.gestion_turnos.entity.Notificacion;
import com.git.gestion_turnos.repository.NotificacionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class NotificacionServiceSheduler {
    private NotificacionRepository notificacionRepository;

    public NotificacionServiceSheduler(NotificacionRepository notificacionRepository){
        this.notificacionRepository = notificacionRepository;
    }

    @Scheduled(fixedRate = 60000) // cada 1 minuto
    public void enviarRecordatorios() {
        List<Notificacion> pendientes = notificacionRepository
                .findByEnviadaFalse();

        LocalDateTime ahora = LocalDateTime.now();
        int enviados = 0;

        for (Notificacion n : pendientes) {

            LocalDateTime fechaTurno = LocalDateTime.of(
                    n.getTurno().getFecha(),
                    n.getTurno().getHora()
            );

            //Calcular horas que faltan para el turno
            long horasHastaTurno = ChronoUnit.HOURS.between(ahora, fechaTurno);

            if (horasHastaTurno <= 24 && horasHastaTurno >= 23) {

                // enviar mensaje

                n.setEnviada(true);
                notificacionRepository.save(n);
                enviados++;
            }
        }
    }
}
