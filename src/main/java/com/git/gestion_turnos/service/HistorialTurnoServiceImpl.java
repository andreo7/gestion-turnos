package com.git.gestion_turnos.service;

import com.git.gestion_turnos.entity.HistorialTurno;
import com.git.gestion_turnos.entity.Turno;
import com.git.gestion_turnos.enums.EstadoTurno;
import com.git.gestion_turnos.repository.HistorialTurnoRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class HistorialTurnoServiceImpl implements IHistorialTurno{
    private final HistorialTurnoRepository historialTurnoRepository;

    public HistorialTurnoServiceImpl(HistorialTurnoRepository historialTurnoRepository){
        this.historialTurnoRepository = historialTurnoRepository;
    }

    //Registra los cambios de estado en el historial al reservar, confirmar o cancelar un turno.
    @Transactional
    public void registrarCambioEstado(@NotNull Turno turno, EstadoTurno estadoTurno){
        HistorialTurno historialTurno = setAtributos(turno);
        historialTurno.setEstadoTurnoActual(estadoTurno);

        historialTurnoRepository.save(historialTurno);
    }

    private HistorialTurno setAtributos(@NotNull Turno turno){
        HistorialTurno historialTurno = new HistorialTurno();
        historialTurno.setTurno(turno);
        historialTurno.setPersona(turno.getPersona());
        historialTurno.setEstadoTurnoAnterior(turno.getEstado());
        historialTurno.setFechaHoraActualizacion(LocalDateTime.now());

        return historialTurno;
    }
}
