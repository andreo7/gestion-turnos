package com.git.gestion_turnos.service.persona;

import com.git.gestion_turnos.dto.historial_turno.HistorialDetalleDTO;
import com.git.gestion_turnos.dto.persona.PersonaDTO;
import com.git.gestion_turnos.dto.persona.PersonaDetalleDTO;
import com.git.gestion_turnos.entity.Persona;
import com.git.gestion_turnos.enums.EstadoTurno;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    Page<HistorialDetalleDTO> listarHistorialDePersona(Integer personaId, EstadoTurno estadoTurno, Pageable pageable);
}
