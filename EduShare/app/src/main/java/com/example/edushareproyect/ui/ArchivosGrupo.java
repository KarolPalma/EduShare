package com.example.edushareproyect.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.edushareproyect.R;
import com.example.edushareproyect.databinding.FragmentHomeBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArchivosGrupo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArchivosGrupo extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mParam3;

    private FragmentHomeBinding binding;
    TextView titulo;

    public ArchivosGrupo(String id, String grupo, String codigo) {
        // Required empty public constructor
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
     * @param param3 Parameter 3.
     * @return A new instance of fragment ArchivosGrupo.
     */
    // TODO: Rename and change types and number of parameters
    public static ArchivosGrupo newInstance(String param1, String param2, String param3) {
        ArchivosGrupo fragment = new ArchivosGrupo(param1, param2, param3);
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
            mParam3 = getArguments().getString(ARG_PARAM3);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_archivos_grupo, container, false);
        titulo = (TextView) root.findViewById(R.id.tituloClase);
        titulo.setText(mParam2);

        ImageView btnInfo = (ImageView) root.findViewById(R.id.btnInfo);
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mParam1 != null) {
                    //ID, NOMBRE, CODIGO
                    Fragment InformacionGrupo = new InfoGrupo(mParam1,mParam2,mParam3);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.nav_host_fragment_content_vista_principal, InformacionGrupo);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });

        return root;
    }
}