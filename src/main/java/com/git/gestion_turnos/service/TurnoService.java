package com.git.gestion_turnos.service;

import com.git.gestion_turnos.dto.TurnoDTO;
import com.git.gestion_turnos.entity.Turno;
import com.git.gestion_turnos.repository.TurnoRepository;

public class TurnoService implements ITurno{
    private TurnoRepository turnoRepository;

    public TurnoService(TurnoRepository turnoRepository){
        this.turnoRepository = turnoRepository; //Inyecto el repositorio.
    }

    public TurnoDTO crear(TurnoDTO dto){
        //Creo el pasaje DTO -> Entidad (Mapeo los datos que van a entrar a la BD)
        Turno turno = new Turno();
        turno.setEstado(dto.getEstado());
        turno.setFecha(dto.getFecha());
        turno.setHora(dto.getHora());

        //Inserto el nuevo turno en la BD
        Turno guardar = turnoRepository.save(turno);

        //Respuesta para la API Entidad -> DTO
        TurnoDTO response = new TurnoDTO();
        response.setEstado(guardar.getEstado());
        response.setFecha(guardar.getFecha());
        response.setHora(guardar.getHora());

        return response;
    }
}
