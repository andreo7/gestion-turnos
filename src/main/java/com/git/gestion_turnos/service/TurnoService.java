package com.git.gestion_turnos.service;

import com.git.gestion_turnos.dto.PersonaDTO;
import com.git.gestion_turnos.dto.TurnoDTO;
import com.git.gestion_turnos.entity.Persona;
import com.git.gestion_turnos.entity.Turno;
import com.git.gestion_turnos.enums.EstadoTurno;
import com.git.gestion_turnos.mapper.TurnoMapper;
import com.git.gestion_turnos.repository.TurnoRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class TurnoService implements ITurno{
    private final TurnoRepository turnoRepository;
    private final IPersona personaService;
    private final TurnoMapper turnoMapper;
    private static final LocalTime HORA_INICIO = LocalTime.of(8, 0);
    private static final LocalTime HORA_FIN = LocalTime.of(16, 0);
    private static final int DURACION_TURNO = 30;

    //Inyecto los componentes que TurnoService necesita para funcionar.
    public TurnoService(TurnoRepository turnoRepository, IPersona personaService, TurnoMapper turnoMapper){
        this.turnoRepository = turnoRepository;
        this.personaService = personaService;
        this.turnoMapper = turnoMapper;
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
        turno.setEstado(EstadoTurno.RESERVADO);
        turnoRepository.save(turno);

        return turnoMapper.toDto(turno);
    }

    public TurnoDTO cancelarTurno(Integer id){
        Turno turno = obtenerTurnoPorId(id);

        turno.setPersona(null);
        turno.setEstado(EstadoTurno.DISPONIBLE);
        turnoRepository.save(turno);

        return turnoMapper.toDto(turno);
    }

    public void confirmarTurno(Turno turno){
        turno = turnoRepository.findById(turno.getId()).orElseThrow(() -> new RuntimeException("El turno no existe"));

        turno.setEstado(EstadoTurno.CONFIRMADO);
        turnoRepository.save(turno);
    }

    //Genera los turnos del mes siguiente al actual solo si no existen turnos ya creados.
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

    private boolean diaLaborable(@NonNull LocalDate fecha){
        DayOfWeek dia = fecha.getDayOfWeek();
        return dia != DayOfWeek.SATURDAY && dia != DayOfWeek.SUNDAY;
    }

    private Turno obtenerTurnoPorId(@NotNull Integer id){
        return turnoRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "El turno no fue encontrado"));
    }


}
