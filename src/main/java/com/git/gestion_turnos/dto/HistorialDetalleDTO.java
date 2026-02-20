package com.git.gestion_turnos.dto;

import com.git.gestion_turnos.enums.EstadoTurno;


/**
 * DTO utilizado en el endpoint:
 * GET /personas/{id}/historial
 * Representa los registros de historial asociados a una persona, tanto cancelados como confirmados.
 */
public class HistorialDetalleDTO {
    private Integer id;
    private TurnoDTO turno;
    private EstadoTurno estadoTurnoActual; //Guarda el estado del turno posterior al cambio.

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public TurnoDTO getTurno() { return turno; }

    public void setTurno(TurnoDTO turno) { this.turno = turno; }

    public EstadoTurno getEstadoTurnoActual() { return estadoTurnoActual; }

    public void setEstadoTurnoActual(EstadoTurno estadoTurnoActual) { this.estadoTurnoActual = estadoTurnoActual; }
}
