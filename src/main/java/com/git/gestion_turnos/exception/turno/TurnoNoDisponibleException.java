package com.git.gestion_turnos.exception.turno;

import com.git.gestion_turnos.exception.BaseException;
import org.springframework.http.HttpStatus;

public class TurnoNoDisponibleException extends BaseException {
    public TurnoNoDisponibleException(Integer id) {
        super(String.format("El turno con ID %d no esta disponible para reservar", id),
                "TURNO_006",
                HttpStatus.BAD_REQUEST);
    }
}
