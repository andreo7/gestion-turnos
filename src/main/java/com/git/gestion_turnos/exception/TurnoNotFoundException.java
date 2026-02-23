package com.git.gestion_turnos.exception;

public class TurnoNotFoundException extends BaseException {
    public TurnoNotFoundException(Integer id) {
        super(String.format("Turno con ID %d no encontrado", id), "TURNO-001");
    }
}
