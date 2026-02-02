package com.git.gestion_turnos.controller;

import com.git.gestion_turnos.dto.TurnoDTO;
import com.git.gestion_turnos.service.ITurno;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/turnos")
public class TurnoController {
    private ITurno iturno;

    //Se inyecta por constructor el service (Spring usa la implementacion de la interfaz como bean)
    public TurnoController(ITurno iturno){
        this.iturno = iturno;
    }

    @PostMapping //Endpoint utilizado para generar los turnos del primer mes. El resto se genera con el disparador shedule
    public void crearTurnos(@RequestParam int anio, @RequestParam int mes){
        iturno.crearTurnosEnUnMes(anio, mes);
    }
}
