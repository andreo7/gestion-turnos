package com.git.gestion_turnos.repository;

import com.git.gestion_turnos.entity.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {
    List<Notificacion> findByPersonaId(Integer personaId);
    List<Notificacion> findByEnviadaFalse();
}
