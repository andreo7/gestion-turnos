package com.git.gestion_turnos.exception.notificacion;

import com.git.gestion_turnos.exception.BaseException;

public class NotificacionNotFoundException extends BaseException {
    public NotificacionNotFoundException(Integer id) {
        super(
                String.format("Notificacion coin ID %d no encontrada", id), "NOTIF_001"
        );
    }
}
