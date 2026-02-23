package com.git.gestion_turnos.exception;

public class PersonaNotFoundException extends BaseException {
    public PersonaNotFoundException(Integer id) {
        super(
                String.format("Persona con ID &d ni encontrada", id), "PERSONA_001"
        );
    }
}
