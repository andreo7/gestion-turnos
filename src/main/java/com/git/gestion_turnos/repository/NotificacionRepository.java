package com.git.gestion_turnos.repository;

import com.git.gestion_turnos.entity.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {
}
