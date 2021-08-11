package com.example.edushareproyect;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
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

public class CrearGrupo extends AppCompatActivity {

    EditText CodigoGrupo;
    EditText NombreGrupo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_grupo);

        CodigoGrupo = (EditText) findViewById(R.id.txtCodigoGrupo);
        NombreGrupo = (EditText) findViewById(R.id.txtNombreGrupo);

        SharedPreferences session = getSharedPreferences("session", MODE_PRIVATE);
        String token = session.getString("token","");


        Button btnAgregar = (Button) findViewById(R.id.btnAgregarGrupo);
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = CodigoGrupo.getText().toString();
                String grupo = NombreGrupo.getText().toString();
                if(token.isEmpty()){
                    mostrarDialogo("Erorr","No hay una session activa");
                    Intent main = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(main);
                }else{
                    if(code.isEmpty()){
                        mostrarDialogo("Error", "Debe ingresar un codigo");
                    }else{
                        agregar(token,grupo,code);
                    }
                }

            }
        });

    }

    //-------------------------------------------------------------------------------------------------------------------------------//
    private void agregar(String token,String grupo, String codigo){
        JSONObject objReq = new JSONObject();
        try{
            objReq.put("token",token);
            objReq.put("codigo",codigo);
            objReq.put("grupo", grupo);

            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, RestApiMehotds.ApiPOSTCrearGrupo, objReq, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try{
                        JSONArray ar = response.getJSONArray("response");
                        JSONObject r = ar.getJSONObject(0);
                        Integer status = r.getInt("STATUS");
                        String message = r.getString("MESSAGE");
                        if(status==1){
                            Intent home = new Intent(getApplicationContext(), VistaPrincipal.class);
                            startActivity(home);
                        }else{
                            mostrarDialogo("Error en la APP", message);
                            Log.e("APP EROR", r.toString());
                        }
                    }catch (JSONException je){
                        Log.e("Errro JSON objReq",je.getMessage());
                        mostrarDialogo("Error en la APP", "No se pudo obtener una respuesta del servidor");
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Error en el request",error.getMessage());
                    mostrarDialogo("Error en la APP", "No se pudo obtener una respuesta del servidor");
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(objectRequest);


        }catch(JSONException je){
            Log.e("Errro JSON objReq",je.getMessage());
            mostrarDialogo("Error en la APP", "No se puede crear el objeto de la peticion");
        }
    }
    //-------------------------------------------------------------------------------------------------------------------------------//

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



}