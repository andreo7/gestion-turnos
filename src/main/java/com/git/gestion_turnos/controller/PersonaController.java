package com.git.gestion_turnos.controller;

import java.util.List;

import com.git.gestion_turnos.dto.PersonaDetalleDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

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
    public PersonaDTO save(@Valid @RequestBody PersonaDTO persona){
        return iPersona.save(persona);
    }

    //GET
    //http:localhost:8080/personas/1
    @GetMapping("/{id}")
    public PersonaDetalleDTO findById(@PathVariable Integer id){
        return iPersona.findById(id);
    }

    //GET
    //http:localhost:8080/personas
    @GetMapping()
    public Page<PersonaDTO> findAll(@RequestParam(defaultValue = "0") int page){
        return iPersona.findAll(page, 10);
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
                             @Valid @RequestBody PersonaDTO persona){
        return iPersona.update(id,persona);
    }    
}
