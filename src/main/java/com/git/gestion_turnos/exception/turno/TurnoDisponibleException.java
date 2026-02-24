package com.git.gestion_turnos.exception.turno;

import com.git.gestion_turnos.exception.BaseException;

//Excepcion que se lanza al intentar cancelar un turno que esta disponible.
public class TurnoDisponibleException extends BaseException {
    public TurnoDisponibleException(Integer id) {
        super(String.format("No es posible cancelar el turno con ID %d ya que esta DISPONIBLE"), "TURNO-002");
    }
}
