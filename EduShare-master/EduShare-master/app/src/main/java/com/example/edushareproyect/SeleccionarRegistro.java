package com.example.edushareproyect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SeleccionarRegistro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_registro);

        Button regAlumno = (Button) findViewById(R.id.btnRegAluOpen);
        Button regCatedratico = (Button) findViewById(R.id.btnRegCatOpen);

        regAlumno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regAlu = new Intent(getApplicationContext(), RegistroAlumno.class);
                startActivity(regAlu);
            }
        });
        regCatedratico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regCat = new Intent(getApplicationContext(), RegistroCatedratico.class);
                startActivity(regCat);
            }
        });

    }
}