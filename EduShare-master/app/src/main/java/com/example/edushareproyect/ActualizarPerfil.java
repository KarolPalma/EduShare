package com.example.edushareproyect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.edushareproyect.Objetos.Alumno;
import com.example.edushareproyect.Objetos.Catedratico;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ActualizarPerfil extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PETICION_ACCESO_PERMISOS = 100;
    byte[] byteArray;

    EditText nombre, apellido, telefono, fechaN, direccion;
    TextView cuenta, identidad, correo, carrera, campus;
    ImageView imgActualizar;
    SharedPreferences informacionSession;
    ArrayList<String> ListaCarreras = new ArrayList<>();;
    ArrayList<String> ListaCampus = new ArrayList<>();;
    Alumno alumno;
    Catedratico catedratico;
    String usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_perfil);
        informacionSession = getApplicationContext().getSharedPreferences("session", Context.MODE_PRIVATE);
        nombre = (EditText) findViewById(R.id.lblNombreActualizar);
        apellido = (EditText) findViewById(R.id.lblApellidoActualizar);
        telefono = (EditText) findViewById(R.id.lblTelefonoActualizar);
        fechaN = (EditText) findViewById(R.id.lblFechaActualizar);
        direccion = (EditText) findViewById(R.id.lblDireccionActualizar);
        cuenta = (TextView) findViewById(R.id.lblCuentaActualizar);
        identidad = (TextView) findViewById(R.id.lblIdentidadActualizar);
        correo = (TextView) findViewById(R.id.lblACorreoctualizar);
        carrera = (TextView) findViewById(R.id.lblCarreraActualizar);
        campus = (TextView) findViewById(R.id.lblCampusActualizar);
        TextView cuentaAc = (TextView) findViewById(R.id.lblCuentaAc);
        TextView carreraAc = (TextView) findViewById(R.id.lblCarreraAc);
        imgActualizar = (ImageView) findViewById(R.id.imgActualizar);
        Button btnActualizarInfo = (Button) findViewById(R.id.btnActualizarInfo);
        Button btnActualizarFoto = (Button) findViewById(R.id.btnActualizarFoto);

        ListaCarreras.add("");
        ListaCampus.add("");

        alumno = new Alumno("","","","","","","","","","","","");

        Intent verPerfil = getIntent();
        usuario = verPerfil.getStringExtra("usuario2");
        if(usuario.equals("estudiante")){
            PostPerfilAlumno();
        }else if (usuario.equals("catedratico")){
            PostPerfilCatedratico();
            cuentaAc.setVisibility(View.INVISIBLE);
            carreraAc.setVisibility(View.INVISIBLE);
            cuenta.setVisibility(View.INVISIBLE);
            carrera.setVisibility(View.INVISIBLE);
        }

        btnActualizarInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(usuario.equals("estudiante")){
                    actualizarAlumno();
                }else if (usuario.equals("catedratico")){
                    actualizarCatedratico();
                }
            }
        });

        btnActualizarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permisos();
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
                        JSONArray amigoArrayJSON = response.getJSONArray("data");

                        for (int i = 0; i < amigoArrayJSON.length(); i++) {
                            JSONObject grupoObject = amigoArrayJSON.getJSONObject(i);
                            alumno = new Alumno(grupoObject.getString("NOMBRES"),
                                    grupoObject.getString("APELLIDOS"),
                                    grupoObject.getString("TELEFONO"),
                                    grupoObject.getString("FECHA_NACIMIENTO"),
                                    grupoObject.getString("DIRECCION"),
                                    grupoObject.getString("CARRERA"),
                                    grupoObject.getString("CAMPUS"),
                                    grupoObject.getString("CUENTA"),
                                    grupoObject.getString("IDENTIFICACION"),
                                    grupoObject.getString("CORREO"),
                                    grupoObject.getString("FOTO_URL"));
                        }

                        String[] split = alumno.getFechaNacimiento().split("T");
                        alumno.setFechaNacimiento(split[0]);

                        cuenta.setText(alumno.getCuenta());
                        identidad.setText(alumno.getIdentidad());
                        correo.setText(alumno.getCorreo());
                        nombre.setText(alumno.getNombres());
                        apellido.setText(alumno.getApellidos());
                        telefono.setText(alumno.getTelefono());
                        fechaN.setText(alumno.getFechaNacimiento());
                        direccion.setText(alumno.getDireccion());
                        carrera.setText(alumno.getCarrera());
                        campus.setText(alumno.getCampus());

                        if(alumno.getFoto().equalsIgnoreCase("null") == false) {
                            byte[] foto = Base64.decode(alumno.getFoto().getBytes(), Base64.DEFAULT);
                            Bitmap bitMapFoto = BitmapFactory.decodeByteArray(foto, 0, foto.length);
                            imgActualizar.setImageBitmap(bitMapFoto);
                        }else {
                            imgActualizar.setImageResource(R.drawable.img_default);
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
                        JSONArray amigoArrayJSON = response.getJSONArray("data");

                        for (int i = 0; i < amigoArrayJSON.length(); i++) {
                            JSONObject grupoObject = amigoArrayJSON.getJSONObject(i);
                            catedratico = new Catedratico(grupoObject.getString("NOMBRES"),
                                    grupoObject.getString("APELLIDOS"),
                                    grupoObject.getString("IDENTIFICACION"),
                                    grupoObject.getString("TELEFONO"),
                                    grupoObject.getString("FECHA_NACIMIENTO"),
                                    grupoObject.getString("DIRECCION"),
                                    grupoObject.getString("CAMPUS"),
                                    grupoObject.getString("CORREO"),
                                    grupoObject.getString("FOTO_URL"));
                        }

                        String[] split = catedratico.getFechaNacimiento().split("T");
                        catedratico.setFechaNacimiento(split[0]);

                        identidad.setText(catedratico.getIdentidad());
                        correo.setText(catedratico.getCorreo());
                        nombre.setText(catedratico.getNombres());
                        apellido.setText(catedratico.getApellidos());
                        telefono.setText(catedratico.getTelefono());
                        fechaN.setText(catedratico.getFechaNacimiento());
                        direccion.setText(catedratico.getDireccion());
                        campus.setText(catedratico.getCampus());

                        if(catedratico.getFoto().equalsIgnoreCase("null") == false) {
                            byte[] foto = Base64.decode(catedratico.getFoto().getBytes(), Base64.DEFAULT);
                            Bitmap bitMapFoto = BitmapFactory.decodeByteArray(foto, 0, foto.length);
                            imgActualizar.setImageBitmap(bitMapFoto);
                        }else {
                            imgActualizar.setImageResource(R.drawable.img_default);
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

    private void actualizarAlumno() {
        int comprobaciones = 0;
        int numeros = 0;
        if(nombre.getText().toString().isEmpty() || apellido.getText().toString().isEmpty() || telefono.getText().toString().isEmpty()
                || fechaN.getText().toString().isEmpty() || direccion.getText().toString().isEmpty()) {
            mostrarDialogoVacios();
            comprobaciones = 1;
        }

        if(alumno.getFoto() == "" && comprobaciones == 0) {
            mostrarDialogoImagenNoTomada();
            comprobaciones = 1;
        }

        if(comprobaciones == 0) {
            for (int i = 0; i < nombre.getText().toString().length(); i++) {
                if (Character.isDigit(nombre.getText().toString().charAt(i))) {
                    mostrarDialogoNumeros();
                    numeros = 1;
                    break;
                }
            }

            for (int i = 0; i < apellido.getText().toString().length(); i++) {
                if (Character.isDigit(apellido.getText().toString().charAt(i))) {
                    mostrarDialogoNumeros();
                    numeros = 1;
                    break;
                }
            }

            if (numeros == 0) {
                alumno.setNombres(nombre.getText().toString());
                alumno.setApellidos(apellido.getText().toString());
                alumno.setTelefono(telefono.getText().toString());
                alumno.setFechaNacimiento(fechaN.getText().toString());
                alumno.setDireccion(direccion.getText().toString());

                JSONObject object = new JSONObject();
                String url = RestApiMehotds.ApiUpdateStudent;
                String token = informacionSession.getString("token","");
                try {
                    object.put("cuenta", alumno.getCuenta());
                    object.put("identificacion", alumno.getIdentidad());
                    object.put("nombres",alumno.getNombres());
                    object.put("apellidos",alumno.getApellidos());
                    object.putOpt("telefono",alumno.getTelefono());
                    object.put("fechaNacimiento",alumno.getFechaNacimiento());
                    object.put("direccion",alumno.getDireccion());
                    object.putOpt("foto",alumno.getFoto());
                    object.put("carrera",alumno.getCarrera());
                    object.put("campus",alumno.getCampus());
                    object.putOpt("token",token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    Log.d("JSON", String.valueOf(response));
                                    String Error = response.getString("httpStatus");
                                    if (Error.equals("")||Error.equals(null)){
                                    }else if(Error.equals("OK")){
                                        JSONObject body = response.getJSONObject("body");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(getApplicationContext(),"Alumno Actualizado",Toast.LENGTH_LONG).show();
                                Intent verPerfil = new Intent(getApplicationContext(), VerPerfil.class);
                                startActivity(verPerfil);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("Error", "Error: " + error.getMessage());
                        Toast.makeText(ActualizarPerfil.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(jsonObjectRequest);
            }
        }
    }

    private void actualizarCatedratico() {
        int comprobaciones = 0;
        int numeros = 0;
        if(nombre.getText().toString().isEmpty() || apellido.getText().toString().isEmpty() || telefono.getText().toString().isEmpty()
                || fechaN.getText().toString().isEmpty() || direccion.getText().toString().isEmpty()) {
            mostrarDialogoVacios();
            comprobaciones = 1;
        }

        if(catedratico.getFoto() == "" && comprobaciones == 0) {
            mostrarDialogoImagenNoTomada();
            comprobaciones = 1;
        }

        if(comprobaciones == 0) {
            for (int i = 0; i < nombre.getText().toString().length(); i++) {
                if (Character.isDigit(nombre.getText().toString().charAt(i))) {
                    mostrarDialogoNumeros();
                    numeros = 1;
                    break;
                }
            }

            for (int i = 0; i < apellido.getText().toString().length(); i++) {
                if (Character.isDigit(apellido.getText().toString().charAt(i))) {
                    mostrarDialogoNumeros();
                    numeros = 1;
                    break;
                }
            }

            if (numeros == 0) {
                catedratico.setNombres(nombre.getText().toString());
                catedratico.setApellidos(apellido.getText().toString());
                catedratico.setTelefono(telefono.getText().toString());
                catedratico.setFechaNacimiento(fechaN.getText().toString());
                catedratico.setDireccion(direccion.getText().toString());

                JSONObject object = new JSONObject();
                String url = RestApiMehotds.ApiUpdateCatedratico;
                String token = informacionSession.getString("token","");
                try {
                    object.put("identificacion", catedratico.getIdentidad());
                    object.put("nombres",catedratico.getNombres());
                    object.put("apellidos",catedratico.getApellidos());
                    object.putOpt("telefono",catedratico.getTelefono());
                    object.put("fechaNacimiento",catedratico.getFechaNacimiento());
                    object.put("direccion",catedratico.getDireccion());
                    object.putOpt("foto",catedratico.getFoto());
                    object.put("campus",catedratico.getCampus());
                    object.putOpt("token",token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    Log.d("JSON", String.valueOf(response));
                                    String Error = response.getString("httpStatus");
                                    if (Error.equals("")||Error.equals(null)){
                                    }else if(Error.equals("OK")){
                                        JSONObject body = response.getJSONObject("body");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(getApplicationContext(),"Catedrático Actualizado",Toast.LENGTH_LONG).show();
                                Intent verPerfil = new Intent(getApplicationContext(), VerPerfil.class);
                                startActivity(verPerfil);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("Error", "Error: " + error.getMessage());
                        Toast.makeText(ActualizarPerfil.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(jsonObjectRequest);
            }
        }
    }

    private void mostrarDialogoImagenNoTomada() {
        new AlertDialog.Builder(this)
                .setTitle("Alerta de Fotografía")
                .setMessage("No se ha tomado ninguna fotografía")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }

    private void mostrarDialogoNumeros() {
        new AlertDialog.Builder(this)
                .setTitle("Alerta de Números")
                .setMessage("No puede ingresar números en el campo de Nombre y Apellido")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }

    private void mostrarDialogoVacios() {
        new AlertDialog.Builder(this)
                .setTitle("Alerta de Vacíos")
                .setMessage("No puede dejar ningún campo vacío")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }

    //Tomar Fotografía

    private void permisos() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ActualizarPerfil.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PETICION_ACCESO_PERMISOS);
        } else {
            tomarFoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PETICION_ACCESO_PERMISOS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                tomarFoto();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Se necesitan permisos de acceso", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            getBytes(data);
        }
    }

    private void getBytes(Intent data) {
        Bitmap photo = (Bitmap) data.getExtras().get("data");
        imgActualizar.setImageBitmap(photo);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byteArray = stream.toByteArray();

        //SETEO DE DATOS EN EL OBJETO (FOTO BASE64 Y NOMBRE DEL ARCHIVO)
        String encode = Base64.encodeToString(byteArray, Base64.DEFAULT);
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

        if(usuario.equals("estudiante")){
            alumno.setFoto(encode);
        }else if (usuario.equals("catedratico")){
            catedratico.setFoto(encode);
        }
    }

    private void tomarFoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void RecuperarClave(View v) {
        Intent cambiarClave = new Intent(getApplicationContext(), ChangePassword.class);
        startActivity(cambiarClave);
    }
}