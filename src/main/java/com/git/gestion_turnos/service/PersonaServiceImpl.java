package com.git.gestion_turnos.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.git.gestion_turnos.dto.PersonaDTO;
import com.git.gestion_turnos.entity.Persona;
import com.git.gestion_turnos.repository.PersonaRepository;

@Service
public class PersonaServiceImpl implements IPersona{

    private PersonaRepository personaRepository;

    //Se inyecta por constructor el bean que el service necesita para funcionar.
    public PersonaServiceImpl(PersonaRepository personaRepository){
        this.personaRepository = personaRepository;
    }

    @Override
    public PersonaDTO save(PersonaDTO dto) {
        Persona persona = new Persona();
        persona.setNombre(dto.getNombre());
        persona.setTelefono(dto.getTelefono());

        Persona guardada = personaRepository.save(persona);

        PersonaDTO respuesta = new PersonaDTO();
        respuesta.setNombre(guardada.getNombre());
        respuesta.setTelefono(guardada.getTelefono());
        
        return respuesta;
    }

    @Override
    public List<PersonaDTO> findAll() {
        List<Persona> personas = personaRepository.findAll();
        List<PersonaDTO> dtos = new ArrayList<>();

        for(Persona p : personas){
            PersonaDTO pdto = new PersonaDTO();
            pdto.setId(p.getId());
            pdto.setNombre(p.getNombre());
            pdto.setTelefono(p.getTelefono());
            dtos.add(pdto);
        }

        return dtos;
    }

    @Override
    public PersonaDTO findById(Integer id) {
        PersonaDTO personaDTO = new PersonaDTO();
       Persona persona = personaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Persona no encontrada"));
        personaDTO.setId(persona.getId());
        personaDTO.setNombre(persona.getNombre());
        personaDTO.setTelefono(persona.getTelefono());
        return personaDTO;
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

        Persona guardada = personaRepository.save(personaBD);

        PersonaDTO response = new PersonaDTO();
        response.setId(guardada.getId());
        response.setNombre(guardada.getNombre());
        response.setTelefono(guardada.getTelefono());

        return response;
    }

}
