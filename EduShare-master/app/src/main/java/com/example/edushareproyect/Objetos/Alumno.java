package com.example.edushareproyect.Objetos;

public class Alumno {
    String id;
    String cuenta;
    String nombres;
    String apellidos;
    String identidad;
    String telefono;
    String fechaNacimiento;
    String direccion;
    String carrera;
    String campus;
    String correo;
    String password;
    String foto;

    public Alumno(){}

    public Alumno(String nombres, String apellidos, String telefono, String fechaNacimiento, String direccion, String carrera, String campus, String foto){
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.fechaNacimiento = fechaNacimiento;
        this.direccion = direccion;
        this.carrera = carrera;
        this.campus = campus;
        this.foto = foto;
    }

    public Alumno(String nombres, String apellidos, String telefono, String fechaNacimiento, String direccion, String carrera, String campus,
                  String cuenta, String identidad, String correo, String foto){
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.fechaNacimiento = fechaNacimiento;
        this.direccion = direccion;
        this.carrera = carrera;
        this.campus = campus;
        this.cuenta = cuenta;
        this.identidad = identidad;
        this.correo = correo;
        this.foto = foto;
    }


    public Alumno(
            String cuenta,
            String nombres,
            String apellidos,
            String identidad,
            String telefono,
            String fechaNacimiento,
            String direccion,
            String carrera,
            String campus,
            String correo,
            String password,
            String foto){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
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
