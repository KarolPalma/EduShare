package com.example.edushareproyect.Objetos;

import android.graphics.Bitmap;

public class FotografiaUsuario {
    String id;
    Bitmap imagen;
    String nombre;

    public FotografiaUsuario(String id, Bitmap imagen, String nombre)
    {
        this.id = id;
        this.imagen = imagen;
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public Bitmap getImagen()
    {
        return imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
