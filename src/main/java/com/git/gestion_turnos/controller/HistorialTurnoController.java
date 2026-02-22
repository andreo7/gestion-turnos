package com.git.gestion_turnos.controller;

import com.git.gestion_turnos.dto.historial_turno.HistorialTurnoMensualDTO;
import com.git.gestion_turnos.enums.EstadoTurno;
import com.git.gestion_turnos.service.historial_turno.IHistorialTurno;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/historial-turnos")
public class HistorialTurnoController {
    private final IHistorialTurno iHistorialTurno;

    public HistorialTurnoController(IHistorialTurno iHistorialTurno) {
        this.iHistorialTurno = iHistorialTurno;
    }

    @GetMapping()
    public HistorialTurnoMensualDTO totalTurnosMensualesConEstado(@RequestParam LocalDate fechaInicio,
                                                                  @RequestParam LocalDate fechaFin){
        return iHistorialTurno.totalTurnosMensualesConEstado(fechaInicio, fechaFin);
    }
}
