package com.git.gestion_turnos.mapper;

import com.git.gestion_turnos.dto.PersonaDTO;
import com.git.gestion_turnos.entity.Persona;

public class PersonaMapper {

    public PersonaDTO toDTO(Persona persona){
        PersonaDTO pdto = new PersonaDTO();
        pdto.setId(persona.getId());
        pdto.setNombre(persona.getNombre());
        pdto.setTelefono(persona.getTelefono());
        pdto.setApellido(persona.getApellido());
        return pdto;
    }

    public Persona toEntity(PersonaDTO personaDTO){
        Persona persona = new Persona();
        persona.setId(personaDTO.getId());
        persona.setNombre(personaDTO.getNombre());
        persona.setTelefono(personaDTO.getTelefono());
        persona.setApellido(personaDTO.getApellido());
        return persona;
    }
}
