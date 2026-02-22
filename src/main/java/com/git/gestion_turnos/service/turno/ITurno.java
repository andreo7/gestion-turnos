package com.git.gestion_turnos.service.turno;

import com.git.gestion_turnos.dto.persona.PersonaDTO;
import com.git.gestion_turnos.entity.Turno;
import com.git.gestion_turnos.dto.turno.TurnoDTO;

import java.util.List;

public interface ITurno {
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

    /**
     * Consulta la lista de todos los turnos.
     * @return la lista de turnos.
     */
    List<TurnoDTO> findAll();

    /**
     * Busca un turno por su id.
     * @param id del turno
     * @return informacion del turno con el id pasado como parametro
     */
    TurnoDTO findById(Integer id);

    /**
     * CU-001: Asigna a un turno un cliente existenete.
     * En el caso de que el cliente no exista se crea y guarda en la BD.
     * @param id del turno al cual se le asignara el cliente.
     * @param personaDto datos de la persona que sera asignada al turno
     * @return informacion del turno con el cliente que lo reservo
     */
    TurnoDTO reservarTurno(Integer id, PersonaDTO personaDto);

    /**
     * CU-002: Cancela un turno con estado CONFIRMADO O RESERVADO.
     * @param id del turno a cancelar.
     * @return informacion del turno sin el cliente que habia reservado.
     */
    TurnoDTO cancelarTurno(Integer id);

    /**
     * CU-003: Confirma el turno con estado RESERVADO.
     * @param turno a confirmar.
     */
    void confirmarTurno(Turno turno);

    /**
     * Consulta que lista todos los turnos diponibles.
     * @return informacion de todos los turnos con estado DISPONIBLE.
     */
    List<TurnoDTO> verTurnosDisponibles();

    /**
     * Consulta que lista todos los turnos ocupados.
     * @return informacion de todos los turnos con estado RESERVADO o CONFIRMADO.
     */
    List<TurnoDTO> verTurnosOcupados();
}
