package com.git.gestion_turnos.service.persona;

import com.git.gestion_turnos.dto.historial_turno.HistorialDetalleDTO;
import com.git.gestion_turnos.dto.persona.PersonaDTO;
import com.git.gestion_turnos.dto.persona.PersonaDetalleDTO;
import com.git.gestion_turnos.entity.Persona;
import com.git.gestion_turnos.enums.EstadoTurno;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IPersona {

    /**
     * Guarda una nueva persona en el sistema.
     *
     * @param personaDTO datos de la persona a persistir.
     * @return PersonaDTO con los datos guardados (incluyendo el ID generado).
     */
    PersonaDTO save(PersonaDTO personaDTO);

    /**
     * Obtiene una lista paginada de personas.
     *
     * @param page número de página (comienza en 0).
     * @param size cantidad de elementos por página.
     * @return página de PersonaDTO.
     */
    Page<PersonaDTO> findAll(int page, int size);

    /**
     * Busca una persona por su ID y retorna su información detallada.
     *
     * @param id identificador único de la persona.
     * @return PersonaDetalleDTO con información ampliada.
     * @throws RuntimeException si la persona no existe.
     */
    PersonaDetalleDTO findById(Integer id);

    /**
     * Elimina una persona del sistema según su ID.
     *
     * @param id identificador único de la persona.
     * @throws RuntimeException si la persona no existe.
     */
    void deleteById(Integer id);

    /**
     * Actualiza los datos de una persona existente.
     *
     * @param id identificador de la persona a actualizar.
     * @param dto nuevos datos de la persona.
     * @return PersonaDTO con los datos actualizados.
     * @throws RuntimeException si la persona no existe.
     */
    PersonaDTO update(Integer id, PersonaDTO dto);

    /**
     * Busca una persona por nombre, apellido y teléfono.
     *
     * @param nombre nombre de la persona.
     * @param apellido apellido de la persona.
     * @param telefono teléfono de la persona.
     * @return Persona si existe, o null si no se encuentra.
     */
    Persona findByNombreAndApellidoAndTelefono(String nombre, String apellido, String telefono);

    /**
     * Obtiene una entidad Persona por su ID.
     *
     * A diferencia de findById, este método retorna directamente la entidad
     * y no un DTO.
     *
     * @param id identificador único.
     * @return entidad Persona.
     * @throws RuntimeException si no existe.
     */
    Persona getById(Integer id);

    /**
     * Obtiene una persona existente según los datos proporcionados,
     * o la crea en caso de no existir.
     *
     * Este método es útil para evitar duplicados.
     *
     * @param personaDto datos de la persona.
     * @return entidad Persona existente o recién creada.
     */
    Persona obtenerPersonaOCrear(PersonaDTO personaDto);

    /**
     * Obtiene el detalle completo de una persona. Este es un metodo auxiliar,
     * que usamos en findById para desacoplar metodos
     *
     *
     * @param id identificador de la persona.
     * @return PersonaDetalleDTO con información extendida.
     */
    PersonaDetalleDTO obtenerDetalle(Integer id);

    /**
     * Lista el historial de turnos de una persona,
     * permitiendo filtrar por estado y paginar resultados.
     *
     * @param personaId ID de la persona.
     * @param estadoTurno estado por el cual filtrar (puede ser null).
     * @param pageable configuración de paginación.
     * @return página de HistorialDetalleDTO.
     */
    Page<HistorialDetalleDTO> listarHistorialDePersona(Integer personaId, EstadoTurno estadoTurno, Pageable pageable);
}
