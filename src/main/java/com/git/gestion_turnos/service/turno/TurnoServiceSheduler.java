package com.git.gestion_turnos.service.turno;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TurnoServiceSheduler {
    private final TurnoService turnoService;

    public TurnoServiceSheduler(TurnoService turnoService){
        this.turnoService = turnoService;
    }

    @Scheduled(cron = "0 0 * 15 * ?")
    public void disparar(){
        turnoService.generarTurnosMesSiguiente();
    }
}
