package com.git.gestion_turnos.repository;

import com.git.gestion_turnos.entity.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {

    List<Notificacion> findByPersonaId(Integer personaId);

    @Query("""
        SELECT n FROM Notificacion n
        JOIN FETCH n.turno t
        JOIN FETCH n.persona p
        WHERE n.enviada = false
        ORDER BY t.fecha ASC, t.hora ASC
    """)
    List<Notificacion> findPendientesConTurnoYPersona();

    /**
     * Elimina notificaciones antiguas enviadas.
     *
     * @param fechaLimite Eliminar anteriores a esta fecha
     * @return Cantidad de registros eliminados
     */
    @Modifying
    @Query("""
        DELETE FROM Notificacion n 
        WHERE n.enviada = true 
        AND n.fechaCreacion < :fechaLimite
    """)
    int deleteByEnviadaTrueAndFechaCreacionBefore(@Param("fechaLimite") LocalDateTime fechaLimite);
}
