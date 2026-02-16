package com.git.gestion_turnos.service;

import com.git.gestion_turnos.dto.PersonaDTO;
import com.git.gestion_turnos.entity.Turno;
import com.git.gestion_turnos.dto.TurnoDTO;

import java.util.List;

public interface ITurno {
    void crearTurnosEnUnMes(int anio, int mes);
    void generarTurnosMesSiguiente();
    List<TurnoDTO> findAll();
    TurnoDTO findById(Integer id);
    TurnoDTO reservarTurno(Integer id, PersonaDTO personaDto);
    TurnoDTO cancelarTurno(Integer id);
    void confirmarTurno(Turno turno);
    List<TurnoDTO> verTurnosDisponibles();
    List<TurnoDTO> verTurnosOcupados();
}
