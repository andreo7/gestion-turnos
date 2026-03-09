package com.git.gestion_turnos.service.turno;

import com.git.gestion_turnos.dto.persona.PersonaDTO;
import com.git.gestion_turnos.dto.turno.TurnoDTO;

public interface ITurnoReserva {

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
     * @param id del turno a confirmar.
     */
    void confirmarTurno(Integer id);
}
