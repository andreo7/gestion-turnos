package com.git.gestion_turnos.dto;

import com.git.gestion_turnos.enums.EstadoTurno;

import java.time.LocalDateTime;

public class HistorialTurnoDTO {
    private Integer id;
    private Integer personaId;
    private Integer turnoId;
    private EstadoTurno estadoTurnoAnterior; //Guarda el estado del turno previo al cambio.
    private EstadoTurno estadoTurnoActual; //Guarda el estado edl turno posterior al cambio.
    private LocalDateTime fechaActualizacion; //Fecha y hora del cambio.

    public HistorialTurnoDTO(){}

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public Integer getPersonaId() { return personaId; }

    public void setPersonaId(Integer personaId) { this.personaId = personaId; }

    public Integer getTurnoId() { return turnoId; }

    public void setTurnoId(Integer turnoId) { this.turnoId = turnoId; }

    public EstadoTurno getEstadoTurnoAnterior() { return estadoTurnoAnterior; }

    public void setEstadoTurnoAnterior(EstadoTurno estadoTurnoAnterior) { this.estadoTurnoAnterior = estadoTurnoAnterior; }

    public EstadoTurno getEstadoTurnoActual() { return estadoTurnoActual; }

    public void setEstadoTurnoActual(EstadoTurno estadoTurnoActual) { this.estadoTurnoActual = estadoTurnoActual; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}
