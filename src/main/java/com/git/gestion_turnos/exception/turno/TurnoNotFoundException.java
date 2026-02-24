package com.git.gestion_turnos.exception.turno;

import com.git.gestion_turnos.exception.BaseException;

public class TurnoNotFoundException extends BaseException {
    public TurnoNotFoundException(Integer id) {
        super(String.format("Turno con ID %d no encontrado", id), "TURNO-001");
    }
}
