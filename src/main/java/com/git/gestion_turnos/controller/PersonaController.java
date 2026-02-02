package com.git.gestion_turnos.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.git.gestion_turnos.dto.PersonaDTO;
import com.git.gestion_turnos.service.IPersona;

@RestController
@RequestMapping("/personas")
public class PersonaController {

    private IPersona iPersona;

    public PersonaController(IPersona iPersona){
        this.iPersona = iPersona;
    }

    //POST
    //http:localhost:8080/personas
    @PostMapping
    public PersonaDTO save(@RequestBody PersonaDTO persona){
        return iPersona.save(persona);
    }

    //GET
    //http:localhost:8080/personas/1
    @GetMapping("/{id}")
    public PersonaDTO findById(@PathVariable Integer id){
        return iPersona.findById(id);
    }

    //GET
    //http:localhost:8080/personas
    @GetMapping()
    public List<PersonaDTO> findAll(){
        return iPersona.findAll();
    }

    //DELETE
    //htpp://localhost:8080/personas/1
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Integer id){
        iPersona.deleteById(id);
    }

    //PUT
    //htpp://localhost:8080/personas
    @PutMapping("/{id}")
    public PersonaDTO update(@PathVariable Integer id,
                             @RequestBody PersonaDTO persona){
        return iPersona.update(id,persona);
    }    
}
