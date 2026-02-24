package com.git.gestion_turnos.exception.turno;

import com.git.gestion_turnos.exception.BaseException;

public class TurnoNoReservadoException extends BaseException {
    public TurnoNoReservadoException(Integer id) {
        super(String.format("No es posible confirmar el turno con ID %d debido a que no esta reservado"), "TURNO-004");
    }
}
