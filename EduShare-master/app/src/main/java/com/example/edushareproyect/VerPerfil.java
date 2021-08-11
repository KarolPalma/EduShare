package com.example.edushareproyect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.edushareproyect.Objetos.Alumno;
import com.example.edushareproyect.Objetos.Catedratico;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VerPerfil extends AppCompatActivity {

    Alumno alumno;
    Catedratico catedratico;
    TextView txtNombres, txtTelefono, txtFechaN, txtDireccion, txtCarrera, txtCampus, lblCarrera;
    ImageView imgPerfil;
    SharedPreferences informacionSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_perfil);

        TextView Titulo = (TextView) findViewById(R.id.lblTituloInfoPerfil);

        txtNombres = (TextView) findViewById(R.id.lblNombrePerfil);
        txtTelefono = (TextView) findViewById(R.id.lblTelefonoPerfil);
        txtFechaN = (TextView) findViewById(R.id.lblFechaPerfil);
        txtDireccion = (TextView) findViewById(R.id.lblDireccionPerfil);
        txtCarrera = (TextView) findViewById(R.id.lblCarreraPerfil);
        lblCarrera = (TextView) findViewById(R.id.lblCarreraP);
        txtCampus = (TextView) findViewById(R.id.lblCampusPerfil);
        imgPerfil = (ImageView) findViewById(R.id.imgPerfil);
        Button btnActualizarPerfil = (Button) findViewById(R.id.btnActualizarFoto);

        informacionSession = getApplicationContext().getSharedPreferences("session", Context.MODE_PRIVATE);

        Intent verPerfil = getIntent();
        String usuario = verPerfil.getStringExtra("usuario");
        //Toast.makeText(getApplicationContext(), usuario, Toast.LENGTH_LONG).show();
        if(usuario.equals("estudiante")){
            Titulo.setText("Estudiante");
            PostPerfilAlumno();
        }else if (usuario.equals("catedratico")){
            Titulo.setText("Catedrático");
            PostPerfilCatedratico();
            txtCarrera.setVisibility(View.INVISIBLE);
            lblCarrera.setVisibility(View.INVISIBLE);
        }

        btnActualizarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(usuario.equals("estudiante")){
                    Intent actualizarPerfil = new Intent(getApplicationContext(), ActualizarPerfil.class);
                    actualizarPerfil.putExtra("usuario2", "estudiante");
                    startActivity(actualizarPerfil);
                }else if (usuario.equals("catedratico")){
                    Intent actualizarPerfil = new Intent(getApplicationContext(), ActualizarPerfil.class);
                    actualizarPerfil.putExtra("usuario2", "catedratico");
                    startActivity(actualizarPerfil);
                }
            }
        });

    }

    private void PostPerfilAlumno(){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = RestApiMehotds.ApiPostStudent;
        String token = informacionSession.getString("token","");
        JSONObject postData = new JSONObject();
        try {
            postData.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getString("status").equalsIgnoreCase("1")) {
                        JSONArray alumnoArrayJSON = response.getJSONArray("data");

                        for (int i = 0; i < alumnoArrayJSON.length(); i++) {
                            JSONObject grupoObject = alumnoArrayJSON.getJSONObject(i);
                            alumno = new Alumno(grupoObject.getString("NOMBRES"),
                                    grupoObject.getString("APELLIDOS"),
                                    grupoObject.getString("TELEFONO"),
                                    grupoObject.getString("FECHA_NACIMIENTO"),
                                    grupoObject.getString("DIRECCION"),
                                    grupoObject.getString("CARRERA"),
                                    grupoObject.getString("CAMPUS"),
                                    grupoObject.getString("FOTO_URL"));
                        }
                        String[] split = alumno.getFechaNacimiento().split("T");
                        alumno.setFechaNacimiento(split[0]);

                        txtNombres.setText(alumno.getNombres()+" "+alumno.getApellidos());
                        txtTelefono.setText(alumno.getTelefono());
                        txtFechaN.setText(alumno.getFechaNacimiento());
                        txtDireccion.setText(alumno.getDireccion());
                        txtCarrera.setText(alumno.getCarrera());
                        txtCampus.setText(alumno.getCampus());

                        if(alumno.getFoto().equalsIgnoreCase("null") == false) {
                            byte[] foto = Base64.decode(alumno.getFoto().getBytes(), Base64.DEFAULT);
                            Bitmap bitMapFoto = BitmapFactory.decodeByteArray(foto, 0, foto.length);
                            imgPerfil.setImageBitmap(bitMapFoto);
                        }else {
                            imgPerfil.setImageResource(R.drawable.img_default);
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException ex) {
                    Toast.makeText(getApplicationContext(), "Excepción en Response", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error en Response", "onResponse: " +  error.getMessage().toString() );
            }
        });
        queue.add(jsonObjectRequest);
    }

    private void PostPerfilCatedratico(){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = RestApiMehotds.ApiPostCatedratico;
        String token = informacionSession.getString("token","");
        JSONObject postData = new JSONObject();
        try {
            postData.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getString("status").equalsIgnoreCase("1")) {
                        JSONArray catedraticoArrayJSON = response.getJSONArray("data");

                        for (int i = 0; i < catedraticoArrayJSON.length(); i++) {
                            JSONObject grupoObject = catedraticoArrayJSON.getJSONObject(i);
                            catedratico = new Catedratico(grupoObject.getString("NOMBRES"),
                                    grupoObject.getString("APELLIDOS"),
                                    grupoObject.getString("TELEFONO"),
                                    grupoObject.getString("FECHA_NACIMIENTO"),
                                    grupoObject.getString("DIRECCION"),
                                    grupoObject.getString("CAMPUS"),
                                    grupoObject.getString("FOTO_URL"));
                        }
                        txtNombres.setText(catedratico.getNombres()+" "+catedratico.getApellidos());
                        txtTelefono.setText(catedratico.getTelefono());
                        txtFechaN.setText(catedratico.getFechaNacimiento());
                        txtDireccion.setText(catedratico.getDireccion());
                        txtCampus.setText(catedratico.getCampus());

                        if(catedratico.getFoto().equalsIgnoreCase("null") == false) {
                            byte[] foto = Base64.decode(catedratico.getFoto().getBytes(), Base64.DEFAULT);
                            Bitmap bitMapFoto = BitmapFactory.decodeByteArray(foto, 0, foto.length);
                            imgPerfil.setImageBitmap(bitMapFoto);
                        }else {
                            imgPerfil.setImageResource(R.drawable.img_default);
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException ex) {
                    Toast.makeText(getApplicationContext(), "Excepción en Response", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error en Response", "onResponse: " +  error.getMessage().toString() );
            }
        });
        queue.add(jsonObjectRequest);
    }
}