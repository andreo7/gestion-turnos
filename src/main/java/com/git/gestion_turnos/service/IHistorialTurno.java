package com.git.gestion_turnos.service;

import com.git.gestion_turnos.dto.HistorialDetalleDTO;
import com.git.gestion_turnos.entity.HistorialTurno;
import com.git.gestion_turnos.entity.Turno;
import com.git.gestion_turnos.enums.EstadoTurno;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IHistorialTurno {
    void registrarCambioEstado(Turno turno, EstadoTurno estadoTurno);
    Integer countByPersonaIdAndEstadoTurnoActual(Integer personaId, EstadoTurno estadoTurno);
    Page<HistorialDetalleDTO> listarHistorialDePersona(@NotNull Integer personaId, EstadoTurno estadoTurno, Pageable pageable);
}
