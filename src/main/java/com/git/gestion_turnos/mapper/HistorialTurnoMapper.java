package com.git.gestion_turnos.mapper;

import com.git.gestion_turnos.dto.HistorialTurnoDTO;
import com.git.gestion_turnos.entity.HistorialTurno;
import com.git.gestion_turnos.entity.Persona;
import com.git.gestion_turnos.entity.Turno;
import org.jspecify.annotations.NonNull;

public class HistorialTurnoMapper {

    public HistorialTurnoDTO toDTO(@NonNull HistorialTurno historialTurno){
        HistorialTurnoDTO historialTurnoDto = new HistorialTurnoDTO();
        historialTurnoDto.setEstadoTurnoActual(historialTurno.getEstadoTurnoActual());
        historialTurnoDto.setEstadoTurnoAnterior(historialTurno.getEstadoTurnoAnterior());
        historialTurnoDto.setFechaActualizacion(historialTurno.getFechaHoraActualizacion());

        if(historialTurno.getTurno() != null){
            historialTurnoDto.setTurnoId(historialTurno.getTurno().getId());
        }

        if(historialTurno.getPersona() != null){
            historialTurnoDto.setPersonaId(historialTurno.getPersona().getId());
        }

        return historialTurnoDto;
    }

    public HistorialTurno toEntity(@NonNull HistorialTurnoDTO historialTurnoDto){
        HistorialTurno historialTurno = new HistorialTurno();
        historialTurno.setEstadoTurnoActual(historialTurnoDto.getEstadoTurnoActual());
        historialTurno.setEstadoTurnoAnterior(historialTurnoDto.getEstadoTurnoAnterior());
        historialTurno.setFechaHoraActualizacion(historialTurnoDto.getFechaActualizacion());

        if(historialTurnoDto.getTurnoId() != null){
            Turno turno = new Turno();
            turno.setId(historialTurno.getTurno().getId());
            historialTurno.setTurno(turno);
        }

        if(historialTurnoDto.getPersonaId() != null){
            Persona persona = new Persona();
            persona.setId(historialTurno.getPersona().getId());
            historialTurno.setPersona(persona);
        }

        return historialTurno;
    }
}
