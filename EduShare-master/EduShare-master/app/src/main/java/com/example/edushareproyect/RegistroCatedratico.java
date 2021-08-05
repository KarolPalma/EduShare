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
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.edushareproyect.Objetos.Catedratico;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class RegistroCatedratico extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PETICION_ACCESO_PERMISOS = 100;

    ImageView FotoUser;
    byte[] byteArray;
    Catedratico catedratico;
    
    Spinner RegCatCampus;
    
    String nombres;
    String apellidos;
    String identidad;
    String telefono;
    String fechaNacimiento;
    String direccion;    
    String campus;
    String correo;
    String password;

    
    EditText txtIdentidad;
    EditText txtNombres;
    EditText txtApellidos;
    EditText txtTelefonos;
    EditText txtFechaNacimiento;
    EditText txtDireccion;
    EditText txtCorreo;
    EditText txtPassword;
    EditText txtPasswordConfirm;    
    ArrayList<String> ListaCampus = new ArrayList<>();

    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_catedratico);

        validarSesion();
        
        txtIdentidad = (EditText) findViewById(R.id.RegCatIdentidad);
        txtNombres = (EditText) findViewById(R.id.RegCatNombres);
        txtApellidos = (EditText) findViewById(R.id.RegCatApellidos);
        txtTelefonos = (EditText) findViewById(R.id.RegCatTelefono);
        txtFechaNacimiento = (EditText) findViewById(R.id.RegCatFechaNacimiento);
        txtDireccion = (EditText) findViewById(R.id.RegCatDireccion);
        txtCorreo = (EditText) findViewById(R.id.RegCatCorreo);
        txtPassword = (EditText) findViewById(R.id.RegCatPassword);
        txtPasswordConfirm = (EditText) findViewById(R.id.RegCatConfirmPassword);


        url = RestApiMehotds.ApiPOSTcatedratico;
        RegCatCampus = (Spinner) findViewById(R.id.RegCatCampus);
        
        ListaCampus.add("");

        catedratico = new Catedratico("","","","","","","","","","");

        ObtenerCampus();
        ArrayAdapter<CharSequence> adp2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, ListaCampus);
        RegCatCampus.setAdapter(adp2);

        FotoUser= (ImageView) findViewById(R.id.RegCatFoto);

        Button btnTomarFoto = (Button) findViewById(R.id.btnTakeFoto);
        //Tomar Foto para el contacto
        btnTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permisos();
            }
        });

        Button btnRegistro = (Button) findViewById(R.id.btnRegcatedratico);
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrar();
            }
        });
    }

    //-----------------------------------------------------------------------------------------------------------------------//
    private void getBytes(Intent data) {
        Bitmap photo = (Bitmap) data.getExtras().get("data");
        FotoUser.setImageBitmap(photo);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byteArray = stream.toByteArray();

        //SETEO DE DATOS EN EL OBJETO (FOTO BASE64 Y NOMBRE DEL ARCHIVO)
        String encode = Base64.encodeToString(byteArray, Base64.DEFAULT);
        catedratico.setFoto(encode);

    }
    //-----------------------------------------------------------------------------------------------------------------------//

    //-----------------------------------------------------------------------------------------------------------------------//
    private void ObtenerCampus(){
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                RestApiMehotds.ApiGETCampus,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        /*Respuesta*/
                        try{
                            JSONObject campus = new JSONObject(response);
                            JSONArray campusArray = campus.getJSONArray("campus");

                            for(int i=0;i<campusArray.length();i++){
                                JSONObject tmpObj = campusArray.getJSONObject(i);
                                ListaCampus.add(tmpObj.getString("CAMPUS"));
                            }

                        }catch(Exception e){
                            VolleyLog.e(e.getMessage());
                            Toast.makeText(getApplicationContext(), "Error al obtener la lista de campus", Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        /*Error*/
                        VolleyLog.e(error.getMessage());

                    }
                });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }
    //-----------------------------------------------------------------------------------------------------------------------//

    //-----------------------------------------------------------------------------------------------------------------------//
    public void permisos(){
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PETICION_ACCESO_PERMISOS);
        } else {
            tomarFoto();
        }
    }
    //-----------------------------------------------------------------------------------------------------------------------//


    //-----------------------------------------------------------------------------------------------------------------------//
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
    //-----------------------------------------------------------------------------------------------------------------------//

    //-----------------------------------------------------------------------------------------------------------------------//
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            getBytes(data);
        }
    }
    //-----------------------------------------------------------------------------------------------------------------------//

    //-----------------------------------------------------------------------------------------------------------------------//
    public void tomarFoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }
    //-----------------------------------------------------------------------------------------------------------------------//

    //-----------------------------------------------------------------------------------------------------------------------//
    private void mostrarDialogo(String title, String mensaje) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(mensaje)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }
    //-----------------------------------------------------------------------------------------------------------------------//


    //-----------------------------------------------------------------------------------------------------------------------//
    public void registrar(){        
        nombres = txtNombres.getText().toString();
        apellidos = txtApellidos.getText().toString();
        identidad = txtIdentidad.getText().toString();
        telefono = txtTelefonos.getText().toString();
        fechaNacimiento = txtFechaNacimiento.getText().toString();
        direccion = txtDireccion.getText().toString();        
        campus = RegCatCampus.getSelectedItem().toString();
        correo = txtCorreo.getText().toString();
        password = txtPassword.getText().toString();
        String passConfirm = txtPasswordConfirm.getText().toString();

        if(nombres.isEmpty()||apellidos.isEmpty()||identidad.isEmpty()||telefono.isEmpty()||fechaNacimiento.isEmpty()||direccion.isEmpty()||campus.isEmpty()||password.isEmpty()||passConfirm.isEmpty()){
            mostrarDialogo("Error","Debe llenar todos los campos del formulario");
            return;
        }

        if(!password.equals(passConfirm)){
            mostrarDialogo("Error","Las contraseñas no coinciden");
            VolleyLog.d("password: "+password);
            VolleyLog.d("confirmacion: "+passConfirm);
            return;
        }


        catedratico.setNombres(nombres);
        catedratico.setApellidos(apellidos);
        catedratico.setIdentidad(identidad);

        catedratico.setCampus(campus);
        catedratico.setCorreo(correo);
        catedratico.setFechaNacimiento(fechaNacimiento);
        catedratico.setDireccion(direccion);
        catedratico.setTelefono(telefono);
        catedratico.setPassword(password);

        JSONObject objReq = new JSONObject();
        try {
            objReq.put("identidad",catedratico.getIdentidad());
            objReq.put("nombres",catedratico.getNombres());
            objReq.put("apellidos",catedratico.getApellidos());
            objReq.put("campus",catedratico.getCampus());
            objReq.put("correo",catedratico.getCorreo());
            objReq.put("fechaNacimiento",catedratico.getFechaNacimiento());
            objReq.put("direccion",catedratico.getDireccion());
            objReq.put("telefono",catedratico.getTelefono());
            objReq.put("password",catedratico.getPassword());
            objReq.put("foto",catedratico.getFoto());

        }catch (Exception e){
            mostrarDialogo("Error",e.getMessage());
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest =new JsonObjectRequest(Request.Method.POST, url, objReq, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{

                    JSONArray ar = response.getJSONArray("response");
                    JSONObject r = ar.getJSONObject(0);
                    Integer status = r.getInt("status");
                    String message = r.getString("message");
                    String data = r.getString("data");

                    if(status.equals(1)){
                        mostrarDialogo("Exito!","Se envio un correo de verificacion a la cuenta: "+correo);
                        VolleyLog.d("Data: "+data);

                        /*Intent para validar correo electronico*/
                        Intent validacion = new Intent(getApplicationContext(), MailValidation.class);
                        validacion.putExtra("CODE",data);
                        validacion.putExtra("MAIL",correo);
                        startActivity(validacion);

                    }else{
                        VolleyLog.e(response.toString());
                        mostrarDialogo("Error",message);
                    }

                }catch(JSONException e){
                    mostrarDialogo("Error",e.getMessage());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error", "Error: " + error.getMessage());
                mostrarDialogo("Error",error.getMessage());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);




    }
    //-----------------------------------------------------------------------------------------------------------------------//

    private void validarSesion(){
        SharedPreferences informacionSession = getSharedPreferences("session", Context.MODE_PRIVATE);
        String token = informacionSession.getString("token","");
        Integer perfilID = informacionSession.getInt("perfilID",0);
        Boolean active = informacionSession.getBoolean("active",false);
        if(active){
            Intent vistaP = new Intent(getApplicationContext(), VistaPrincipal.class);
            startActivity(vistaP);
        }
    }
}