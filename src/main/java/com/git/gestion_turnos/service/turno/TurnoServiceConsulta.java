package com.git.gestion_turnos.service.turno;

import com.git.gestion_turnos.dto.turno.TurnoDTO;
import com.git.gestion_turnos.entity.Turno;
import com.git.gestion_turnos.enums.EstadoTurno;
import com.git.gestion_turnos.exception.turno.TurnoNotFoundException;
import com.git.gestion_turnos.mapper.TurnoMapper;
import com.git.gestion_turnos.repository.TurnoRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TurnoServiceConsulta implements ITurnoConsulta{
    private TurnoRepository turnoRepository;
    private final TurnoMapper turnoMapper;

    public TurnoServiceConsulta(TurnoRepository turnoRepository, TurnoMapper turnoMapper){
        this.turnoMapper = turnoMapper;
        this.turnoRepository = turnoRepository;
    }

    public List<TurnoDTO> findAll(){
        List<Turno> turnos = turnoRepository.findAll();
        List<TurnoDTO> turnosDto = new ArrayList<>();

        for(Turno t: turnos){
            TurnoDTO turnoDto = turnoMapper.toDto(t);
            turnosDto.add(turnoDto);
        }

        return turnosDto;
    }

    public List<TurnoDTO> verTurnosDisponibles(){
        List<Turno> turnos = turnoRepository.findAll();
        List<TurnoDTO> turnosDto = new ArrayList<>();

        for(Turno t: turnos){
            if(t.getEstado() == EstadoTurno.DISPONIBLE){
                TurnoDTO turnoDto = turnoMapper.toDto(t);
                turnosDto.add(turnoDto);
            }
        }

        return turnosDto;
    }

    public List<TurnoDTO> verTurnosOcupados(){
        List<Turno> turnos = turnoRepository.findAll();
        List<TurnoDTO> turnosDto = new ArrayList<>();

        for(Turno t: turnos){
            if(t.getEstado() == EstadoTurno.RESERVADO || t.getEstado() == EstadoTurno.CONFIRMADO){
                TurnoDTO turnoDto = turnoMapper.toDto(t);
                turnosDto.add(turnoDto);
            }
        }

        return turnosDto;
    }

    public TurnoDTO findById(Integer id){
        Turno turno = obtenerTurnoPorId(id);

        return turnoMapper.toDto(turno);
    }

    //METODO AUXILIAR PARA EVITAR REPETIR CODIGO.
    private Turno obtenerTurnoPorId(@NotNull Integer id){
        return turnoRepository.findById(id).orElseThrow(() ->
                new TurnoNotFoundException(id));
    }
}
