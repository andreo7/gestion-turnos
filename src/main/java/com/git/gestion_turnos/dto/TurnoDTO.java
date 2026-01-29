package com.git.gestion_turnos.dto;

import com.git.gestion_turnos.enums.EstadoTurno;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDate;
import java.time.LocalTime;

public class TurnoDTO {
    private LocalDate fecha;
    private LocalTime hora;
    private EstadoTurno estado;

    public TurnoDTO(){}

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public EstadoTurno getEstado() {
        return estado;
    }

    public void setEstado(EstadoTurno estado) {
        this.estado = estado;
    }
}
