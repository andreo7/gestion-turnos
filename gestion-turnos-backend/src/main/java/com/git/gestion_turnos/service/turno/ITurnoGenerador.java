package com.git.gestion_turnos.service.turno;

public interface ITurnoGenerador {

    /**
     * Crea los turnos de todo un mes.
     * Usado para crear los turnos del primer mes de uso del sistema.
     * @param anio de creacion de los turnos
     * @param mes de creaciond de lso turnos
     */
    void crearTurnosEnUnMes(int anio, int mes);

    /**
     * Genera mediante un disparador los turnos del siguiente mes.
     * Se dispara el 15 de cada mes.
     */
    void generarTurnosMesSiguiente();
}
