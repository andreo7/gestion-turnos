package com.git.gestion_turnos.dto.persona;

public class PersonaDetalleDTO {
    private Integer id;

    private String nombre;

    private String apellido;

    private String telefono;

    private int confirmaciones;

    private int cancelaciones;

    public PersonaDetalleDTO(){
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getConfirmaciones() {
        return confirmaciones;
    }

    public void setConfirmaciones(int confirmaciones) {
        this.confirmaciones = confirmaciones;
    }

    public int getCancelaciones() {
        return cancelaciones;
    }

    public void setCancelaciones(int cancelaciones) {
        this.cancelaciones = cancelaciones;
    }
}
