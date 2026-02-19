package com.git.gestion_turnos.service;

import java.util.ArrayList;
import java.util.List;

import com.git.gestion_turnos.dto.PersonaDetalleDTO;
import com.git.gestion_turnos.enums.EstadoTurno;
import com.git.gestion_turnos.mapper.PersonaMapper;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.git.gestion_turnos.dto.PersonaDTO;
import com.git.gestion_turnos.entity.Persona;
import com.git.gestion_turnos.repository.PersonaRepository;

@Service
public class PersonaServiceImpl implements IPersona{

    private PersonaRepository personaRepository;

    private PersonaMapper personaMapper;

    private IHistorialTurno historialTurno;

    //Se inyecta por constructor el bean que el service necesita para funcionar.
    public PersonaServiceImpl(PersonaRepository personaRepository,
                              PersonaMapper personaMapper,
                              IHistorialTurno historialTurno){
        this.personaRepository = personaRepository;
        this.personaMapper = personaMapper;
        this.historialTurno = historialTurno;
    }

    @Override
    public PersonaDTO save(PersonaDTO dto) {
        Persona persona = personaMapper.toEntity(dto);
        Persona guardada = personaRepository.save(persona);
        PersonaDTO respuesta = personaMapper.toDTO(guardada);
        return respuesta;
    }

    @Override
    public Page<PersonaDTO> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<Persona> pagePersona = personaRepository.findAll(pageable);
        return pagePersona.map(personaMapper ::toDTO);
    }

    @Override
    public PersonaDetalleDTO findById(Integer id) {
        Persona persona = personaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Persona no encontrada"));

        return  obtenerDetalle(persona.getId());
    }

    public Persona findByNombreAndApellidoAndTelefono(String nombre,String apellido, String telefono){
        return personaRepository.findByNombreAndApellidoAndTelefono(nombre, apellido, telefono);
    }

    public Persona getById(Integer id){
        return personaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));
    }

    @Override
    public void deleteById(Integer id) {
        personaRepository.deleteById(id);
    }

    @Override
    public PersonaDTO update(Integer id, PersonaDTO dto) {
        Persona personaBD = personaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Persona no encontrada"));

        personaBD.setNombre(dto.getNombre());
        personaBD.setTelefono(dto.getTelefono());
        personaBD.setApellido(dto.getApellido());

        Persona guardada = personaRepository.save(personaBD);

        PersonaDTO response = personaMapper.toDTO(guardada);

        return response;
    }

    //Ve si la persona existe por sus atributos, si no es asi, crea una nueva persona
    public Persona obtenerPersonaOCrear(@NonNull PersonaDTO personaDto){
        Persona personaExistente = findByNombreAndApellidoAndTelefono(personaDto.getNombre(), personaDto.getApellido(), personaDto.getTelefono());

        Persona persona;
        if(personaExistente != null){
            return personaExistente;
        }else {
            PersonaDTO personaGuardada = save(personaDto);
            persona = personaMapper.toEntity(personaGuardada);
        }

        return persona;
    }

    @Override
    public PersonaDetalleDTO obtenerDetalle(Integer id) {
        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));
        int confirmaciones = historialTurno.countByPersonaIdAndEstadoTurno(id, EstadoTurno.CONFIRMADO);
        int cancelaciones = historialTurno.countByPersonaIdAndEstadoTurno(id, EstadoTurno.CANCELADO);

        PersonaDetalleDTO personaDet = new PersonaDetalleDTO();
        personaDet.setId(persona.getId());
        personaDet.setApellido(persona.getApellido());
        personaDet.setNombre(persona.getNombre());
        personaDet.setTelefono(persona.getTelefono());
        personaDet.setConfirmaciones(confirmaciones);
        personaDet.setCancelaciones(cancelaciones);

        return personaDet;
    }

}
