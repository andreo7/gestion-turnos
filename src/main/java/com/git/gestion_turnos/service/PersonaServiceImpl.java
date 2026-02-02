package com.git.gestion_turnos.service;

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
    public List<Persona> findAll() {
        return personaRepository.findAll();
    }

    @Override
    public Persona findById(Integer id) {
        return personaRepository.findById(id).get();
    }

    @Override
    public void deleteById(Integer id) {
        personaRepository.deleteById(id);
    }

}
