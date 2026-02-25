package com.git.gestion_turnos.service.turno;

import com.git.gestion_turnos.entity.Turno;
import com.git.gestion_turnos.enums.EstadoTurno;
import com.git.gestion_turnos.repository.TurnoRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;

@Service
public class TurnoServiceGenerador implements ITurnoGenerador{
    private TurnoRepository turnoRepository;
    private static final LocalTime HORA_INICIO = LocalTime.of(8, 0);
    private static final LocalTime HORA_FIN = LocalTime.of(16, 0);
    private static final int DURACION_TURNO = 30;

    public TurnoServiceGenerador(TurnoRepository turnoRepository){
        this.turnoRepository = turnoRepository;
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
}
