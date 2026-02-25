package com.git.gestion_turnos.exception.turno;

import com.git.gestion_turnos.exception.BaseException;
import org.springframework.http.HttpStatus;

//Excepcion que se lanza al intentar cancelar o confirmar un turno sin cliente asociado.
public class TurnoClienteNullException extends BaseException {
    public TurnoClienteNullException(Integer id) {
        super(String.format("El turno con ID %d no tiene un cliente asociado"),
                "TURNO_003",
                HttpStatus.BAD_REQUEST);
    }
}
