package com.git.gestion_turnos.dto;

public class PersonaDTO {
    
    private String nombre;
    private String telefono;

    public PersonaDTO(){
    }

    public String getNombre(){
        return nombre;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public String getTelefono(){
        return telefono;
    }

    public void setTelefono(String telefono){
        this.telefono = telefono;
    }
}
