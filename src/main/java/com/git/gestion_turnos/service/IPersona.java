package com.git.gestion_turnos.service;

import java.util.List;

import com.git.gestion_turnos.dto.PersonaDTO;
import com.git.gestion_turnos.entity.Persona;

public interface IPersona {
    PersonaDTO save(PersonaDTO personaDTO);
    List<Persona> findAll();
    Persona findById(Integer id);
    void deleteById(Integer id);
}
