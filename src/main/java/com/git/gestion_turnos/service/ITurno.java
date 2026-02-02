package com.git.gestion_turnos.service;

import com.git.gestion_turnos.entity.Turno;
import com.git.gestion_turnos.dto.TurnoDTO;

public interface ITurno {
    void crearTurnosEnUnMes(int anio, int mes);
}
