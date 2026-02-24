package com.git.gestion_turnos.service.persona;

import com.git.gestion_turnos.dto.historial_turno.HistorialDetalleDTO;
import com.git.gestion_turnos.dto.persona.PersonaDetalleDTO;
import com.git.gestion_turnos.enums.EstadoTurno;
import com.git.gestion_turnos.exception.PersonaNotFoundException;
import com.git.gestion_turnos.mapper.PersonaMapper;
import com.git.gestion_turnos.service.historial_turno.IHistorialTurno;
import jakarta.validation.constraints.NotNull;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.git.gestion_turnos.dto.persona.PersonaDTO;
import com.git.gestion_turnos.entity.Persona;
import com.git.gestion_turnos.repository.PersonaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class PersonaServiceImpl implements IPersona {

    private final PersonaRepository personaRepository;

    private final PersonaMapper personaMapper;

    private final IHistorialTurno historialTurno;

    private static final Logger log = LoggerFactory.getLogger(PersonaServiceImpl.class);

    //Se inyecta por constructor el bean que el service necesita para funcionar.
    public PersonaServiceImpl(PersonaRepository personaRepository,
                              PersonaMapper personaMapper,
                              IHistorialTurno historialTurno){
        this.personaRepository = personaRepository;
        this.personaMapper = personaMapper;
        this.historialTurno = historialTurno;
    }

    @Override
    public PersonaDTO save(PersonaDTO dto) {
        log.info("📝 Creando nueva persona");

        Persona persona = personaMapper.toEntity(dto);
        Persona guardada = personaRepository.save(persona);

        log.info("✅ Persona creada con ID: {}", guardada.getId());

        PersonaDTO respuesta = personaMapper.toDTO(guardada);
        return respuesta;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PersonaDTO> findAll(int page, int size) {
        log.info("🔍 Buscando personas");

        Pageable pageable = PageRequest.of(page,size);
        Page<Persona> pagePersona = personaRepository.findAll(pageable);
        //Este metodo sirve para mapear todas las personas de pagePersona a personaDTO creando asi
        //una page<PersonaDTO> nueva que es devuelta al instante
        log.info("✅ Busqueda exitosa, {} personas encontradas", pagePersona.getNumberOfElements());

        return pagePersona.map(personaMapper ::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public PersonaDetalleDTO findById(Integer id) {
        log.info("🔍 Buscando detalle de la persona con ID: {}", id);

        Persona persona = personaRepository.findById(id)
            .orElseThrow(() -> new PersonaNotFoundException(id));

        log.info("✅ Persona encontrada");

        return  obtenerDetalle(persona.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HistorialDetalleDTO> listarHistorialDePersona(@NotNull Integer personaId, EstadoTurno estadoTurno, Pageable pageable){
        log.info("🔍 Buscando historial de la persona con ID: {}", personaId);
        personaRepository.findById(personaId).orElseThrow(() -> new PersonaNotFoundException(personaId));

        log.info("✅ Historial de persona encontrado");
        return historialTurno.listarHistorialDePersona(personaId, estadoTurno, pageable);
    }

    @Override
    public void deleteById(Integer id) {
        log.info("🔍 Buscando persona a eliminar con ID: {}", id);

        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new PersonaNotFoundException(id));

        log.info("✅ Eliminando persona con id {}", id);

        personaRepository.delete(persona);
    }

    @Override
    public PersonaDTO update(Integer id, PersonaDTO dto) {
        log.info("🔍 Buscando persona a actualizar con ID: {}", id);

        Persona personaBD = personaRepository.findById(id)
            .orElseThrow(() -> new PersonaNotFoundException(id));

        personaBD.setNombre(dto.getNombre());
        personaBD.setTelefono(dto.getTelefono());
        personaBD.setApellido(dto.getApellido());

        Persona guardada = personaRepository.save(personaBD);

        log.info("✅ Persona actualizada con id {}", guardada.getId());

        PersonaDTO response = personaMapper.toDTO(guardada);
        return response;
    }

    //Este metodo es un metodo auxiliar parar obtenerPersonaOCrear
    //Puede devolver null o la persona existente
    @Override
    @Transactional(readOnly = true)
    public Persona findByNombreAndApellidoAndTelefono(String nombre,String apellido, String telefono){
        return personaRepository.findByNombreAndApellidoAndTelefono(nombre, apellido, telefono);
    }

    @Override
    @Transactional(readOnly = true)
    public Persona getById(Integer id){
        log.info("🔍 Buscando la persona con ID: {}", id);

        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new PersonaNotFoundException(id));

        log.info("✅ Persona encontrada");

        return persona;
    }

    //Ve si la persona existe por sus atributos, si no es asi, crea una nueva persona
    @Override
    public Persona obtenerPersonaOCrear(@NonNull PersonaDTO personaDto){
        log.info("🔍 Analizando si Obtener o Crear persona");
        Persona personaExistente = findByNombreAndApellidoAndTelefono(personaDto.getNombre(), personaDto.getApellido(), personaDto.getTelefono());

        Persona persona;
        if(personaExistente != null){
            log.info("✅ Persona encontrada");
            return personaExistente;
        }else {
            PersonaDTO personaGuardada = save(personaDto);
            persona = personaMapper.toEntity(personaGuardada);
        }
        log.info("✅ Persona Creada");
        return persona;
    }



    //Metodo que busca todos los detalles de una persona en relacion con los turnos realizados
    //es un metodo auxiliar que usamos en findById por si en algun momento queremos agregar algo
    //al detalle desacoplamos de ese metodo.
    @Override
    @Transactional(readOnly = true)
    public PersonaDetalleDTO obtenerDetalle(Integer id) {
        log.info("📝 Obteniendo detalle de la persona con ID: {}", id);
        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new PersonaNotFoundException(id));
        int confirmaciones = historialTurno.contarTurnosPorPersonaYEstado(id, EstadoTurno.CONFIRMADO);
        int cancelaciones = historialTurno.contarTurnosPorPersonaYEstado(id, EstadoTurno.CANCELADO);

        PersonaDetalleDTO personaDet = new PersonaDetalleDTO();
        personaDet.setId(persona.getId());
        personaDet.setApellido(persona.getApellido());
        personaDet.setNombre(persona.getNombre());
        personaDet.setTelefono(persona.getTelefono());
        personaDet.setConfirmaciones(confirmaciones);
        personaDet.setCancelaciones(cancelaciones);

        log.info("✅ Detalle obtenido");
        return personaDet;
    }

}
