package com.git.gestion_turnos.service;

import com.git.gestion_turnos.dto.NotificacionDTO;
import com.git.gestion_turnos.entity.Notificacion;
import com.git.gestion_turnos.entity.Persona;
import com.git.gestion_turnos.entity.Turno;
import com.git.gestion_turnos.enums.TipoNotificacion;
import com.git.gestion_turnos.mapper.NotificacionMapper;
import com.git.gestion_turnos.repository.NotificacionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificacionServiceImpl implements INotificacion{

    private NotificacionRepository notificacionRepository;
    private NotificacionMapper notificacionMapper;

    public NotificacionServiceImpl(NotificacionRepository notificacionRepository,
                                   NotificacionMapper notificacionMapper){
        this.notificacionRepository = notificacionRepository;
        this.notificacionMapper = notificacionMapper;
    }

    @Override
    public NotificacionDTO crearRecordatorio24h(Persona persona, Turno turno) {
        Notificacion not = new Notificacion();
        not.setPersona(persona);
        not.setTurno(turno);
        not.setMensaje("Su turno en la fecha: " + turno.getFecha() + "Es dentro de 24 horas " +
                        "\n para confimar responda SI, para cancelar responda NO.");
        not.setRespondida(false);
        not.setTipo(TipoNotificacion.RECORDATORIO);
        not.setFechaCreacion(LocalDateTime.now());
        not.setFechaRespuesta(null);
        Notificacion notguardada = notificacionRepository.save(not);
        NotificacionDTO notResponse = notificacionMapper.toDTO(notguardada);

        return notResponse;
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
