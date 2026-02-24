package com.git.gestion_turnos.exception.turno;

import com.git.gestion_turnos.exception.BaseException;
import org.springframework.http.HttpStatus;

public class TurnosDuplicadosException extends BaseException {
    public TurnosDuplicadosException(int anio, int mes) {
        super("Los turnos para el año " + mes + " / " + anio + " ya existen",
                "TURNO-005",
                HttpStatus.CONFLICT);
    }
}
