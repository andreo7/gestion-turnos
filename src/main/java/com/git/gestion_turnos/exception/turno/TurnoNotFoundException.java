package com.git.gestion_turnos.exception.turno;

import com.git.gestion_turnos.exception.BaseException;
import org.springframework.http.HttpStatus;

//Excepcion lanzada al intenar acceder a un turno inexistente.
public class TurnoNotFoundException extends BaseException {
    public TurnoNotFoundException(Integer id) {
        super(String.format("Turno con ID %d no encontrado", id),
                "TURNO-001",
                HttpStatus.NOT_FOUND);
    }
}
