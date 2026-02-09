package com.git.gestion_turnos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.git.gestion_turnos.entity.Persona;

public interface PersonaRepository extends JpaRepository<Persona, Integer>{
    Persona findByNombreAndApellidoAndTelefono(String nombre, String apellido, String telefono);
}
