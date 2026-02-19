package com.git.gestion_turnos.dto;

import com.git.gestion_turnos.enums.EstadoTurno;


//Clase usada para devolver la informacion de turnos cancelados, confirmados o ambos de una persona.
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
