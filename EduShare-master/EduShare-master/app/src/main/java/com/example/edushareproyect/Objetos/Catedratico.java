package com.example.edushareproyect.Objetos;

public class Catedratico {


    String nombres;
    String apellidos;
    String identidad;
    String telefono;
    String fechaNacimiento;
    String direccion;
    String campus;
    String correo;
    String password;
    String foto;

    public Catedratico(

            String nombres,
            String apellidos,
            String identidad,
            String telefono,
            String fechaNacimiento,
            String direccion,

            String campus,
            String correo,
            String password,
            String foto){}



    public Catedratico(String nombre, String telefono, String foto, String correo) {
        this.nombres = nombre;
        this.telefono = telefono;
        this.foto = foto;
        this.correo = correo;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdentidad() {
        return identidad;
    }

    public void setIdentidad(String identidad) {
        this.identidad = identidad;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
