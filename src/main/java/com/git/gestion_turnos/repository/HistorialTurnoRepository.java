package com.git.gestion_turnos.repository;

import com.git.gestion_turnos.entity.HistorialTurno;
import com.git.gestion_turnos.enums.EstadoTurno;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistorialTurnoRepository extends JpaRepository<HistorialTurno, Integer> {
     Integer countByPersonaIdAndEstadoTurnoActual(@NotNull Integer personaId, EstadoTurno estadoTurno);
}
