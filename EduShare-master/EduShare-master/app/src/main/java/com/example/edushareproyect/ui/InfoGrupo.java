package com.example.edushareproyect.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.se.omapi.Session;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.edushareproyect.Adaptadores.Adaptador;
import com.example.edushareproyect.Objetos.Alumno;
import com.example.edushareproyect.Objetos.Catedratico;
import com.example.edushareproyect.Objetos.FotografiaUsuario;
import com.example.edushareproyect.R;
import com.example.edushareproyect.RestApiMehotds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoGrupo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoGrupo extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    Catedratico catedratico;
    TextView txtCateNombreInfo, txtCorreoInfo, txtTelInfo;
    ImageView imgCatedratico;
    SharedPreferences informacionSession;

    ArrayList<FotografiaUsuario> listadoFotos;
    ListView listadoIntegrantes;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mParam3;

    public InfoGrupo() {
        // Required empty public constructor
    }

    public InfoGrupo(String id, String grupo, String codigo) {
        mParam1 = id;
        mParam2 = grupo;
        mParam3 = codigo;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfoGrupo.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoGrupo newInstance(String param1, String param2, String param3) {
        InfoGrupo fragment = new InfoGrupo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_info_grupo, container, false);

        TextView txtNombreInfo = (TextView) root.findViewById(R.id.txtNombreInfo);
        TextView txtCodigoInfo = (TextView) root.findViewById(R.id.txtCodigoInfo);
        txtCateNombreInfo = (TextView) root.findViewById(R.id.txtCateNombreInfo);
        txtCorreoInfo = (TextView) root.findViewById(R.id.txtCorreoInfo);
        txtTelInfo = (TextView) root.findViewById(R.id.txtTelInfo);
        imgCatedratico = (ImageView) root.findViewById(R.id.imgAdmin);
        listadoIntegrantes = (ListView) root.findViewById(R.id.listadoAlumnos);

        txtNombreInfo.setText(mParam2);
        txtCodigoInfo.setText(mParam3);

        informacionSession = root.getContext().getSharedPreferences("session", Context.MODE_PRIVATE);

        PostCatedratico();
        listarIntegrantes();

        listadoIntegrantes.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                String idIntegrante = listadoFotos.get(position).getId();

                Fragment integrante = new AnadirContacto(idIntegrante);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_content_vista_principal, integrante);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return root;
    }

    private void PostCatedratico(){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = RestApiMehotds.ApiPostAdmin;
        JSONObject postData = new JSONObject();
        try {
            postData.put("grupoid", mParam1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getString("status").equalsIgnoreCase("1")) {
                        JSONArray grupoArrayJSON = response.getJSONArray("data");

                        for (int i = 0; i < grupoArrayJSON.length(); i++) {
                            JSONObject grupoObject = grupoArrayJSON.getJSONObject(i);

                            catedratico = new Catedratico(grupoObject.getString("CATEDRATICO"),
                                    grupoObject.getString("TELEFONO"),
                                    grupoObject.getString("FOTO_URL"),
                                    grupoObject.getString("CORREO"));
                        }
                        txtCateNombreInfo.setText(catedratico.getNombres());
                        txtCorreoInfo.setText(catedratico.getCorreo());
                        txtTelInfo.setText(catedratico.getTelefono());

                        if(catedratico.getFoto().equalsIgnoreCase("null") == false) {
                            byte[] foto = Base64.decode(catedratico.getFoto().getBytes(), Base64.DEFAULT);
                            Bitmap bitMapFoto = BitmapFactory.decodeByteArray(foto, 0, foto.length);
                            imgCatedratico.setImageBitmap(bitMapFoto);
                        }else {
                            imgCatedratico.setImageResource(R.drawable.img_default);
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

    private void listarIntegrantes(){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String token = informacionSession.getString("token","");
        String url = RestApiMehotds.ApiPostMembers;
        JSONObject postData = new JSONObject();
        try {
            postData.put("grupoid", mParam1);
            postData.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                listadoFotos = new ArrayList<FotografiaUsuario>();
                try {
                    if(response.getString("status").equalsIgnoreCase("1")) {
                        JSONArray grupoArrayJSON = response.getJSONArray("data");

                        for (int i = 0; i < grupoArrayJSON.length(); i++) {
                            JSONObject grupoObject = grupoArrayJSON.getJSONObject(i);
                            Alumno alumno = new Alumno();
                            alumno.setId(grupoObject.getString("USUARIOID"));
                            alumno.setNombres(grupoObject.getString("ALUMNO"));
                            alumno.setFoto(grupoObject.getString("FOTO"));

                            Bitmap bitmap;
                            if(alumno.getFoto().equalsIgnoreCase("null") == false) {
                                byte[] foto = Base64.decode(alumno.getFoto().getBytes(), Base64.DEFAULT);
                                bitmap = BitmapFactory.decodeByteArray(foto, 0, foto.length);
                            }else {
                                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_default);
                            }
                            FotografiaUsuario fotografia = new FotografiaUsuario(alumno.getId(), bitmap, alumno.getNombres());
                            listadoFotos.add(fotografia);
                        }

                        Adaptador adp = new Adaptador(getContext(), R.layout.imgtxt_view_items, listadoFotos );
                        listadoIntegrantes.setAdapter(adp);

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