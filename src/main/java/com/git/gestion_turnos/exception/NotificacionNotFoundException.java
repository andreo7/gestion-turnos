package com.git.gestion_turnos.exception;

public class NotificacionNotFoundException extends BaseException {
    public NotificacionNotFoundException(Integer id) {
        super(
                String.format("Notificacion coin ID %d no encontrada", id), "NOTIF_001"
        );
    }
}
