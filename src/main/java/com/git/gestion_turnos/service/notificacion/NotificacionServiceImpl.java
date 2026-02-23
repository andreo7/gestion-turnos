package com.git.gestion_turnos.service.notificacion;

import com.git.gestion_turnos.dto.notificacion.NotificacionDTO;
import com.git.gestion_turnos.entity.Notificacion;
import com.git.gestion_turnos.entity.Persona;
import com.git.gestion_turnos.entity.Turno;
import com.git.gestion_turnos.enums.TipoNotificacion;
import com.git.gestion_turnos.exception.NotificacionNotFoundException;
import com.git.gestion_turnos.mapper.NotificacionMapper;
import com.git.gestion_turnos.repository.NotificacionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificacionServiceImpl implements INotificacion {

    private NotificacionRepository notificacionRepository;
    private NotificacionMapper notificacionMapper;

    private static final Logger log = LoggerFactory.getLogger(NotificacionServiceImpl.class);

    public NotificacionServiceImpl(NotificacionRepository notificacionRepository,
                                   NotificacionMapper notificacionMapper){
        this.notificacionRepository = notificacionRepository;
        this.notificacionMapper = notificacionMapper;
    }

    @Override
    @Transactional
    public NotificacionDTO crearRecordatorio24h(Persona persona, Turno turno) {
        log.info("📝 Creando recordatorio para persona {} y turno {}",
                persona.getId(), turno.getId());

        Notificacion not = new Notificacion();
        not.setPersona(persona);
        not.setTurno(turno);
        not.setMensaje("Su turno en la fecha: " + turno.getFecha() + " es dentro de 24 horas " +
                        "\n para confimar responda SI, para cancelar responda NO.");
        not.setRespondida(false);
        not.setEnviada(false);
        not.setTipo(TipoNotificacion.RECORDATORIO);
        not.setFechaCreacion(LocalDateTime.now());
        not.setFechaRespuesta(null);
        Notificacion notguardada = notificacionRepository.save(not);

        log.info("✅ Recordatorio creado con ID: {}", notguardada.getId());

        return notificacionMapper.toDTO(notguardada);
    }

    @Override
    @Transactional
    public void marcarComoEnviada(Integer notificacionId) {
        log.info("📤 Marcando notificación {} como enviada", notificacionId);

        Notificacion notif = notificacionRepository.findById(notificacionId)
                .orElseThrow(() -> new NotificacionNotFoundException(notificacionId));
        notif.setEnviada(true);
        notificacionRepository.save(notif);

        log.info("✅ Notificación {} marcada como enviada", notificacionId);
    }

    @Override
    @Transactional
    public void marcarComoRespondida(Integer notificacionId) {
        log.info("✉️ Marcando notificación {} como respondida", notificacionId);

        Notificacion notif = notificacionRepository.findById(notificacionId)
                .orElseThrow(() -> new NotificacionNotFoundException(notificacionId));
        notif.setRespondida(true);
        notif.setFechaRespuesta(LocalDateTime.now());
        notificacionRepository.save(notif);

        log.info("✅ Notificación {} marcada como respondida", notificacionId);
    }

    @Override
    public List<NotificacionDTO> findByPersona(Integer personaId) {
        log.info("🔍 Buscando notificaciones de persona {}", personaId);

        List<NotificacionDTO> resultado = new ArrayList<>();
        List<Notificacion> lista = notificacionRepository.findByPersonaId(personaId);

        for(Notificacion n : lista){
            resultado.add(notificacionMapper.toDTO(n));
        }

        log.info("✅ Encontradas {} notificaciones", resultado.size());

        return resultado;
    }

    @Override
    public NotificacionDTO findById(Integer id) {
        log.info("🔍 Buscando notificación con ID: {}", id);

        Notificacion not = notificacionRepository.findById(id)
                .orElseThrow(() -> new NotificacionNotFoundException(id));

        log.info("✅ Notificación encontrada");

        return notificacionMapper.toDTO(not);
    }
}
