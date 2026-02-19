package com.git.gestion_turnos.service;

import com.git.gestion_turnos.entity.Turno;
import com.git.gestion_turnos.enums.EstadoTurno;

public interface IHistorialTurno {
    void registrarCambioEstado(Turno turno, EstadoTurno estadoTurno);
    Integer countByPersonaIdAndEstadoTurnoActual(Integer personaId, EstadoTurno estadoTurno);
}
