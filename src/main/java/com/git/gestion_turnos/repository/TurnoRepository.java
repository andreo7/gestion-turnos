package com.git.gestion_turnos.repository;

import com.git.gestion_turnos.entity.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface TurnoRepository extends JpaRepository<Turno, Integer> {

}
