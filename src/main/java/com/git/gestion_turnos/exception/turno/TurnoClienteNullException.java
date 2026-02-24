package com.git.gestion_turnos.exception.turno;

import com.git.gestion_turnos.exception.BaseException;

public class TurnoClienteNullException extends BaseException {
    public TurnoClienteNullException(Integer id) {
        super(String.format("El turno con ID %d no tiene un cliente asociado"), "TURNO-003");
    }
}
