package com.example.edushareproyect.Objetos;

public class Amigo {

    private String usuarioid;
    private String contactoid;
    private String nombres;
    private String foto;
    private String telefono;
    private String carrera;
    private String campus;
    private String correo;

    public Amigo(){

    }

    public Amigo(String nombres, String telefono, String carrera, String campus, String correo, String foto) {
        this.nombres = nombres;
        this.telefono = telefono;
        this.carrera = carrera;
        this.campus = campus;
        this.correo = correo;
        this.foto = foto;
    }

    public String getUsuarioid() {
        return usuarioid;
    }

    public void setUsuarioid(String usuarioid) {
        this.usuarioid = usuarioid;
    }

    public String getContactoid() {
        return contactoid;
    }

    public void setContactoid(String contactoid) {
        this.contactoid = contactoid;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
