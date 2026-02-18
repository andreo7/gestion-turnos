package com.git.gestion_turnos.mapper;

import com.git.gestion_turnos.dto.NotificacionDTO;
import com.git.gestion_turnos.entity.Notificacion;
import com.git.gestion_turnos.entity.Persona;
import com.git.gestion_turnos.entity.Turno;
import org.springframework.stereotype.Component;

@Component
public class NotificacionMapper {

    public NotificacionDTO toDTO(Notificacion notificacion){
        NotificacionDTO notificacionDTO = new NotificacionDTO();
        notificacionDTO.setId(notificacion.getId());
        notificacionDTO.setFechaCreacion(notificacion.getFechaCreacion());
        notificacionDTO.setEnviada(notificacion.isEnviada());
        notificacionDTO.setRespondida(notificacion.isRespondida());
        notificacionDTO.setTipo(notificacion.getTipo());
        notificacionDTO.setFechaRespuesta(notificacion.getFechaRespuesta());
        notificacionDTO.setMensaje(notificacion.getMensaje());

        if(notificacion.getPersona() != null){
            notificacionDTO.setPersonaId(notificacion.getPersona().getId());
        }

        if(notificacion.getTurno() != null){
            notificacionDTO.setTurnoId(notificacion.getTurno().getId());
        }

        return notificacionDTO;
    }

    public Notificacion toEntity(NotificacionDTO notificacionDTO){
        Notificacion notificacion = new Notificacion();
        notificacion.setId(notificacionDTO.getId());
        notificacion.setFechaCreacion(notificacionDTO.getFechaCreacion());
        notificacion.setEnviada(notificacionDTO.isEnviada());
        notificacion.setRespondida(notificacionDTO.isRespondida());
        notificacion.setTipo(notificacionDTO.getTipo());
        notificacion.setFechaRespuesta(notificacionDTO.getFechaRespuesta());
        notificacion.setMensaje(notificacionDTO.getMensaje());

        if(notificacionDTO.getPersonaId() != null){
            Persona p = new Persona();
            p.setId(notificacionDTO.getPersonaId());
            notificacion.setPersona(p);
        }

        if(notificacionDTO.getTurnoId() != null){
            Turno t = new Turno();
            t.setId(notificacionDTO.getTurnoId());
            notificacion.setTurno(t);
        }

        return notificacion;
    }
}
