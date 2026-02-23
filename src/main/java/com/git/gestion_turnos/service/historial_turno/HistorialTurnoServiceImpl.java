package com.git.gestion_turnos.service.historial_turno;

import com.git.gestion_turnos.dto.historial_turno.HistorialDetalleDTO;
import com.git.gestion_turnos.dto.historial_turno.HistorialTurnoMensualDTO;
import com.git.gestion_turnos.entity.HistorialTurno;
import com.git.gestion_turnos.entity.Turno;
import com.git.gestion_turnos.enums.EstadoTurno;
import com.git.gestion_turnos.mapper.HistorialTurnoMapper;
import com.git.gestion_turnos.repository.HistorialTurnoRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class HistorialTurnoServiceImpl implements IHistorialTurno {
    private final HistorialTurnoRepository historialTurnoRepository;
    private final HistorialTurnoMapper historialTurnoMapper;

    public HistorialTurnoServiceImpl(HistorialTurnoRepository historialTurnoRepository, HistorialTurnoMapper historialTurnoMapper){
        this.historialTurnoRepository = historialTurnoRepository;
        this.historialTurnoMapper = historialTurnoMapper;
    }

    @Transactional
    public void registrarCambioEstado(@NotNull Turno turno, EstadoTurno estadoTurno){
        HistorialTurno historialTurno = setAtributos(turno);
        historialTurno.setEstadoTurnoActual(estadoTurno);

        historialTurnoRepository.save(historialTurno);
    }

    public Integer contarTurnosPorPersonaYEstado(@NotNull Integer personaId, EstadoTurno estadoTurno){
        return historialTurnoRepository.countByPersonaIdAndEstadoTurnoActual(personaId, estadoTurno);
    }

    @Override
    public Page<HistorialDetalleDTO> listarHistorialDePersona(@NotNull Integer personaId, EstadoTurno estadoTurno, Pageable pageable){
        Page<HistorialTurno> page = historialTurnoRepository.findByPersonaIdAndEstadoTurnoActual(personaId, estadoTurno, pageable);

        return page.map(historialTurnoMapper:: toDetalleDto);
    }

    @Transactional
    public HistorialTurnoMensualDTO totalTurnosMensualesConEstado(int anio, int mes){
        HistorialTurnoMensualDTO historialTurnoMensual = new HistorialTurnoMensualDTO();

        Integer cancelaciones = historialTurnoRepository.totalTurnosMensualesConEstado(EstadoTurno.CANCELADO, anio, mes);
        Integer confirmaciones = historialTurnoRepository.totalTurnosMensualesConEstado(EstadoTurno.CONFIRMADO, anio, mes);
        Integer total = cancelaciones + confirmaciones; //No se tienen en cuenta los turnos en estado RESERVADO ya que no es un estado final.
        double porcentajeAsistencia = calcularPorcentaje(total, confirmaciones);

        historialTurnoMensual.setTotal(total);
        historialTurnoMensual.setCancelados(cancelaciones);
        historialTurnoMensual.setConfirmados(confirmaciones);
        historialTurnoMensual.setPorcentajeAsistencia(porcentajeAsistencia);

        return historialTurnoMensual;
    }

    //METODO AUXILIAR PARA CALCULAR EL PORCENTAJE DE ASISTENCIA EN UN MES.
    private double calcularPorcentaje(Integer totalMensual, Integer confirmados) {
        if(totalMensual == 0){
            return 0.0;
        }

        return (confirmados * 100.0) / totalMensual;
    }

    //METODO AUXILIAR para setear los atributos de un objeto HistorialTurno.
    private HistorialTurno setAtributos(@NotNull Turno turno){
        HistorialTurno historialTurno = new HistorialTurno();
        historialTurno.setTurno(turno);
        historialTurno.setPersona(turno.getPersona());
        historialTurno.setEstadoTurnoAnterior(turno.getEstado());
        historialTurno.setFechaHoraActualizacion(LocalDateTime.now());

        return historialTurno;
    }
}
