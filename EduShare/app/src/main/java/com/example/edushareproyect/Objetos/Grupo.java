package com.example.edushareproyect.Objetos;

public class Grupo {
    private String id;
    private String nombre;
    private String codigo;
    private String usuarioLogin;

    public Grupo() {
    }

    public Grupo(String id, String nombre, String codigo, String usuarioLogin) {
        this.id = id;
        this.nombre = nombre;
        this.codigo = codigo;
        this.usuarioLogin = usuarioLogin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getUsuarioLogin() {
        return usuarioLogin;
    }

    public void setUsuarioLogin(String usuarioLogin) {
        this.usuarioLogin = usuarioLogin;
    }
}
