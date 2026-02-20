package com.git.gestion_turnos.mapper;

import com.git.gestion_turnos.dto.persona.PersonaDTO;
import com.git.gestion_turnos.dto.turno.TurnoDTO;
import com.git.gestion_turnos.entity.Persona;
import com.git.gestion_turnos.entity.Turno;
import org.springframework.stereotype.Component;

@Component
public class TurnoMapper {
    private PersonaMapper personaMapper;

    public TurnoMapper(PersonaMapper personaMapper) {
        this.personaMapper = personaMapper;
    }

    public TurnoDTO toDto(Turno turno) {
        TurnoDTO turnoDto = new TurnoDTO();
        turnoDto.setId(turno.getId());
        turnoDto.setFecha(turno.getFecha());
        turnoDto.setHora(turno.getHora());
        turnoDto.setEstado(turno.getEstado());

        Persona persona = turno.getPersona();
        if(persona != null) {
            PersonaDTO personaDTO = personaMapper.toDTO(persona);
            turnoDto.setCliente(personaDTO);
        }

        return turnoDto;
    }

    //No se setea id ni estado porque son campos que no deberian venir en el body de la request.
    //id y estado son definidos por el backend (BD / logica de negocio)
    public Turno toEntity(TurnoDTO turnoDto) {
        Turno turno = new Turno();
        turno.setFecha(turnoDto.getFecha());
        turno.setHora(turnoDto.getHora());

        PersonaDTO personaDto = turnoDto.getCliente();
        Persona persona = personaMapper.toEntity(personaDto);
        turno.setPersona(persona);

        return turno;
    }
}
