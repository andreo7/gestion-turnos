package com.git.gestion_turnos.service;

import com.git.gestion_turnos.dto.persona.PersonaDTO;
import com.git.gestion_turnos.dto.turno.TurnoDTO;
import com.git.gestion_turnos.entity.Persona;
import com.git.gestion_turnos.entity.Turno;
import com.git.gestion_turnos.enums.EstadoTurno;
import com.git.gestion_turnos.exception.turno.TurnoNotFoundException;
import com.git.gestion_turnos.mapper.PersonaMapper;
import com.git.gestion_turnos.mapper.TurnoMapper;
import com.git.gestion_turnos.repository.TurnoRepository;
import com.git.gestion_turnos.service.turno.TurnoServiceConsulta;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TurnoServiceConsultaTest {

    //Creo los mocks (objetos falsos) que se inyectan en el service.
    @Mock
    private TurnoRepository turnoRepository;

    @Spy
    private PersonaMapper personaMapper = new PersonaMapper();

    @Spy
    private final TurnoMapper turnoMapper = new TurnoMapper(personaMapper);

    //Inyecto los mocks anteriores al service que voy a testear.
    @InjectMocks //Equivalente a hacer: TurnoServiceConsulta turnoServiceConsulta = new TurnoServiceConsulta(turnoRepository, turnoMapper)
    private TurnoServiceConsulta turnoServiceConsulta;

    @Test
    @DisplayName("Debe retornar el turno cuando existe el ID")
    public void obtenerTurno_cuandoExisteId(){

        //ARRAGE - Arranque: preparo los datos con los que voy a testear
        Turno turnoObtenido = new Turno();
        turnoObtenido.setId(1);
        Persona personaTurno = new Persona();
        personaTurno.setNombre("Juan");
        turnoObtenido.setPersona(personaTurno);

        //Esto le dice a mock: cuando llame a findById con el id 1 devolve el objeto turnoObtenido.
        when(turnoRepository.findById(1)).thenReturn(Optional.of(turnoObtenido));

        TurnoDTO resultado = turnoServiceConsulta.findById(1);

        assertNotNull(resultado);
        assertEquals("Juan", resultado.getCliente().getNombre());
        verify(turnoRepository).findById(1);
    }

    @Test
    @DisplayName("Debe retornar TurnoNotFoundExcepcion")
    public void deberiaLanzarExcepcionCuandoElTurnoNoExiste(){
        //Con ese id la llamada al metodo deberia lanzar una excepcion
        when(turnoRepository.findById(4)).thenReturn(Optional.empty());

        assertThrows(TurnoNotFoundException.class, () ->
                turnoServiceConsulta.findById(4));

        verify(turnoRepository).findById(4);
    }

    @Test
    @DisplayName("Debe retornar la lista de turnos")
    public void debeRetornarListaDeTurnos(){
        Turno turno1 = new Turno();
        turno1.setId(1);
        turno1.setEstado(EstadoTurno.RESERVADO);

        Turno turno2 = new Turno();
        turno2.setId(2);
        turno2.setEstado(EstadoTurno.CONFIRMADO);
        List<Turno> turnosObtenidos = List.of(turno1, turno2);

        when(turnoRepository.findAll()).thenReturn(turnosObtenidos);

        List<TurnoDTO> resultado = turnoServiceConsulta.findAll();

        assertFalse(resultado.isEmpty());
        assertEquals(2, resultado.size());
        assertEquals(EstadoTurno.RESERVADO, resultado.get(0).getEstado());
        assertEquals(EstadoTurno.CONFIRMADO, resultado.get(1).getEstado());
    }

    @Test
    @DisplayName("Debe retornar una lista con los turnos disponibles")
    public void debeRetornarListaConTurnosDisponibles(){
        Turno turno1 = new Turno();
        turno1.setId(1);
        turno1.setEstado(EstadoTurno.DISPONIBLE);

        Turno turno2 = new Turno();
        turno2.setId(2);
        turno2.setEstado(EstadoTurno.DISPONIBLE);

        Turno turno3 = new Turno();
        turno3.setId(3);
        turno3.setEstado(EstadoTurno.RESERVADO);
        List<Turno> turnosObtenidos = List.of(turno1, turno2, turno3);


        when(turnoRepository.findAll()).thenReturn(turnosObtenidos);

        List<TurnoDTO> resultado = turnoServiceConsulta.verTurnosDisponibles();

        assertEquals(2, resultado.size());
        verify(turnoRepository, times(1)).findAll();
    }
}
