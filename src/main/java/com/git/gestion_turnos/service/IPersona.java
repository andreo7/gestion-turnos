package com.git.gestion_turnos.service;

import java.util.List;

import com.git.gestion_turnos.dto.PersonaDTO;
import com.git.gestion_turnos.entity.Persona;

public interface IPersona {
    PersonaDTO save(PersonaDTO personaDTO);
    List<PersonaDTO> findAll();
    PersonaDTO findById(Integer id);
    void deleteById(Integer id);
    PersonaDTO update(Integer id, PersonaDTO dto);
    Persona findByNombreAndApellidoAndTelefono(String nombre, String apellido, String telefono);
    Persona getById(Integer id);
}
