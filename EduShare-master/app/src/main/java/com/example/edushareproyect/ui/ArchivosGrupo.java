package com.example.edushareproyect.ui;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.edushareproyect.Adaptadores.AdapArchivos;
import com.example.edushareproyect.Adaptadores.Adaptador;
import com.example.edushareproyect.Objetos.FotografiaUsuario;
import com.example.edushareproyect.R;
import com.example.edushareproyect.RestApiMehotds;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArchivosGrupo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArchivosGrupo extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String GRUPOID = "GrupoID";
    private static final String GRUPO = "Grupo";
    private static final String CODIGO = "Codigo";

    // TODO: Rename and change types of parameters
    private String mGrupoID;
    private String mGrupo;
    private String mCodigo;


    TextView titulo;

    ArrayList<FotografiaUsuario> ListaArchivos;
    ListView ListViewArchivos;
    SharedPreferences session;

    public ArchivosGrupo() {
        // Required empty public constructor

    }

    public ArchivosGrupo(String id, String grupo, String codigo) {
        // Required empty public constructor
        mGrupoID = id;
        mGrupo = grupo;
        mCodigo = codigo;
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param GrupoID Parameter 1.
     * @param Grupo Parameter 2.
     * @param Codigo Parameter 3.
     * @return A new instance of fragment ArchivosGrupo.
     */
    // TODO: Rename and change types and number of parameters
    public static ArchivosGrupo newInstance(String GrupoID, String Grupo, String Codigo) {
        ArchivosGrupo fragment = new ArchivosGrupo(GrupoID, Grupo, Codigo);
        Bundle args = new Bundle();
        args.putString(GRUPOID, GrupoID);
        args.putString(GRUPO, Grupo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mGrupoID = getArguments().getString(GRUPOID);
            mGrupo = getArguments().getString(GRUPO);
            mCodigo = getArguments().getString(CODIGO);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_archivos_grupo, container, false);
        titulo = (TextView) root.findViewById(R.id.tituloClase);
        titulo.setText(mGrupo);
        session = root.getContext().getSharedPreferences("session", Context.MODE_PRIVATE);

        getFiles(root.getContext());
        ListViewArchivos = (ListView) root.findViewById(R.id.listadoArchivos);

        ListViewArchivos.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){

                String ArchivoID = ListaArchivos.get(position).getId();
                String nombreGrupo = mGrupo;
                Fragment ArchivoDetalle = new ArchivoDetalles(ArchivoID, nombreGrupo);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_content_vista_principal, ArchivoDetalle);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });


        ImageView btnInfo = (ImageView) root.findViewById(R.id.btnInfo);
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mGrupoID != null) {
                    //ID, NOMBRE, CODIGO
                    Fragment InformacionGrupo = new InfoGrupo(mGrupoID,mGrupo,mCodigo);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.nav_host_fragment_content_vista_principal, InformacionGrupo);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });



        return root;
    }

    private void getFiles(Context context){
        ListaArchivos = new ArrayList<FotografiaUsuario>();
        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject req = new JSONObject();
        try{
            req.put("grupoid",mGrupoID);
            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, RestApiMehotds.ApiPOSTListFiles, req, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray ar = response.getJSONArray("response");

                        Bitmap excel = BitmapFactory.decodeResource(getResources(), R.drawable.excel);
                        Bitmap word = BitmapFactory.decodeResource(getResources(), R.drawable.word);
                        Bitmap pp = BitmapFactory.decodeResource(getResources(), R.drawable.powerpoint);
                        Bitmap pdf = BitmapFactory.decodeResource(getResources(), R.drawable.pdf);
                        Bitmap default_file = BitmapFactory.decodeResource(getResources(), R.drawable.default_file);

                        if(ar.length()>=1){
                            for(int i=0;i<ar.length();i++){

                                JSONObject file = ar.getJSONObject(i);
                                String extension = file.getString("EXTENSION");
                                Bitmap img ;
                                switch (extension){
                                    case "application/pdf":
                                        img = pdf;
                                        break;
                                    case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
                                        img = word;
                                        break;
                                    case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet":
                                        img = excel;
                                        break;
                                    case "application/vnd.openxmlformats-officedocument.presentationml.presentation":
                                        img = pp;
                                        break;
                                    default:
                                        img = default_file;
                                                break;
                                }
                                FotografiaUsuario fotografiaUsuario = new FotografiaUsuario(
                                        file.getString("ARCHIVOID"),
                                        img,
                                        file.getString("NOMBRE")
                                );
                                ListaArchivos.add(fotografiaUsuario);
                            }

                            AdapArchivos adp = new AdapArchivos(getContext(), R.layout.adp_archivos, ListaArchivos );
                            ListViewArchivos.setAdapter(adp);

                        }else{
                            mostrarDialogo("Info","No hay archivos en el grupo aun");
                        }

                    }catch (Exception e){
                        mostrarDialogo("Error",e.getMessage());
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mostrarDialogo("error",error.getMessage());
                    Log.d("Error",error.getMessage());
                }
            });

            queue.add(objectRequest);


        }catch(JSONException ex){
            mostrarDialogo("Error","No se pudo realizar la peticion");
            Log.d("Error",ex.getMessage());
            ex.printStackTrace();
        }

    }
    //-----------------------------------------------------------------------------------------------------------------------//
    private void mostrarDialogo(String title, String mensaje) {
        new AlertDialog.Builder(getActivity())
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