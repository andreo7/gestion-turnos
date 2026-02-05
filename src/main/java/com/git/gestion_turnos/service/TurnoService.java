package com.git.gestion_turnos.service;

import com.git.gestion_turnos.dto.PersonaDTO;
import com.git.gestion_turnos.dto.TurnoDTO;
import com.git.gestion_turnos.entity.Persona;
import com.git.gestion_turnos.entity.Turno;
import com.git.gestion_turnos.enums.EstadoTurno;
import com.git.gestion_turnos.repository.TurnoRepository;
import jakarta.transaction.Transactional;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class TurnoService implements ITurno{
    private final TurnoRepository turnoRepository;
    private final PersonaServiceImpl personaService;
    private static final LocalTime HORA_INICIO = LocalTime.of(8, 0);
    private static final LocalTime HORA_FIN = LocalTime.of(16, 0);
    private static final int DURACION_TURNO = 30;


    public TurnoService(TurnoRepository turnoRepository, PersonaServiceImpl personaService){
        this.turnoRepository = turnoRepository; //Inyecto el repositorio.
        this.personaService = personaService;
    }

    public List<TurnoDTO> findAll(){
        List<Turno> turnos = turnoRepository.findAll();
        List<TurnoDTO> turnosDto = new ArrayList<>();

        for(Turno t: turnos){
            TurnoDTO turnoDto = new TurnoDTO();
            turnoDto.setId(t.getId());
            turnoDto.setHora(t.getHora());
            turnoDto.setFecha(t.getFecha());
            turnoDto.setEstado(t.getEstado());
            Persona persona = t.getPersona();
            PersonaDTO personaDto = new PersonaDTO();
            personaDto.setNombre(persona.getNombre());
            personaDto.setTelefono(persona.getTelefono());
            personaDto.setId(persona.getId());

            turnoDto.setCliente(personaDto);

            turnosDto.add(turnoDto);
        }

        return turnosDto;
    }

    public TurnoDTO findById(Integer id){
        Turno turno = turnoRepository.findById(id).orElseThrow(() -> new RuntimeException("Turno no encontrado"));

        TurnoDTO turnoDto = new TurnoDTO();
        turnoDto.setId(turno.getId());
        turnoDto.setEstado(turno.getEstado());
        turnoDto.setFecha(turno.getFecha());
        turnoDto.setHora(turno.getHora());

        Persona persona = turno.getPersona();
        PersonaDTO personaDto = new PersonaDTO();
        personaDto.setId(persona.getId());
        personaDto.setTelefono(persona.getTelefono());
        personaDto.setNombre(persona.getNombre());

        turnoDto.setCliente(personaDto);

        return turnoDto;
    }

    //Asigna a un turno un cliente existenete. En el caso de que el cliente no exista se crea y guarda en la BD.
    @Transactional
    public TurnoDTO asignarCliente(Integer idTurno, PersonaDTO personaDto){
        Turno turno = turnoRepository.findById(idTurno).orElseThrow(() -> new RuntimeException("Turno no encontrado"));


        Persona persona = new Persona();
        //Veo si en el body de la request me llega el id de la persona
        if(personaDto.getId() != null){
            persona = personaService.getById(personaDto.getId());
        }else {
            Persona personaExistente = personaService.findByNombreAndTelefono(personaDto.getNombre(), personaDto.getTelefono());
            if(personaExistente != null){
                persona = personaExistente;
            }else{
                persona.setNombre(personaDto.getNombre());
                persona.setTelefono(personaDto.getTelefono());
                personaService.save(personaDto);
            }
        }

        turno.setPersona(persona);
        turno.setEstado(EstadoTurno.RESERVADO);
        turnoRepository.save(turno);

        TurnoDTO turnoDto = new TurnoDTO();
        turnoDto.setId(turno.getId());
        turnoDto.setEstado(turno.getEstado());
        turnoDto.setFecha(turno.getFecha());
        turnoDto.setHora(turno.getHora());
        turnoDto.setCliente(personaDto);

        return turnoDto;
    }

    //Genera los turnos del mes siguiente al actual solo si no existen turnos ya creados.
    @Transactional
    public void generarTurnosMesSiguiente(){
        LocalDate hoy = LocalDate.now();
        LocalDate siguienteMes = hoy.plusMonths(1);

        if(turnoRepository.existenTurnosEnMes(siguienteMes.getYear(), siguienteMes.getMonth().getValue()).isEmpty()) {
            crearTurnosEnUnMes(siguienteMes.getYear(), siguienteMes.getMonth().getValue());
        }
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

    private boolean diaLaborable(LocalDate fecha){
        DayOfWeek dia = fecha.getDayOfWeek();
        return dia != DayOfWeek.SATURDAY && dia != DayOfWeek.SUNDAY;
    }


}
