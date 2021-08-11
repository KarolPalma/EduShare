package com.example.edushareproyect;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MailValidation extends AppCompatActivity {

    EditText txtCode;
    String code = "";
    String correo = "";
    String url;

    SharedPreferences session;
    SharedPreferences.Editor EditorS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_validation);

        session = this.getSharedPreferences("session",Context.MODE_PRIVATE);
        EditorS = session.edit();

        url = RestApiMehotds.ApiPOSTSesionMail;

        Bundle extras = getIntent().getExtras();
        String codeValidar = extras.getString("CODE");
        correo = extras.getString("MAIL");
        Log.d("recibe correo",correo);

        txtCode = findViewById(R.id.RegVerificacion);

        Button btnValidar = (Button) findViewById(R.id.btnRegValidar);
        btnValidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code = txtCode.getText().toString();
                if(code.isEmpty()){
                    mostrarDialogo("Error","Ingrese el codigo");
                    return;
                }else{
                    Log.d("dev",code);
                }

                if(code.equals(codeValidar)){
                    getSession(correo,code);
                }else{
                    mostrarDialogo("Error","los codigos de validacion no coinciden");
                }

            }
        });
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
    private void getSession(String correo, String code){
        JSONObject objReq = new JSONObject();
        try{
            objReq.put("correo",correo);
            Log.d("correo a JSON",correo);
            objReq.put("code",code);
            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url, objReq, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try{
                        Log.e("show",response.toString());
                        JSONArray ar = response.getJSONArray("response");
                        JSONObject r = ar.getJSONObject(0);
                        Integer status = r.getInt("STATUS");
                        String message = r.getString("MESSAGE");
                        String token  = r.getString("TOKEN");
                        Integer perfilID = r.getInt("PERFIL");

                        if(status.equals(1)){
                            sessionData(token,perfilID);
                            Intent estudiante = new Intent(getApplicationContext(), VistaPrincipal.class);
                            startActivity(estudiante);
                        }else{
                            mostrarDialogo("Error",message);
                            Log.d("Erorr",response.toString());
                        }

                    }catch (JSONException e){
                        mostrarDialogo("Error","No se puede validar la sesion");
                        VolleyLog.e(e.getMessage());
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mostrarDialogo("Error",error.getMessage());

                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(objectRequest);

        }catch(JSONException e){
            mostrarDialogo("Error","No se puede validar el usuario");
            VolleyLog.e(e.getMessage());
        }
    }
    //-----------------------------------------------------------------------------------------------------------------------//

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
}