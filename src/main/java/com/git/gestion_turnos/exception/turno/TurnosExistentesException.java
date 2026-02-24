package com.git.gestion_turnos.exception.turno;

import com.git.gestion_turnos.exception.BaseException;

public class TurnosExistentesException extends BaseException {
    public TurnosExistentesException(int anio, int mes) {
        super("Los turnos para el año " + mes + " / " + anio + " ya existen", "TURNO-005");
    }
}
