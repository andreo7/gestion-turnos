package com.git.gestion_turnos.repository;

import com.git.gestion_turnos.entity.HistorialTurno;
import com.git.gestion_turnos.enums.EstadoTurno;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface HistorialTurnoRepository extends JpaRepository<HistorialTurno, Integer> {
     Integer countByPersonaIdAndEstadoTurnoActual(@NotNull Integer personaId, EstadoTurno estadoTurno);
     Page<HistorialTurno> findByPersonaIdAndEstadoTurnoActual(@NotNull Integer personaId, EstadoTurno estadoTurno, Pageable pageable);

     @Query("""
        SELECT COUNT(DISTINCT t.id)
        FROM HistorialTurno h
        JOIN h.turno t
        WHERE YEAR(t.fecha) = :anio AND MONTH(t.fecha) = :mes
        AND h.estadoTurnoActual = :estadoTurno
     """)
     Integer totalTurnosMensualesConEstado(@Param("estadoTurno") EstadoTurno estadoTurno,
                                      @Param("anio") int anio,
                                      @Param("mes") int mes);
}
