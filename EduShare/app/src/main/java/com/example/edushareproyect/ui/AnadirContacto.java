package com.example.edushareproyect.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.edushareproyect.Objetos.Catedratico;
import com.example.edushareproyect.Objetos.Usuario;
import com.example.edushareproyect.R;
import com.example.edushareproyect.RestApiMehotds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnadirContacto#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnadirContacto extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Usuario usuario;
    ImageView imgUsuario;
    TextView txtNombres, txtTelefono,txtCarrera, txtCampus, txtCorreo;
    SharedPreferences informacionSession;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AnadirContacto() {
        // Required empty public constructor
    }

    public AnadirContacto(String idContacto) {
        // Required empty public constructor
        mParam1 = idContacto;
        mParam2 = null;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AnadirContacto.
     */
    // TODO: Rename and change types and number of parameters
    public static AnadirContacto newInstance(String param1, String param2) {
        AnadirContacto fragment = new AnadirContacto();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_anadir_contacto, container, false);

        txtNombres = (TextView) root.findViewById(R.id.lblNombreContacto);
        txtTelefono = (TextView) root.findViewById(R.id.lblTelefonoContacto);
        txtCarrera = (TextView) root.findViewById(R.id.lblCarreraContacto);
        txtCampus = (TextView) root.findViewById(R.id.lblCampusContacto);
        txtCorreo = (TextView) root.findViewById(R.id.lblCorreoContacto);
        imgUsuario = (ImageView) root.findViewById(R.id.imgContacto);
        Button btnAgregarAmigo = (Button) root.findViewById(R.id.btnAgregarAmigo);

        informacionSession = root.getContext().getSharedPreferences("session", Context.MODE_PRIVATE);

        PostIntegrante();

        btnAgregarAmigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarAmigo();
            }
        });

        return root;
    }

    private void agregarAmigo() {
        new AlertDialog.Builder(getContext())
                .setTitle("Confirmación de Agregar Compañero")
                .setMessage("¿Desea agregar a " + txtNombres.getText() + " a su lista de amigos?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        RequestQueue queue = Volley.newRequestQueue(getContext());
                        String token = informacionSession.getString("token","");
                        String url = RestApiMehotds.ApiPostAddFriend;
                        JSONObject postData = new JSONObject();
                        try {
                            postData.put("token",token);
                            postData.put("contactoid", mParam1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("Error en Response", "onResponse: " +  error.getMessage().toString() );
                            }
                        });
                        queue.add(jsonObjectRequest);
                        Toast.makeText(getContext(), "Compañero agregado", Toast.LENGTH_LONG).show();

                        /*Fragment listaComp = new ListaContactos();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.nav_host_fragment_content_vista_principal, listaComp);
                        transaction.addToBackStack(null);
                        transaction.commit();*/
                    }

                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getContext(), "Se canceló agregar al compañero", Toast.LENGTH_LONG).show();
                    }
                }).show();
    }

    private void PostIntegrante(){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = RestApiMehotds.ApiPostInfoUser;
        JSONObject postData = new JSONObject();
        try {
            postData.put("usuarioid", mParam1);
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
                            usuario = new Usuario(grupoObject.getString("CONTACTO"),
                                    grupoObject.getString("TELEFONO"),
                                    grupoObject.getString("CARRERA"),
                                    grupoObject.getString("CAMPUS"),
                                    grupoObject.getString("CORREO"),
                                    grupoObject.getString("FOTO_URL"));
                        }
                        txtNombres.setText(usuario.getNombres());
                        txtTelefono.setText(usuario.getTelefono());
                        txtCarrera.setText(usuario.getCarrera());
                        txtCampus.setText(usuario.getCampus());
                        txtCorreo.setText(usuario.getCorreo());

                        if(usuario.getFoto().equalsIgnoreCase("null") == false) {
                            byte[] foto = Base64.decode(usuario.getFoto().getBytes(), Base64.DEFAULT);
                            Bitmap bitMapFoto = BitmapFactory.decodeByteArray(foto, 0, foto.length);
                            imgUsuario.setImageBitmap(bitMapFoto);
                        }else {
                            imgUsuario.setImageResource(R.drawable.img_default);
                        }
                    }else{
                        Toast.makeText(getContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException ex) {
                    Toast.makeText(getContext(), "Excepción en Response", Toast.LENGTH_LONG).show();
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