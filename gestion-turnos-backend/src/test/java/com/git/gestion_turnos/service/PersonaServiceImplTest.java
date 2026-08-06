package com.git.gestion_turnos.service;

import com.git.gestion_turnos.dto.persona.PersonaDTO;
import com.git.gestion_turnos.entity.Persona;
import com.git.gestion_turnos.mapper.PersonaMapper;
import com.git.gestion_turnos.repository.PersonaRepository;
import com.git.gestion_turnos.service.historial_turno.IHistorialTurno;
import com.git.gestion_turnos.service.persona.PersonaServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonaServiceImplTest {
    @Mock
    private PersonaRepository personaRepository;

    @Spy
    private PersonaMapper personaMapper = new PersonaMapper();

    @Mock
    private IHistorialTurno historialTurno;

    @InjectMocks
    private PersonaServiceImpl personaService;

    @Test
    @DisplayName("Debe retornar a la persona si existen sus datos")
    public void retornarPersona_existente(){
        Persona persona = new Persona();
        persona.setNombre("Juan");
        persona.setApellido("Perez");
        persona.setTelefono("3586789002");

        PersonaDTO personaDto = new PersonaDTO();
        personaDto.setNombre("Juan");
        personaDto.setApellido("Perez");
        personaDto.setTelefono("3586789002");

        when(personaRepository.findByNombreAndApellidoAndTelefono(persona.getNombre(),
                persona.getApellido(),
                persona.getTelefono())).thenReturn(persona);

        Persona resultado = personaService.obtenerPersonaOCrear(personaDto);

        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());

        verify(personaRepository).findByNombreAndApellidoAndTelefono(persona.getNombre(),
                persona.getApellido(),
                persona.getTelefono());
    }

    @Test
    @DisplayName("Debe crear y retornar una nueva persona si no existen sus datos")
    public void obtenerPersonaOCrear_CuandoNoExiste_CreaYRetornaPersona() {
        PersonaDTO personaDto = new PersonaDTO();
        personaDto.setNombre("Juan");
        personaDto.setApellido("Perez");
        personaDto.setTelefono("3586789002");

        Persona personaCreada = new Persona();
        personaCreada.setId(1);
        personaCreada.setNombre("Juan");
        personaCreada.setApellido("Perez");
        personaCreada.setTelefono("3586789002");

        when(personaRepository.findByNombreAndApellidoAndTelefono(
                personaDto.getNombre(),
                personaDto.getApellido(),
                personaDto.getTelefono()
        )).thenReturn(null);

        when(personaMapper.toEntity(personaDto)).thenReturn(personaCreada);
        when(personaRepository.save(any(Persona.class))).thenReturn(personaCreada);
        when(personaMapper.toDTO(personaCreada)).thenReturn(personaDto);

        Persona resultado = personaService.obtenerPersonaOCrear(personaDto);

        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());
        assertEquals("Perez", resultado.getApellido());

        verify(personaRepository).findByNombreAndApellidoAndTelefono(
                personaDto.getNombre(),
                personaDto.getApellido(),
                personaDto.getTelefono()
        );
        verify(personaRepository).save(any(Persona.class));
    }
}
