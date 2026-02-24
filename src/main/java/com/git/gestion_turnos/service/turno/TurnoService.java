package com.git.gestion_turnos.service.turno;

import com.git.gestion_turnos.dto.persona.PersonaDTO;
import com.git.gestion_turnos.dto.turno.TurnoDTO;
import com.git.gestion_turnos.entity.Persona;
import com.git.gestion_turnos.entity.Turno;
import com.git.gestion_turnos.enums.EstadoTurno;
import com.git.gestion_turnos.exception.turno.TurnoNotFoundException;
import com.git.gestion_turnos.mapper.TurnoMapper;
import com.git.gestion_turnos.repository.TurnoRepository;
import com.git.gestion_turnos.service.historial_turno.IHistorialTurno;
import com.git.gestion_turnos.service.persona.IPersona;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.constraints.NotNull;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class TurnoService implements ITurno {
    private final TurnoRepository turnoRepository;
    private final IHistorialTurno historialTurno;
    private final IPersona personaService;
    private final TurnoMapper turnoMapper;
    private static final LocalTime HORA_INICIO = LocalTime.of(8, 0);
    private static final LocalTime HORA_FIN = LocalTime.of(16, 0);
    private static final int DURACION_TURNO = 30;

    //Inyecto los componentes que TurnoService necesita para funcionar.
    public TurnoService(TurnoRepository turnoRepository, IPersona personaService, TurnoMapper turnoMapper, IHistorialTurno historialTurno){
        this.turnoRepository = turnoRepository;
        this.personaService = personaService;
        this.turnoMapper = turnoMapper;
        this.historialTurno = historialTurno;
    }

    public List<TurnoDTO> findAll(){
        List<Turno> turnos = turnoRepository.findAll();
        List<TurnoDTO> turnosDto = new ArrayList<>();

        for(Turno t: turnos){
            TurnoDTO turnoDto = turnoMapper.toDto(t);
            turnosDto.add(turnoDto);
        }

        return turnosDto;
    }

    public TurnoDTO findById(Integer id){
        Turno turno = obtenerTurnoPorId(id);

        return turnoMapper.toDto(turno);
    }

    //Asigna a un turno un cliente existenete. En el caso de que el cliente no exista se crea y guarda en la BD.
    @Transactional
    public TurnoDTO reservarTurno(Integer idTurno, @NonNull PersonaDTO personaDto){
        Turno turno = obtenerTurnoPorId(idTurno);

        if(turno.getEstado() != EstadoTurno.DISPONIBLE){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El turno no esta disponible para reservar");
        }

        Persona persona;
        //Veo si en el body de la request me llega el id de la persona
        if(personaDto.getId() != null){
            persona = personaService.getById(personaDto.getId());
        }else{
            persona = personaService.obtenerPersonaOCrear(personaDto);
        }

        turno.setPersona(persona);
        historialTurno.registrarCambioEstado(turno, EstadoTurno.RESERVADO);
        turno.setEstado(EstadoTurno.RESERVADO);
        turnoRepository.save(turno);

        return turnoMapper.toDto(turno);
    }

    @Transactional
    public TurnoDTO cancelarTurno(Integer id){
        Turno turno = obtenerTurnoPorId(id);

        if(turno.getEstado() != EstadoTurno.RESERVADO && turno.getEstado() != EstadoTurno.CONFIRMADO){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No es posible cancelar el turno debido a que esta DISPONIBLE");
        }
        if(turno.getPersona() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No es posible cancelar el turno debido a que no tiene un cliente al cual cancelar");
        }

        historialTurno.registrarCambioEstado(turno, EstadoTurno.CANCELADO);
        turno.setPersona(null);
        turno.setEstado(EstadoTurno.DISPONIBLE);
        turnoRepository.save(turno);

        return turnoMapper.toDto(turno);
    }

    @Transactional
    public void confirmarTurno(Turno turno){
        Turno turnoConfirmado = obtenerTurnoPorId(turno.getId());

        if(turno.getEstado() != EstadoTurno.RESERVADO){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No es posible confirmar un turno no reservado");
        }
        if(turno.getPersona() != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No es posible confirmar el turno si no tiene una persona asociada");
        }

        historialTurno.registrarCambioEstado(turno, EstadoTurno.CONFIRMADO);
        turnoConfirmado.setEstado(EstadoTurno.CONFIRMADO);
        turnoRepository.save(turnoConfirmado);
    }

    public List<TurnoDTO> verTurnosDisponibles(){
        List<Turno> turnos = turnoRepository.findAll();
        List<TurnoDTO> turnosDto = new ArrayList<>();

        for(Turno t: turnos){
            if(t.getEstado() == EstadoTurno.DISPONIBLE){
                TurnoDTO turnoDto = turnoMapper.toDto(t);
                turnosDto.add(turnoDto);
            }
        }

        return turnosDto;
    }

    public List<TurnoDTO> verTurnosOcupados(){
        List<Turno> turnos = turnoRepository.findAll();
        List<TurnoDTO> turnosDto = new ArrayList<>();

        for(Turno t: turnos){
            if(t.getEstado() == EstadoTurno.RESERVADO || t.getEstado() == EstadoTurno.CONFIRMADO){
                TurnoDTO turnoDto = turnoMapper.toDto(t);
                turnosDto.add(turnoDto);
            }
        }

        return turnosDto;
    }

    @Transactional
    public void generarTurnosMesSiguiente(){
        LocalDate hoy = LocalDate.now();
        LocalDate siguienteMes = hoy.plusMonths(1);

        if(!turnoRepository.existenTurnosEnMes(siguienteMes.getYear(), siguienteMes.getMonth().getValue()).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Los turnos para el siguiente mes ya existen");
        }

        crearTurnosEnUnMes(siguienteMes.getYear(), siguienteMes.getMonth().getValue());
    }

    public void crearTurnosEnUnMes(int anio, int mes){
        YearMonth yearMonth = YearMonth.of(anio, mes);
        LocalDate inicioMes = yearMonth.atDay(1);
        LocalDate finMes = yearMonth.atEndOfMonth();

        while(!inicioMes.isAfter(finMes)){
            crearTurnosPorDia(inicioMes);

            inicioMes = inicioMes.plusDays(1);
        }
    }

    private void crearTurnosPorDia(LocalDate fecha){
        if(!diaLaborable(fecha)){
            return;
        }

        LocalTime hora = HORA_INICIO;

        while(hora.isBefore(HORA_FIN)){
            Turno turno = new Turno();
            turno.setEstado(EstadoTurno.DISPONIBLE);
            turno.setFecha(fecha);
            turno.setHora(hora);
            turnoRepository.save(turno);

            hora = hora.plusMinutes(DURACION_TURNO);
        }
    }

    /**
     * METODOS AUXILIARES.
     */

    private boolean diaLaborable(@NonNull LocalDate fecha){
        DayOfWeek dia = fecha.getDayOfWeek();
        return dia != DayOfWeek.SATURDAY && dia != DayOfWeek.SUNDAY;
    }

    private Turno obtenerTurnoPorId(@NotNull Integer id){
        return turnoRepository.findById(id).orElseThrow(() ->
                new TurnoNotFoundException(id));
    }


}
