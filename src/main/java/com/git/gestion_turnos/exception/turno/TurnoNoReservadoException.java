package com.git.gestion_turnos.exception.turno;

import com.git.gestion_turnos.exception.BaseException;
import org.springframework.http.HttpStatus;

//Excepcion que se lanza al intentar confirmar un turno que no esta reservado.
public class TurnoNoReservadoException extends BaseException {
    public TurnoNoReservadoException(Integer id) {
        super(String.format("No es posible confirmar el turno con ID %d debido a que no esta reservado"),
                "TURNO-004",
                HttpStatus.BAD_REQUEST);
    }
}
