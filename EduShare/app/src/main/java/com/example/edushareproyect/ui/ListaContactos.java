package com.example.edushareproyect.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.edushareproyect.Adaptadores.Adaptador;
import com.example.edushareproyect.Objetos.Amigo;
import com.example.edushareproyect.Objetos.FotografiaUsuario;
import com.example.edushareproyect.R;
import com.example.edushareproyect.RestApiMehotds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListaContactos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListaContactos extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ArrayList<FotografiaUsuario> listadoFotos;
    ListView listadoAmigos;
    SharedPreferences informacionSession;

    public ListaContactos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListaContactos.
     */
    // TODO: Rename and change types and number of parameters
    public static ListaContactos newInstance(String param1, String param2) {
        ListaContactos fragment = new ListaContactos();
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
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_lista_contactos, container, false);

        listadoAmigos = (ListView) root.findViewById(R.id.listadoAmigos);
        informacionSession = root.getContext().getSharedPreferences("session", Context.MODE_PRIVATE);

        listarAmigos();

        listadoAmigos.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                String idAmigo = listadoFotos.get(position).getId();

                Fragment verContacto = new VerContacto(idAmigo);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_content_vista_principal, verContacto);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return root;
    }

    private void listarAmigos(){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = RestApiMehotds.ApiPostFriends;
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
                listadoFotos = new ArrayList<FotografiaUsuario>();
                try {
                    if(response.getString("status").equalsIgnoreCase("1")) {
                        JSONArray amigoArrayJSON = response.getJSONArray("data");

                        for (int i = 0; i < amigoArrayJSON.length(); i++) {
                            JSONObject grupoObject = amigoArrayJSON.getJSONObject(i);
                            Amigo amigo = new Amigo();
                            amigo.setUsuarioid(grupoObject.getString("USUARIOID"));
                            amigo.setNombres(grupoObject.getString("CONTACTO"));
                            amigo.setFoto(grupoObject.getString("FOTO_URL"));

                            Bitmap bitmap;
                            if(amigo.getFoto().equalsIgnoreCase("null") == false) {
                                byte[] foto = Base64.decode(amigo.getFoto().getBytes(), Base64.DEFAULT);
                                bitmap = BitmapFactory.decodeByteArray(foto, 0, foto.length);
                            }else {
                                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_default);
                            }
                            FotografiaUsuario fotografia = new FotografiaUsuario(amigo.getUsuarioid(), bitmap, amigo.getNombres());
                            listadoFotos.add(fotografia);
                        }

                        Adaptador adp = new Adaptador(getContext(), R.layout.imgtxt_view_items, listadoFotos );
                        listadoAmigos.setAdapter(adp);

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