package com.git.gestion_turnos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.git.gestion_turnos.entity.Persona;

public interface PersonaRepository extends JpaRepository<Persona, Integer>{
    Persona findByNombreAndTelefono(String nombre, String telefono);
}
