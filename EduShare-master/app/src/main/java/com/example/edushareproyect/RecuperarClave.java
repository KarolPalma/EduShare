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

public class RecuperarClave extends AppCompatActivity {

    EditText txtPassRecovery;
    EditText txtVerificationCode;
    Button btnSendCode;
    Button btnValidarCodigo;

    String correo;
    String code;
    SharedPreferences session;
    SharedPreferences.Editor EditorS;

    String codigo_validacion;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_clave);

        session = this.getSharedPreferences("session", Context.MODE_PRIVATE);
        EditorS = session.edit();

        txtPassRecovery = (EditText) findViewById(R.id.txtPassRecovery);
        txtVerificationCode = (EditText) findViewById(R.id.txtVerificationCode);
        btnSendCode = (Button) findViewById(R.id.btnSendCode);
        btnValidarCodigo = (Button) findViewById(R.id.btnValidarCodigo);

        correo = txtPassRecovery.getText().toString();

        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correo = txtPassRecovery.getText().toString();
                sendCode();

            }
        });

        btnValidarCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateMail();

            }
        });

    }

    //-----------------------------------------------------------------------------------------------------------------------//
    //ENVIO DE CODIGO A CORREO ELECTRONICO
    private void sendCode(){

        if(this.correo.isEmpty()){
            mostrarDialogo("Error","Debe ingresar un correo electronico");
            return;
        }else{
            JSONObject req = new JSONObject();
            RequestQueue queue = Volley.newRequestQueue(this);
            try{
                req.put("mail",this.correo);
                JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, RestApiMehotds.ApiPOSTSendRecoveryCode, req, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONArray ar = response.getJSONArray("response");
                            JSONObject r = ar.getJSONObject(0);
                            if(r.getString("STATUS").equals("1")){
                                code = r.getString("data");
                                btnValidarCodigo.setEnabled(true);
                                mostrarDialogo("Informacion",r.getString("MESSAGE"));
                                Log.d("DATOS",response.toString());
                            }else{
                                mostrarDialogo("Error",r.getString("MESSAGE"));
                                Log.e("Datos",response.toString());
                            }
                        }catch (Exception e){
                            mostrarDialogo("error", e.getMessage());
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(objectRequest);
            }catch (JSONException jex){
                mostrarDialogo("Error",jex.getMessage());
                jex.printStackTrace();
            }
        }
    }
    //-----------------------------------------------------------------------------------------------------------------------//

    //-----------------------------------------------------------------------------------------------------------------------//
    private void validateMail(){
        JSONObject objReq = new JSONObject();
        try{
            String tmpCorreo = txtPassRecovery.getText().toString();
            String tmpCode = txtVerificationCode.getText().toString();
            objReq.put("correo",tmpCorreo);
            objReq.put("code",tmpCode);
            Log.d("Correo",tmpCorreo);
            Log.d("Code",tmpCode);

            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, RestApiMehotds.ApiPOSTSesionMail, objReq, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try{

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