package com.git.gestion_turnos.service;

import java.util.List;

import com.git.gestion_turnos.dto.PersonaDTO;

public interface IPersona {
    PersonaDTO save(PersonaDTO personaDTO);
    List<PersonaDTO> findAll();
    PersonaDTO findById(Integer id);
    void deleteById(Integer id);
    PersonaDTO update(Integer id, PersonaDTO dto);
}
