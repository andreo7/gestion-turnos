package com.git.gestion_turnos.controller;

import com.git.gestion_turnos.dto.persona.PersonaDTO;
import com.git.gestion_turnos.dto.turno.TurnoDTO;
import com.git.gestion_turnos.service.turno.ITurnoConsulta;
import com.git.gestion_turnos.service.turno.ITurnoGenerador;
import com.git.gestion_turnos.service.turno.ITurnoReserva;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/turnos")
public class TurnoController {
    private final ITurnoReserva iTurnoReserva;
    private final ITurnoConsulta iTurnoConsulta;
    private final ITurnoGenerador iTurnoGenerador;

    //Se inyecta por constructor el service (Spring usa la implementacion de la interfaz como bean)
    public TurnoController(ITurnoReserva iTurnoReserva, ITurnoConsulta iTurnoConsulta, ITurnoGenerador iTurnoGenerador){
        this.iTurnoReserva = iTurnoReserva;
        this.iTurnoConsulta = iTurnoConsulta;
        this.iTurnoGenerador = iTurnoGenerador;
    }

    @PostMapping("/crear") //Endpoint utilizado para generar los turnos del primer mes (solo uso de admin. El resto se genera con el disparador schedule
    public void crearTurnos(@RequestParam int anio, @RequestParam int mes){
        iTurnoGenerador.crearTurnosEnUnMes(anio, mes);
    }

    @GetMapping //Devuelve todos los turnos
    public List<TurnoDTO> findAll(){
        return iTurnoConsulta.findAll();
    }

    @GetMapping("/{id}")
    public TurnoDTO findById(@PathVariable Integer id){
        return iTurnoConsulta.findById(id);
    }

    @PatchMapping("/{idTurno}/reservar")
    public TurnoDTO reservarTurno(@PathVariable Integer idTurno, @RequestBody PersonaDTO personaDto){
        return iTurnoReserva.reservarTurno(idTurno, personaDto);
    }

    @PatchMapping("/{id}/cancelar")
    public TurnoDTO cancelarTurno(@PathVariable Integer id){
        return iTurnoReserva.cancelarTurno(id);
    }

    @GetMapping("/disponibles")
    public List<TurnoDTO> verTurnosDisponibles(){
        return iTurnoConsulta.verTurnosDisponibles();
    }

    @GetMapping("/ocupados")
    public List<TurnoDTO> verTurnosOcupados(){
        return iTurnoConsulta.verTurnosOcupados();
    }
}
