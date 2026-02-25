package com.git.gestion_turnos.service.turno;

import com.git.gestion_turnos.dto.persona.PersonaDTO;
import com.git.gestion_turnos.dto.turno.TurnoDTO;
import com.git.gestion_turnos.entity.Persona;
import com.git.gestion_turnos.entity.Turno;
import com.git.gestion_turnos.enums.EstadoTurno;
import com.git.gestion_turnos.exception.turno.*;
import com.git.gestion_turnos.mapper.TurnoMapper;
import com.git.gestion_turnos.repository.TurnoRepository;
import com.git.gestion_turnos.service.historial_turno.IHistorialTurno;
import com.git.gestion_turnos.service.persona.IPersona;
import jakarta.validation.constraints.NotNull;
import org.springframework.transaction.annotation.Transactional;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.time.*;

@Service
@Transactional
public class TurnoServiceReserva implements ITurnoReserva {
    private final TurnoRepository turnoRepository;
    private final IHistorialTurno historialTurno;
    private final IPersona personaService;
    private final TurnoMapper turnoMapper;

    //Inyecto los componentes que TurnoService necesita para funcionar.
    public TurnoServiceReserva(TurnoRepository turnoRepository, IPersona personaService, TurnoMapper turnoMapper, IHistorialTurno historialTurno){
        this.turnoRepository = turnoRepository;
        this.personaService = personaService;
        this.turnoMapper = turnoMapper;
        this.historialTurno = historialTurno;
    }


    public TurnoDTO reservarTurno(Integer idTurno, @NonNull PersonaDTO personaDto){
        Turno turno = obtenerTurnoPorId(idTurno);

        if(turno.getEstado() != EstadoTurno.DISPONIBLE){
            throw new TurnoNoDisponibleException(turno.getId());
        }

        Persona persona;

        //Si el id de la persona me llega en la request lo devuelve.
        if(personaDto.getId() != null){
            persona = personaService.getById(personaDto.getId());
        }else{
            persona = personaService.obtenerPersonaOCrear(personaDto);
        }

        turno.setPersona(persona);
        historialTurno.registrarCambioEstado(turno, EstadoTurno.RESERVADO);
        turno.setEstado(EstadoTurno.RESERVADO);
        turnoRepository.save(turno);

        return turnoMapper.toDto(turno);
    }

    public TurnoDTO cancelarTurno(Integer id){
        Turno turno = obtenerTurnoPorId(id);

        if(turno.getEstado() != EstadoTurno.RESERVADO && turno.getEstado() != EstadoTurno.CONFIRMADO){
            throw new TurnoDisponibleException(turno.getId());
        }
        if(turno.getPersona() == null){
            throw new TurnoClienteNullException(turno.getId());
        }

        historialTurno.registrarCambioEstado(turno, EstadoTurno.CANCELADO);
        turno.setPersona(null);
        turno.setEstado(EstadoTurno.DISPONIBLE);
        turnoRepository.save(turno);

        return turnoMapper.toDto(turno);
    }

    public void confirmarTurno(Integer id){
        Turno turno = obtenerTurnoPorId(id);

        if(turno.getEstado() != EstadoTurno.RESERVADO){
            throw new TurnoNoReservadoException(turno.getId());
        }
        if(turno.getPersona() == null){
            throw new TurnoClienteNullException(turno.getId());
        }

        historialTurno.registrarCambioEstado(turno, EstadoTurno.CONFIRMADO);
        turno.setEstado(EstadoTurno.CONFIRMADO);
        turnoRepository.save(turno);
    }

    //METODO AUXILIAR PARA EVITAR REPETIR CODIGO.
    private Turno obtenerTurnoPorId(@NotNull Integer id){
        return turnoRepository.findById(id).orElseThrow(() ->
                new TurnoNotFoundException(id));
    }
}
