package com.git.gestion_turnos.dto;

import com.git.gestion_turnos.entity.Persona;
import com.git.gestion_turnos.enums.TipoNotificacion;

import java.time.LocalDateTime;

public class NotificacionDTO {
    private Integer id;
    //El texto que vas a mostrar o mandar
    private String mensaje;
    //Cuándo se generó la notificación.
    private LocalDateTime fechaCreacion;
    private boolean enviada;
    private boolean respondida;

    private TipoNotificacion tipo;

    private Integer personaId;
    private Integer turnoId;

    private LocalDateTime fechaRespuesta;
    public NotificacionDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public boolean isEnviada() {
        return enviada;
    }

    public void setEnviada(boolean enviada) {
        this.enviada = enviada;
    }

    public TipoNotificacion getTipo() {
        return tipo;
    }

    public void setTipo(TipoNotificacion tipo) {
        this.tipo = tipo;
    }

    public Integer getPersonaId() {
        return personaId;
    }

    public void setPersonaId(int personaId) {
        this.personaId = personaId;
    }

    public Integer getTurnoId() {
        return turnoId;
    }

    public void setTurnoId(int turnoId) {
        this.turnoId = turnoId;
    }

    public boolean isRespondida() {
        return respondida;
    }

    public void setRespondida(boolean respondida) {
        this.respondida = respondida;
    }

    public LocalDateTime getFechaRespuesta() {
        return fechaRespuesta;
    }

    public void setFechaRespuesta(LocalDateTime fechaRespuesta) {
        this.fechaRespuesta = fechaRespuesta;
    }
}
