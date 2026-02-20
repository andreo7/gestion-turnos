package com.git.gestion_turnos.service.notificacion;

import com.git.gestion_turnos.dto.notificacion.NotificacionDTO;
import com.git.gestion_turnos.entity.Persona;
import com.git.gestion_turnos.entity.Turno;

import java.util.List;

public interface INotificacion {

    /**
     * Crea una notificación de recordatorio 24 horas antes del turno.
     *
     * @param persona persona a la cual se le enviará el recordatorio.
     * @param turno turno asociado al recordatorio.
     * @return NotificacionDTO con los datos de la notificación creada.
     */
    NotificacionDTO crearRecordatorio24h(Persona persona, Turno turno);

    /**
     * Marca una notificación como enviada.
     *
     * Este método actualiza el estado de la notificación
     * indicando que ya fue enviada al destinatario.
     *
     * @param notificacionId identificador único de la notificación.
     * @throws RuntimeException si la notificación no existe.
     */
    void marcarComoEnviada(Integer notificacionId);

    /**
     * Marca una notificación como respondida.
     *
     * Este método actualiza el estado de la notificación
     * cuando el destinatario ha respondido.
     *
     * @param notificacionId identificador único de la notificación.
     * @throws RuntimeException si la notificación no existe.
     */
    void marcarComoRespondida(Integer notificacionId);

    /**
     * Obtiene todas las notificaciones asociadas a una persona.
     *
     * @param personaId identificador único de la persona.
     * @return lista de NotificacionDTO correspondientes a la persona.
     */
    List<NotificacionDTO> findByPersona(Integer personaId);

    /**
     * Busca una notificación por su ID.
     *
     * @param id identificador único de la notificación.
     * @return NotificacionDTO con los datos de la notificación.
     * @throws RuntimeException si no existe.
     */
    NotificacionDTO findById(Integer id);
}
