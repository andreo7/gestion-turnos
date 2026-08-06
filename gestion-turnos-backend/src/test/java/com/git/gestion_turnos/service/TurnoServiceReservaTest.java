package com.git.gestion_turnos.service;

import com.git.gestion_turnos.dto.persona.PersonaDTO;
import com.git.gestion_turnos.dto.turno.TurnoDTO;
import com.git.gestion_turnos.entity.HistorialTurno;
import com.git.gestion_turnos.entity.Persona;
import com.git.gestion_turnos.entity.Turno;
import com.git.gestion_turnos.enums.EstadoTurno;
import com.git.gestion_turnos.exception.turno.TurnoClienteNullException;
import com.git.gestion_turnos.exception.turno.TurnoDisponibleException;
import com.git.gestion_turnos.exception.turno.TurnoNoDisponibleException;
import com.git.gestion_turnos.exception.turno.TurnoNoReservadoException;
import com.git.gestion_turnos.mapper.PersonaMapper;
import com.git.gestion_turnos.mapper.TurnoMapper;
import com.git.gestion_turnos.repository.TurnoRepository;
import com.git.gestion_turnos.service.historial_turno.IHistorialTurno;
import com.git.gestion_turnos.service.notificacion.INotificacion;
import com.git.gestion_turnos.service.persona.IPersona;
import com.git.gestion_turnos.service.turno.TurnoServiceReserva;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TurnoServiceReservaTest {
    @Mock
    private TurnoRepository turnoRepository;

    @Mock
    private IHistorialTurno historialTurno;

    @Mock
    private IPersona personaService;

    @Mock
    private INotificacion notificacionService;

    @Spy
    private PersonaMapper personaMapper = new PersonaMapper();

    @Spy
    private TurnoMapper turnoMapper = new TurnoMapper(personaMapper);

    @InjectMocks
    private TurnoServiceReserva turnoServiceReserva;

    @Test
    @DisplayName("Debe reservar el turno si esta DISPONIBLE")
    public void reservaSiEstaDisponible(){
        Turno turno = new Turno();
        turno.setId(1);
        turno.setEstado(EstadoTurno.DISPONIBLE);

        PersonaDTO personaDTO = new PersonaDTO();
        personaDTO.setId(10);

        Persona persona = new Persona();
        persona.setId(10);

        when(turnoRepository.findById(1)).thenReturn(Optional.of(turno));
        when(personaService.getById(10)).thenReturn(persona);

        TurnoDTO resultado = turnoServiceReserva.reservarTurno(1, personaDTO);

        assertNotNull(resultado);
        assertEquals(EstadoTurno.RESERVADO, turno.getEstado());
        assertEquals(persona, turno.getPersona());

        verify(personaService).getById(10);
        verify(personaService, never()).obtenerPersonaOCrear(any()); // Aseguramos que NO entró al else
        verify(notificacionService).crearRecordatorio24h(persona, turno);
        verify(historialTurno).registrarCambioEstado(turno, EstadoTurno.RESERVADO);
        verify(turnoRepository).save(turno);
    }

    @Test
    @DisplayName("Debe reservar el turno exitosamente creando o recuperando persona cuando ID es null")
    void reservarTurno_Exito_ConPersonaSinId() {
        PersonaDTO personaDto = new PersonaDTO(); // id = null por defecto
        personaDto.setNombre("Juan");

        Turno turno = new Turno();
        turno.setId(1);
        turno.setEstado(EstadoTurno.DISPONIBLE);

        Persona personaNueva = new Persona();
        personaNueva.setNombre("Juan");

        when(turnoRepository.findById(1)).thenReturn(Optional.of(turno));
        when(personaService.obtenerPersonaOCrear(personaDto)).thenReturn(personaNueva);

        TurnoDTO resultado = turnoServiceReserva.reservarTurno(1, personaDto);

        assertNotNull(resultado);
        assertEquals("Juan", resultado.getCliente().getNombre());
        verify(personaService, never()).getById(any()); // Aseguramos que NO entró al if
        verify(personaService).obtenerPersonaOCrear(personaDto);
        verify(turnoRepository).save(turno);
    }

    @Test
    @DisplayName("Debe lanzar TurnoNoDisponibleException cuando el turno no está DISPONIBLE")
    void reservarTurno_LanzaExcepcion_CuandoTurnoNoDisponible() {
        PersonaDTO personaDto = new PersonaDTO();

        Turno turno = new Turno();
        turno.setId(1);
        turno.setEstado(EstadoTurno.RESERVADO);

        when(turnoRepository.findById(1)).thenReturn(Optional.of(turno));

        assertThrows(TurnoNoDisponibleException.class, () -> {
            turnoServiceReserva.reservarTurno(1, personaDto);
        });

        // Verificamos que el proceso se cortó y NUNCA se notificó ni se guardó nada
        verifyNoInteractions(personaService, notificacionService, historialTurno);
        verify(turnoRepository, never()).save(any());
    }

    @Nested
    @DisplayName("Tests para cancelarTurno")
    class CancelarTurnoTests {

        @Test
        @DisplayName("Debe cancelar el turno exitosamente cuando está RESERVADO y tiene persona")
        void cancelarTurno_Exito() {
            Integer idTurno = 1;
            Turno turno = new Turno();
            turno.setId(idTurno);
            turno.setEstado(EstadoTurno.RESERVADO);
            turno.setPersona(new Persona());

            TurnoDTO turnoDtoEsperado = new TurnoDTO();

            when(turnoRepository.findById(idTurno)).thenReturn(Optional.of(turno));
            when(turnoMapper.toDto(turno)).thenReturn(turnoDtoEsperado);

            TurnoDTO resultado = turnoServiceReserva.cancelarTurno(idTurno);

            assertNotNull(resultado);
            assertNull(turno.getPersona());
            assertEquals(EstadoTurno.DISPONIBLE, turno.getEstado()); // Mapea que volvió a quedar DISPONIBLE

            verify(historialTurno).registrarCambioEstado(turno, EstadoTurno.CANCELADO);
            verify(turnoRepository).save(turno);
            verify(turnoMapper).toDto(turno);
        }

        @Test
        @DisplayName("Debe lanzar TurnoDisponibleException si el turno no está RESERVADO ni CONFIRMADO")
        void cancelarTurno_LanzaExcepcion_CuandoEstadoInvalido() {
            Integer idTurno = 1;
            Turno turno = new Turno();
            turno.setId(idTurno);
            turno.setEstado(EstadoTurno.DISPONIBLE); // Estado no cancelable
            turno.setPersona(new Persona());

            when(turnoRepository.findById(idTurno)).thenReturn(Optional.of(turno));

            assertThrows(TurnoDisponibleException.class, () -> {
                turnoServiceReserva.cancelarTurno(idTurno);
            });

            // Aseguramos que NO modificó la base de datos ni registró en el historial
            verify(historialTurno, never()).registrarCambioEstado(any(), any());
            verify(turnoRepository, never()).save(any());
        }

        @Test
        @DisplayName("Debe lanzar TurnoClienteNullException si el turno no tiene persona asignada")
        void cancelarTurno_LanzaExcepcion_CuandoPersonaEsNull() {
            Integer idTurno = 1;
            Turno turno = new Turno();
            turno.setId(idTurno);
            turno.setEstado(EstadoTurno.CONFIRMADO);
            turno.setPersona(null); // Persona es null

            when(turnoRepository.findById(idTurno)).thenReturn(Optional.of(turno));

            assertThrows(TurnoClienteNullException.class, () -> {
                turnoServiceReserva.cancelarTurno(idTurno);
            });

            verify(historialTurno, never()).registrarCambioEstado(any(), any());
            verify(turnoRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Tests para confirmarTurno")
    class ConfirmarTurnoTests {

        @Test
        @DisplayName("Debe confirmar el turno exitosamente cuando está RESERVADO y tiene persona asignada")
        void confirmarTurno_Exito() {
            Integer idTurno = 1;
            Turno turno = new Turno();
            turno.setId(idTurno);
            turno.setEstado(EstadoTurno.RESERVADO);
            turno.setPersona(new Persona());

            when(turnoRepository.findById(idTurno)).thenReturn(Optional.of(turno));

            turnoServiceReserva.confirmarTurno(idTurno);

            assertEquals(EstadoTurno.CONFIRMADO, turno.getEstado());

            verify(historialTurno).registrarCambioEstado(turno, EstadoTurno.CONFIRMADO);
            verify(turnoRepository).save(turno);
        }

        @Test
        @DisplayName("Debe lanzar TurnoNoReservadoException si el turno no está en estado RESERVADO")
        void confirmarTurno_LanzaExcepcion_CuandoEstadoNoEsReservado() {
            Integer idTurno = 1;
            Turno turno = new Turno();
            turno.setId(idTurno);
            turno.setEstado(EstadoTurno.DISPONIBLE); // No está RESERVADO
            turno.setPersona(new Persona());

            when(turnoRepository.findById(idTurno)).thenReturn(Optional.of(turno));

            assertThrows(TurnoNoReservadoException.class, () -> {
                turnoServiceReserva.confirmarTurno(idTurno);
            });

            // Aseguramos que corta la ejecución y no modifica la DB ni el historial
            verify(historialTurno, never()).registrarCambioEstado(any(), any());
            verify(turnoRepository, never()).save(any());
        }

        @Test
        @DisplayName("Debe lanzar TurnoClienteNullException si el turno no tiene persona asignada")
        void confirmarTurno_LanzaExcepcion_CuandoPersonaEsNull() {
            Integer idTurno = 1;
            Turno turno = new Turno();
            turno.setId(idTurno);
            turno.setEstado(EstadoTurno.RESERVADO);
            turno.setPersona(null); // Sin persona asociada

            when(turnoRepository.findById(idTurno)).thenReturn(Optional.of(turno));

            assertThrows(TurnoClienteNullException.class, () -> {
                turnoServiceReserva.confirmarTurno(idTurno);
            });

            //Aseguramos que NO llega a cambiar de estado ni persistir
            verify(historialTurno, never()).registrarCambioEstado(any(), any());
            verify(turnoRepository, never()).save(any());
        }
    }
}
