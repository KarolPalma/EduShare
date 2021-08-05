package com.example.edushareproyect;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    String url;
    String token;
    String correo = "";
    String password = "";

    SharedPreferences session;
    SharedPreferences.Editor EditorS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        session = this.getSharedPreferences("session",Context.MODE_PRIVATE);
        EditorS = session.edit();

        validarSesion();

        EditText txtCorreo = (EditText) findViewById(R.id.LoginCorreo);
        EditText txtPassword = (EditText) findViewById(R.id.LoginPassword);

        /*Intent principal = new Intent(this, RegistroAlumno.class);
        startActivity(principal);*/

        Button btnRegistro = (Button) findViewById(R.id.btnOpenRegistro);
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openReg = new Intent(getApplicationContext(), SeleccionarRegistro.class);
                startActivity(openReg);
            }
        });

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correo = txtCorreo.getText().toString();
                password = txtPassword.getText().toString();
                if(correo.equals("") || password.equals("")){
                    Toast.makeText(getApplicationContext(), "Debe ingresar el correo y password",Toast.LENGTH_LONG).show();
                }else{
                    Login(correo,password);

                }
            }
        });

    }

    public void Login(String correo, String password){
        url = RestApiMehotds.ApiPOSTLogin;
        HashMap<String, String> params =new HashMap<String, String>();
        params.put("correo",correo);
        params.put("password",password);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Toast.makeText(getApplicationContext(), "Respuesta: " + response, Toast.LENGTH_LONG).show();
                //VolleyLog.d("Status "+ response.toString());
                try{
                    String statusRespuesta = response.getString("STATUS");
                    if(statusRespuesta.equals("1")){
                        token = response.getString("DATA");
                        Integer perfilID = response.getInt("PERFIL");

                        /*CREARMOS LA SESSION EN SHAREDPREFERENCES*/
                        sessionData(token,perfilID);

                        /*Datos validos iniciamos la sesion*/
                        Intent intent = new Intent(getApplicationContext(),VistaPrincipal.class);
                        startActivity(intent);

                    }else{
                        Toast.makeText(getApplicationContext(), "Usuario o clave no valida", Toast.LENGTH_LONG).show();
                    }

                }catch(Exception e){
                    Toast.makeText(getApplicationContext(), "Respuesta: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error"+error.getMessage(), "Error: "+error.getMessage());
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue =Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);

    }

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

    //-----------------------------------------------------------------------------------------------------------------------//
    private void sessionData(String token, Integer perfilID){
        try{
            EditorS.putString("token",token);
            EditorS.putInt("perfilID",perfilID);
            EditorS.putBoolean("active",true);

            EditorS.apply();

        }catch(Exception e){
            mostrarDialogo("error",e.getMessage());
            e.printStackTrace();
        }


    }
    //-----------------------------------------------------------------------------------------------------------------------//

    //-----------------------------------------------------------------------------------------------------------------------//
    public void onClick(View v) {
            Intent recuperarClave = new Intent(getApplicationContext(), RecuperarClave.class);
            startActivity(recuperarClave);
    }
    //-----------------------------------------------------------------------------------------------------------------------//

}