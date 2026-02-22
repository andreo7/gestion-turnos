package com.git.gestion_turnos.dto.historial_turno;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class HistorialTurnoMensualDTO {
    Integer total;
    Integer cancelados;
    Integer confirmados;
    double porcentajeAsistencia;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getCancelados() {
        return cancelados;
    }

    public void setCancelados(Integer cancelados) {
        this.cancelados = cancelados;
    }

    public Integer getConfirmados() {
        return confirmados;
    }

    public void setConfirmados(Integer confirmados) {
        this.confirmados = confirmados;
    }

    public double getPorcentajeAsistencia() {
        return porcentajeAsistencia;
    }

    public void setPorcentajeAsistencia(double porcentajeAsistencia) {
        this.porcentajeAsistencia = porcentajeAsistencia;
    }
}
