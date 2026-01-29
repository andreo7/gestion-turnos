package com.git.gestion_turnos.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.git.gestion_turnos.dto.PersonaDTO;
import com.git.gestion_turnos.service.IPersona;

@RestController
public class PersonaController {

    private IPersona iPersona;

    public PersonaController(IPersona iPersona){
        this.iPersona = iPersona;
    }

    @PostMapping
    public PersonaDTO save(@RequestBody PersonaDTO persona){
        return iPersona.save(persona);
    }
}
