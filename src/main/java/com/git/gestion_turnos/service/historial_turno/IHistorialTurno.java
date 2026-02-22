package com.git.gestion_turnos.service.historial_turno;

import com.git.gestion_turnos.dto.historial_turno.HistorialDetalleDTO;
import com.git.gestion_turnos.entity.Turno;
import com.git.gestion_turnos.enums.EstadoTurno;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IHistorialTurno {
    /**
     * Registra los cambios de estado de un turno.
     * @param turno que cambio de estado.
     * @param estadoTurno nuevo estado del turno.
     */
    void registrarCambioEstado(Turno turno, EstadoTurno estadoTurno);

    /**
     * Cuenta la cantidad de turnos con estadoTurno de una persona.
     * @param personaId persona a la cual se le cuentan los turnos con estadoTurno.
     * @param estadoTurno estado del turno que se quiere consultar
     * @return numero de turnos de una persona con estadoTurno = ?.
     */
    Integer contarTurnosPorPersonaYEstado(Integer personaId, EstadoTurno estadoTurno);

    /**
     * Lista los turnos históricos de una persona, permitiendo filtrar por estado.
     * @param personaId id de la persona.
     * @param estadoTurno estado por el cual se desea filtrar (opcional).
     * @param pageable parámetros de paginación y ordenamiento.
     * @return página de resultados con el detalle del historial.
     */
    Page<HistorialDetalleDTO> listarHistorialDePersona(@NotNull Integer personaId, EstadoTurno estadoTurno, Pageable pageable);
}
