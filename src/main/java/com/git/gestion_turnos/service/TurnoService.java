package com.git.gestion_turnos.service;

import com.git.gestion_turnos.entity.Turno;
import com.git.gestion_turnos.enums.EstadoTurno;
import com.git.gestion_turnos.repository.TurnoRepository;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;

@Service
public class TurnoService implements ITurno{
    private final TurnoRepository turnoRepository;
    private static final LocalTime HORA_INICIO = LocalTime.of(8, 0);
    private static final LocalTime HORA_FIN = LocalTime.of(16, 0);
    private static final int DURACION_TURNO = 30;


    public TurnoService(TurnoRepository turnoRepository){
        this.turnoRepository = turnoRepository; //Inyecto el repositorio.
    }

    //Genera los turnos del mes siguiente al actual solo si no existen turnos ya creados.
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
