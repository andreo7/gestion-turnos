package com.git.gestion_turnos.service;

import java.util.List;

import com.git.gestion_turnos.dto.PersonaDTO;
import com.git.gestion_turnos.dto.PersonaDetalleDTO;
import com.git.gestion_turnos.entity.Persona;
import org.springframework.data.domain.Page;

public interface IPersona {
    PersonaDTO save(PersonaDTO personaDTO);
    Page<PersonaDTO> findAll(int page, int size);
    PersonaDetalleDTO findById(Integer id);
    void deleteById(Integer id);
    PersonaDTO update(Integer id, PersonaDTO dto);
    Persona findByNombreAndApellidoAndTelefono(String nombre, String apellido, String telefono);
    Persona getById(Integer id);
    Persona obtenerPersonaOCrear(PersonaDTO personaDto);
    PersonaDetalleDTO obtenerDetalle(Integer id);
}
