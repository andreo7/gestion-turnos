package com.git.gestion_turnos.entity;

import com.git.gestion_turnos.enums.EstadoTurno;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Turno {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDate fecha;
    private LocalTime hora;
    @Enumerated(EnumType.STRING)
    private EstadoTurno estado;

    public Turno(){}

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public LocalDate getFecha() { return fecha; }

    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHora() { return hora; }

    public void setHora(LocalTime hora) { this.hora = hora; }

    public EstadoTurno getEstado() { return estado; }

    public void setEstado(EstadoTurno estado) { this.estado = estado; }
}

