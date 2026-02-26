package com.git.gestion_turnos.service.turno;

import com.git.gestion_turnos.dto.turno.TurnoDTO;

import java.util.List;

public interface ITurnoConsulta {

    /**
     * Consulta la lista de todos los turnos.
     * @return la lista de turnos.
     */
    List<TurnoDTO> findAll();

    /**
     * Busca un turno por su id.
     * @param id del turno
     * @return informacion del turno con el id pasado como parametro
     */
    TurnoDTO findById(Integer id);

    /**
     * Consulta que lista todos los turnos diponibles.
     * @return informacion de todos los turnos con estado DISPONIBLE.
     */
    List<TurnoDTO> verTurnosDisponibles();

    /**
     * Consulta que lista todos los turnos ocupados.
     * @return informacion de todos los turnos con estado RESERVADO o CONFIRMADO.
     */
    List<TurnoDTO> verTurnosOcupados();
}
