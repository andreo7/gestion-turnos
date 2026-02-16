package com.git.gestion_turnos.controller;

import com.git.gestion_turnos.dto.PersonaDTO;
import com.git.gestion_turnos.dto.TurnoDTO;
import com.git.gestion_turnos.service.ITurno;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/turnos")
public class TurnoController {
    private final ITurno iturno;

    //Se inyecta por constructor el service (Spring usa la implementacion de la interfaz como bean)
    public TurnoController(ITurno iturno){
        this.iturno = iturno;
    }

    @PostMapping("/crear") //Endpoint utilizado para generar los turnos del primer mes (solo uso de admin. El resto se genera con el disparador schedule
    public void crearTurnos(@RequestParam int anio, @RequestParam int mes){
        iturno.crearTurnosEnUnMes(anio, mes);
    }

    @GetMapping //Devuelve todos los turnos
    public List<TurnoDTO> findAll(){
        return iturno.findAll();
    }

    @GetMapping("/{id}")
    public TurnoDTO findById(@PathVariable Integer id){
        return iturno.findById(id);
    }

    @PatchMapping("/{idTurno}/reservar")
    public TurnoDTO reservarTurno(@PathVariable Integer idTurno, @RequestBody PersonaDTO personaDto){
        return iturno.reservarTurno(idTurno, personaDto);
    }

    @PatchMapping("/{id}/cancelar")
    public TurnoDTO cancelarTurno(@PathVariable Integer id){
        return iturno.cancelarTurno(id);
    }

    @GetMapping("/disponibles")
    public List<TurnoDTO> verTurnosDisponibles(){
        return iturno.verTurnosDisponibles();
    }

    @GetMapping("/ocupados")
    public List<TurnoDTO> verTurnosOcupados(){
        return iturno.verTurnosOcupados();
    }
}
