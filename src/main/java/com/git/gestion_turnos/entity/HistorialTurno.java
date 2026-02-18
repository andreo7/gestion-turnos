package com.git.gestion_turnos.entity;

import com.git.gestion_turnos.enums.EstadoTurno;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class HistorialTurno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "persona_id")
    private Persona persona;

    @ManyToOne
    @JoinColumn(name = "turno_id")
    private Turno turno;

    @Enumerated(EnumType.STRING)
    private EstadoTurno estadoTurnoAnterior;
    @Enumerated(EnumType.STRING)
    private EstadoTurno estadoTurnoActual;

    private LocalDateTime fechaHoraActualizacion;

    public HistorialTurno(){}

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public Persona getPersona() { return persona; }

    public void setPersona(Persona persona) { this.persona = persona; }

    public Turno getTurno() { return turno; }

    public void setTurno(Turno turno) { this.turno = turno; }

    public EstadoTurno getEstadoTurnoAnterior() { return estadoTurnoAnterior; }

    public void setEstadoTurnoAnterior(EstadoTurno estadoTurnoAnterior) { this.estadoTurnoAnterior = estadoTurnoAnterior; }

    public EstadoTurno getEstadoTurnoActual() { return estadoTurnoActual; }

    public void setEstadoTurnoActual(EstadoTurno estadoTurnoActual) { this.estadoTurnoActual = estadoTurnoActual; }

    public LocalDateTime getFechaHoraActualizacion() { return fechaHoraActualizacion; }

    public void setFechaHoraActualizacion(LocalDateTime fechaHoraActualizacion) { this.fechaHoraActualizacion = fechaHoraActualizacion; }
}
