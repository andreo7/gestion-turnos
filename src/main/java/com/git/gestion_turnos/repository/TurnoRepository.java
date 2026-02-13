package com.git.gestion_turnos.repository;

import com.git.gestion_turnos.entity.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface TurnoRepository extends JpaRepository<Turno, Integer> {
    @Query(nativeQuery = true, value =
            "select * from Turno t where YEAR(t.fecha) = :anio and MONTH(t.fecha) = :mes")
    List<Turno> existenTurnosEnMes(@Param("anio") int anio, @Param("mes") int mes);
}
