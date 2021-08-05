package com.example.edushareproyect.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.edushareproyect.AgregarGrupo;
import com.example.edushareproyect.CrearGrupo;
import com.example.edushareproyect.MainActivity;
import com.example.edushareproyect.Objetos.Grupo;
import com.example.edushareproyect.R;
import com.example.edushareproyect.RestApiMehotds;
import com.example.edushareproyect.databinding.FragmentHomeBinding;
import com.example.edushareproyect.ui.ArchivosGrupo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    ListView listadoGrupos;
    ArrayList<Grupo> arregloGrupos;
    ArrayList<String> arregloNombresGrupos;

    SharedPreferences session;


    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        /*Recuperar el token de la session*/
        //session = this.getActivity().getSharedPreferences("session",Context.MODE_PRIVATE);
        SharedPreferences session = root.getContext().getSharedPreferences("session", Context.MODE_PRIVATE);
        String token = session.getString("token","");
        Integer perfil = session.getInt("perfil",0);

        if(token.isEmpty()){
            Intent inicio = new Intent(this.getActivity().getApplicationContext(), MainActivity.class);
            startActivity(inicio);
        }

        String url = RestApiMehotds.ApiGruposUser;
        listadoGrupos = (ListView) root.findViewById(R.id.ListviewGrupos);
        Button btnAddGrupo = (Button) root.findViewById(R.id.btnOpenAgregarGrupo);
        Log.d("PerfilID: ",perfil.toString());
        /*Verificacion de perfil*/
        if(perfil==1){
            /*estudiante*/

        }else{
            /*Catedratico*/
            TextView titulo = root.findViewById(R.id.tituloGrupos);
            titulo.setText("Mis grupos");
            btnAddGrupo.setText("Crear Grupo Nuevo");
        }



        btnAddGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addGrupo = new Intent(root.getContext(), AgregarGrupo.class);
                startActivity(addGrupo);

                /*if(perfil==1){

                    Intent addGrupo = new Intent(root.getContext(), AgregarGrupo.class);
                    startActivity(addGrupo);

                }else{

                    Intent addGrupo = new Intent(root.getContext(), CrearGrupo.class);
                    startActivity(addGrupo);
                }*/

            }
        });

        /*Peticion de datos y creacion del listview*/
        //----------------------------------------------------------------------------------------//
        arregloGrupos = new ArrayList<Grupo>();
        arregloNombresGrupos = new ArrayList<String>();
        RequestQueue queue = Volley.newRequestQueue(root.getContext());

        JSONObject postData = new JSONObject();
        try {
            postData.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                VolleyLog.e(response.toString());
                try {
                    if(response.getString("status").equalsIgnoreCase("1")) {
                        JSONArray grupoArrayJSON = response.getJSONArray("data");

                        if(grupoArrayJSON.length()>=1) {

                            /*llenamos la lista con los datos de los grupos registrados*/
                            for (int i = 0; i < grupoArrayJSON.length(); i++) {
                                JSONObject grupoObject = grupoArrayJSON.getJSONObject(i);


                                Grupo grupo = new Grupo(grupoObject.getString("GRUPOID"),
                                        grupoObject.getString("GRUPO"),
                                        grupoObject.getString("CODIGO"),
                                        grupoObject.getString("USUARIOID"));

                                arregloGrupos.add(grupo);
                                arregloNombresGrupos.add((i+1) + "- " +grupo.getNombre());
                            }

                            ArrayAdapter<String> adp = new ArrayAdapter<String>(root.getContext(), android.R.layout.simple_list_item_1, arregloNombresGrupos);
                            listadoGrupos.setAdapter(adp);
                        }else{
                            /*No hay grupos registrados, mostramos un boton para agregar*/

                        }
                    }else{
                        Toast.makeText(root.getContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException ex) {
                    Toast.makeText(root.getContext(), "Excepción en Response", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error en Response", "onResponse: " +  error.getMessage().toString() );
            }
        });
        queue.add(jsonObjectRequest);
        //----------------------------------------------------------------------------------------//

        listadoGrupos.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                String idGrupo = arregloGrupos.get(position).getId();
                String nombreGrupo = arregloGrupos.get(position).getNombre();
                String codigoGrupo = arregloGrupos.get(position).getCodigo();
                /*Intent grupoEspecifico = new Intent(root.getContext(), ArchivosGrupo.class);
                grupoEspecifico.putExtra("idGrupo", idGrupo);
                grupoEspecifico.putExtra("nombreGrupo", nombreGrupo);
                grupoEspecifico.putExtra("codigoGrupo", codigoGrupo);
                startActivity(grupoEspecifico);*/

                //Abrir fragmento nuevo
                Fragment Archivos = new ArchivosGrupo(idGrupo,nombreGrupo,codigoGrupo);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_content_vista_principal, Archivos);

                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



    //----------------------------------------------------------------------------------------------------------------------------------------------//
    private void listarGruposUsuario(String token){

    }
    //----------------------------------------------------------------------------------------------------------------------------------------------//


}