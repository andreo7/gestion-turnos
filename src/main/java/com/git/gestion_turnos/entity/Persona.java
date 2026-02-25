package com.git.gestion_turnos.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "personas", uniqueConstraints =
                        {@UniqueConstraint(
                                name = "uk_nombre_telefono", columnNames = {"nombre", "apellido", "telefono"})})
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String nombre;
    private String apellido;
    private String telefono;

    public Persona(){

    }

    public Integer getId(){
        return id;
    }
    
    public void setId(Integer id){
        this.id = id;
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
}
