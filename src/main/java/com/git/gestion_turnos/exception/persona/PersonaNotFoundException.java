package com.git.gestion_turnos.exception.persona;

import com.git.gestion_turnos.exception.BaseException;
import org.springframework.http.HttpStatus;

public class PersonaNotFoundException extends BaseException {
    public PersonaNotFoundException(Integer id) {
        super(String.format("Persona con ID %d ni encontrada", id),
                "PERSONA_001",
                HttpStatus.NOT_FOUND);
    }
}
