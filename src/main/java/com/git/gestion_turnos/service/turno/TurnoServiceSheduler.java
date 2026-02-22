package com.git.gestion_turnos.service.turno;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
public class TurnoServiceSheduler {
    private final TurnoService turnoService;
    private static final Logger log = LoggerFactory.getLogger(TurnoServiceSheduler.class);

    public TurnoServiceSheduler(TurnoService turnoService){
        this.turnoService = turnoService;
    }

    @Scheduled(cron = "0 0 * 15 * ?")
    public void disparar(){
        log.info("🔔 Iniciando generacion de turnos para el siguiente mes");

        try {
            turnoService.generarTurnosMesSiguiente();
            log.info("✅ Turnos generados correctamente");
        }catch (Exception e){
            log.error("Error al generar los turnos", e);
        }
    }
}
