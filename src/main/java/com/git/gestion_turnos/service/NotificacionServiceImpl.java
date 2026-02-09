package com.git.gestion_turnos.service;

import com.git.gestion_turnos.dto.NotificacionDTO;
import com.git.gestion_turnos.entity.Notificacion;
import com.git.gestion_turnos.entity.Persona;
import com.git.gestion_turnos.entity.Turno;
import com.git.gestion_turnos.enums.TipoNotificacion;
import com.git.gestion_turnos.repository.NotificacionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificacionServiceImpl implements INotificacion{

    private NotificacionRepository notificacionRepository;

    public NotificacionServiceImpl(NotificacionRepository notificacionRepository){
        this.notificacionRepository = notificacionRepository;
    }



    @Override
    public NotificacionDTO crearNotificacionReserva(Persona persona, Turno turno) {
        Notificacion notif = new Notificacion();
        notif.setPersona(persona);
        notif.setTurno(turno);
        notif.setEnviada(false);
        notif.setRespondida(false);
        notif.setFechaCreacion(LocalDateTime.now());
        notif.setTipo(TipoNotificacion.TURNO_CREADO);
        notif.setMensaje("Turno reservado para el" + turno.getFecha());
        return null;
    }

    @Override
    public NotificacionDTO crearRecordatorio24h(Persona persona, Turno turno) {
        return null;
    }

    @Override
    public void marcarComoEnviada(Long notificacionId) {

    }

    @Override
    public void marcarComoRespondida(Long notificacionId) {

    }

    @Override
    public List<NotificacionDTO> findByPersona(Long personaId) {
        return List.of();
    }

    @Override
    public NotificacionDTO findById(Long id) {
        return null;
    }
}
