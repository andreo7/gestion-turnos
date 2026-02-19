package com.git.gestion_turnos.repository;

import com.git.gestion_turnos.entity.HistorialTurno;
import com.git.gestion_turnos.enums.EstadoTurno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistorialTurnoRepository extends JpaRepository<HistorialTurno, Integer> {
    Integer countByPersonaIdAndEstadoTurno(Integer personaId, EstadoTurno estadoTurno);
}
