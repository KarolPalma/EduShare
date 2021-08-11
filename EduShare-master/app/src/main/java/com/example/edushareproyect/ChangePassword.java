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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChangePassword extends AppCompatActivity {

    EditText txtClave1;
    EditText txtClave2;

    Button btnChangePassword;

    String clave1;
    String clave2;

    SharedPreferences session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        txtClave1 =  (EditText) findViewById(R.id.txtClave1);
        txtClave2 = (EditText) findViewById(R.id.txtClave2);

        session = getSharedPreferences("session", Context.MODE_PRIVATE);

        String token = session.getString("token","");

        btnChangePassword = (Button) findViewById(R.id.btnChangePassword);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clave1 = txtClave1.getText().toString();
                clave2 = txtClave2.getText().toString();
                if(!clave1.equals(clave2)){
                    mostrarDialogo("Error","Las claves no coinciden");
                }else{

                }

            }
        });

    }


    //-----------------------------------------------------------------------------------------------------------------------//
    private void changePassword(String pass1, String pass2, String token){
        JSONObject req = new JSONObject();
        RequestQueue queue = Volley.newRequestQueue(this);

        try{
            req.put("clave1",pass1);
            req.put("clave2",pass2);
            req.put("token",token);
            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, RestApiMehotds.ApiPOSTChangePassword, req, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try{
                        JSONArray ar = response.getJSONArray("response");
                        JSONObject r = ar.getJSONObject(0);
                        if(r.getInt("STATUS") == 1){
                            mostrarDialogo("Informacion","Se cambio la clave de acceso");
                            logout();
                            Intent vp = new Intent(getApplicationContext(), VistaPrincipal.class);
                            startActivity(vp);

                        }else{
                            mostrarDialogo("Error",r.getString("MESSAGE"));
                            Log.e("data",r.toString());
                        }
                    }catch(Exception e){
                        mostrarDialogo("Error",e.getMessage());
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }catch (JSONException jex){
            mostrarDialogo("Error",jex.getMessage());
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
    private void logout(){
        SharedPreferences session = getSharedPreferences("session", Context.MODE_PRIVATE);
        session.edit().clear().commit();
        Intent main = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(main);
    }
    //-----------------------------------------------------------------------------------------------------------------------//
}