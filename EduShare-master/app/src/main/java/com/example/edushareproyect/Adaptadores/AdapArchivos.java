package com.example.edushareproyect.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.edushareproyect.Objetos.FotografiaUsuario;
import com.example.edushareproyect.R;

import java.util.ArrayList;

public class AdapArchivos extends ArrayAdapter {

    ArrayList<FotografiaUsuario> listado = new ArrayList<>();

    public AdapArchivos(Context context, int textViewResourceId, ArrayList<FotografiaUsuario> objects) {
        super(context, textViewResourceId, objects);
        listado = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.imgtxt_view_items, null);
        ImageView imageView = (ImageView) v.findViewById(R.id.img);
        imageView.setImageBitmap(listado.get(position).getImagen());
        TextView textView = (TextView) v.findViewById(R.id.txt);
        textView.setText(listado.get(position).getNombre());
        return v;
    }
}
