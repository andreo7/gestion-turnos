package com.git.gestion_turnos.service;

import java.util.ArrayList;
import java.util.List;

import com.git.gestion_turnos.mapper.PersonaMapper;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import com.git.gestion_turnos.dto.PersonaDTO;
import com.git.gestion_turnos.entity.Persona;
import com.git.gestion_turnos.repository.PersonaRepository;

@Service
public class PersonaServiceImpl implements IPersona{

    private PersonaRepository personaRepository;

    private PersonaMapper personaMapper;

    //Se inyecta por constructor el bean que el service necesita para funcionar.
    public PersonaServiceImpl(PersonaRepository personaRepository, PersonaMapper personaMapper){
        this.personaRepository = personaRepository;
        this.personaMapper = personaMapper;
    }

    @Override
    public PersonaDTO save(PersonaDTO dto) {
        Persona persona = personaMapper.toEntity(dto);
        Persona guardada = personaRepository.save(persona);
        PersonaDTO respuesta = personaMapper.toDTO(guardada);
        return respuesta;
    }

    @Override
    public List<PersonaDTO> findAll() {
        List<Persona> personas = personaRepository.findAll();
        List<PersonaDTO> dtos = new ArrayList<>();

        for(Persona p : personas){
            PersonaDTO pdto = personaMapper.toDTO(p);
            dtos.add(pdto);
        }

        return dtos;
    }

    @Override
    public PersonaDTO findById(Integer id) {
        Persona persona = personaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Persona no encontrada"));

        PersonaDTO personaDTO = personaMapper.toDTO(persona);
        return personaDTO;
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

}
