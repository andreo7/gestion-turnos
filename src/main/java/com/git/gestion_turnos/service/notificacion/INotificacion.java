package com.git.gestion_turnos.service.notificacion;

import com.git.gestion_turnos.dto.notificacion.NotificacionDTO;
import com.git.gestion_turnos.entity.Persona;
import com.git.gestion_turnos.entity.Turno;

import java.util.List;

public interface INotificacion {


    NotificacionDTO crearRecordatorio24h(Persona persona, Turno turno);

    void marcarComoEnviada(Integer notificacionId);

    void marcarComoRespondida(Integer notificacionId);

    List<NotificacionDTO> findByPersona(Integer personaId);

    NotificacionDTO findById(Integer id);
}
