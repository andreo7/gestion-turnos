package com.git.gestion_turnos.service.turno;

import com.git.gestion_turnos.dto.persona.PersonaDTO;
import com.git.gestion_turnos.dto.turno.TurnoDTO;
import com.git.gestion_turnos.entity.Persona;
import com.git.gestion_turnos.entity.Turno;
import com.git.gestion_turnos.enums.EstadoTurno;
import com.git.gestion_turnos.exception.turno.TurnoClienteNullException;
import com.git.gestion_turnos.exception.turno.TurnoDisponibleException;
import com.git.gestion_turnos.exception.turno.TurnoNoDisponibleException;
import com.git.gestion_turnos.exception.turno.TurnoNotFoundException;
import com.git.gestion_turnos.mapper.TurnoMapper;
import com.git.gestion_turnos.repository.TurnoRepository;
import com.git.gestion_turnos.service.historial_turno.IHistorialTurno;
import com.git.gestion_turnos.service.persona.IPersona;
import jakarta.validation.constraints.NotNull;
import org.springframework.transaction.annotation.Transactional;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.*;

@Service
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

    //Asigna a un turno un cliente existenete. En el caso de que el cliente no exista se crea y guarda en la BD.
    @Transactional
    public TurnoDTO reservarTurno(Integer idTurno, @NonNull PersonaDTO personaDto){
        Turno turno = obtenerTurnoPorId(idTurno);

        if(turno.getEstado() != EstadoTurno.DISPONIBLE){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El turno no esta disponible para reservar");
        }

        Persona persona;
        //Veo si en el body de la request me llega el id de la persona
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

    @Transactional
    public TurnoDTO cancelarTurno(Integer id){
        Turno turno = obtenerTurnoPorId(id);

        if(turno.getEstado() != EstadoTurno.RESERVADO && turno.getEstado() != EstadoTurno.CONFIRMADO){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No es posible cancelar el turno debido a que esta DISPONIBLE");
        }
        if(turno.getPersona() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No es posible cancelar el turno debido a que no tiene un cliente al cual cancelar");
        }

        historialTurno.registrarCambioEstado(turno, EstadoTurno.CANCELADO);
        turno.setPersona(null);
        turno.setEstado(EstadoTurno.DISPONIBLE);
        turnoRepository.save(turno);

        return turnoMapper.toDto(turno);
    }

    @Transactional
    public void confirmarTurno(Turno turno){
        Turno turnoConfirmado = obtenerTurnoPorId(turno.getId());

        if(turno.getEstado() != EstadoTurno.RESERVADO){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No es posible confirmar un turno no reservado");
        }
        if(turno.getPersona() != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No es posible confirmar el turno si no tiene una persona asociada");
        }

        historialTurno.registrarCambioEstado(turno, EstadoTurno.CONFIRMADO);
        turnoConfirmado.setEstado(EstadoTurno.CONFIRMADO);
        turnoRepository.save(turnoConfirmado);
    }

    //METODO AUXILIAR PARA EVITAR REPETIR CODIGO.
    private Turno obtenerTurnoPorId(@NotNull Integer id){
        return turnoRepository.findById(id).orElseThrow(() ->
                new TurnoNotFoundException(id));
    }
}
