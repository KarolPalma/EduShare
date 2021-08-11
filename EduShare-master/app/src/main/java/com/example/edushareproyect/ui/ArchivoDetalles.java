package com.example.edushareproyect.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.provider.DocumentsContract;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
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
import com.example.edushareproyect.R;
import com.example.edushareproyect.RestApiMehotds;
import com.example.edushareproyect.VistaPrincipal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArchivoDetalles#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArchivoDetalles extends Fragment {

    private static final int CREATE_FILE = 1;

    TextView txtDetFilename;
    TextView txtDetFileSize;
    TextView txtDetFileDate;
    TextView txtDetFileMail;
    ImageView FileIMG;
    ImageButton btnDownload;
    ImageButton btnDeleteFile;
    static final int PETICION_ACCESO = 100;
    Boolean permisosConcedidos = false;

    SharedPreferences session;

    String Data;
    String FileName;
    String FileExtension;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARCHIVOID = "";
    private static final String GRUPONOMBRE = "";


    // TODO: Rename and change types of parameters
    private String mArchivoID;
    private String mGrupoNombre;


    public ArchivoDetalles() {
        // Required empty public constructor
    }

    public ArchivoDetalles(String id, String grupoNombre){
        this.mArchivoID = id;
        this.mGrupoNombre = grupoNombre;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param archivoID Parameter 1.
     * @return A new instance of fragment ArchivoDetalles.
     */
    // TODO: Rename and change types and number of parameters
    public static ArchivoDetalles newInstance(String archivoID) {
        ArchivoDetalles fragment = new ArchivoDetalles();
        Bundle args = new Bundle();
        args.putString(ARCHIVOID, archivoID);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mArchivoID = getArguments().getString(ARCHIVOID);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_archivo_detalles, container, false);
        session = root.getContext().getSharedPreferences("session", Context.MODE_PRIVATE);

        Integer PerfilID = session.getInt("perfilID",0);


        mostrarDialogo("Este es el id del archivo: ","Hey!!");
        txtDetFilename = root.findViewById(R.id.txtDetFilename);
        txtDetFileSize = root.findViewById(R.id.txtDetFileSize);
        txtDetFileDate = root.findViewById(R.id.txtDetFileDate);
        txtDetFileMail = root.findViewById(R.id.txtDetFileMail);
        FileIMG = root.findViewById(R.id.FileIMG);

        btnDownload = root.findViewById(R.id.btnFileDownload);
        btnDeleteFile = root.findViewById(R.id.btnDeleteFile);

        if(PerfilID != 1 & PerfilID !=2 ){
            //no encontro el perfil
            btnDeleteFile.setEnabled(false);
            btnDeleteFile.setVisibility(View.INVISIBLE);
        }else if(PerfilID == 1){
            //No tiene permisos para eleminar
            btnDeleteFile.setEnabled(false);
            btnDeleteFile.setVisibility(View.INVISIBLE);
        }

        btnDeleteFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarArchivo();
            }
        });

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permisos();
            }
        });

        Bitmap excel = BitmapFactory.decodeResource(getResources(), R.drawable.excel);
        Bitmap word = BitmapFactory.decodeResource(getResources(), R.drawable.word);
        Bitmap pp = BitmapFactory.decodeResource(getResources(), R.drawable.powerpoint);
        Bitmap pdf = BitmapFactory.decodeResource(getResources(), R.drawable.pdf);
        Bitmap default_file = BitmapFactory.decodeResource(getResources(), R.drawable.default_file);

        getDetalles(root.getContext());


        return root;
    }

    private void eliminarArchivo() {
        new AlertDialog.Builder(getContext())
                .setTitle("Confirmación de Eliminación")
                .setMessage("¿Desea eliminar el archivo?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        RequestQueue queue = Volley.newRequestQueue(getContext());
                        String url = RestApiMehotds.ApiPOSTDeleteFile;
                        // String token = informacionSession.getString("token","");
                        JSONObject postData = new JSONObject();
                        try {
                            // postData.put("token", token);
                            postData.put("archivoid", mArchivoID);
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
                        Toast.makeText(getContext(), "Dato Eliminado", Toast.LENGTH_LONG).show();


                    }

                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getContext(), "Se canceló la eliminación", Toast.LENGTH_LONG).show();
                    }
                }).show();
    }

    //-----------------------------------------------------------------------------------------------------------------------//
    private void getDetalles(Context context){
        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject req = new JSONObject();
        try{
            req.put("archivoID",mArchivoID);
            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, RestApiMehotds.ApiPOSTFileDetail, req, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try{
                        JSONObject data = response.getJSONObject("response");
                        String extension = data.getString("EXTENSION");
                        Bitmap excel = BitmapFactory.decodeResource(getResources(), R.drawable.excel);
                        Bitmap word = BitmapFactory.decodeResource(getResources(), R.drawable.word);
                        Bitmap pp = BitmapFactory.decodeResource(getResources(), R.drawable.powerpoint);
                        Bitmap pdf = BitmapFactory.decodeResource(getResources(), R.drawable.pdf);
                        Bitmap default_file = BitmapFactory.decodeResource(getResources(), R.drawable.default_file);

                        Bitmap img ;
                        img = BitmapFactory.decodeResource(getResources(), R.drawable.default_file);
                        String tipo = "";
                        switch (extension){
                            case "application/pdf":
                                img = pdf;
                                tipo = "Archivo de lectura PDF";
                                break;
                            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
                                img = word;
                                tipo = "Documento de Word";
                                break;
                            case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet":
                                img = excel;
                                tipo = "Hojas de calculo Excel";
                                break;
                            case "application/vnd.openxmlformats-officedocument.presentationml.presentation":
                                img = pp;
                                tipo = "Presentacion de PowerPoint";
                                break;
                            default:
                                img = default_file;
                                tipo = data.getString("EXTENSION");
                                break;
                        }

                        txtDetFilename.setText(data.getString("NOMBRE"));
                        txtDetFileSize.setText(tipo);
                        txtDetFileDate.setText("Fecha: "+data.getString("FECHA_CREACION"));
                        txtDetFileMail.setText("De: "+data.getString("CORREO"));

                        FileIMG.setImageBitmap(img);

                        Data = data.getString("DATA");
                        FileName = data.getString("NOMBRE");
                        FileExtension = data.getString("EXTENSION");


                    }catch(Exception e){
                        mostrarDialogo("Error", e.getMessage());
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mostrarDialogo("Error",error.getMessage());

                }
            });

            queue.add(objectRequest);
        }catch(JSONException ex){
            mostrarDialogo("Error",ex.getMessage());
            ex.printStackTrace();
        }
    }
    //-----------------------------------------------------------------------------------------------------------------------//

    //-----------------------------------------------------------------------------------------------------------------------//
    private File CreateFile(String data, String name, String extension) throws IOException {

        byte[] dataEncode = android.util.Base64.decode(data, Base64.DEFAULT);
        File archivo =  new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), name);
        FileOutputStream fileOutputStream;
        try{

            if(archivo.exists()){
                Log.e("uri:",archivo.getPath().toString());
                //mostrarDialogo("Info", "El archivo ya existe");
                //return null;
                archivo.delete();

            }

            fileOutputStream = new FileOutputStream(archivo);
            fileOutputStream.write(dataEncode);


            if(archivo!=null){
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }catch (FileNotFoundException fe){
            mostrarDialogo("Error",fe.getMessage());
            fe.printStackTrace();
            return null;
        }




        return archivo;
    }
    //-----------------------------------------------------------------------------------------------------------------------//


    //-----------------------------------------------------------------------------------------------------------------------//
    private void mostrarDialogo(String title, String mensaje) {
        new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(mensaje)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });


    }
    //-----------------------------------------------------------------------------------------------------------------------//

    //-----------------------------------------------------------------------------------------------------------------------//
    private void confirmacion(String title, String mensaje, File file) {
        new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(mensaje)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        openFile(file);
                    }
                }).show();
    }
    //-----------------------------------------------------------------------------------------------------------------------//


    private void permisos() {
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE }, PETICION_ACCESO);
        }else{

            try{
                File f = CreateFile(Data,FileName,FileExtension);
                if(f.exists()){
                    confirmacion("Informacion","El archivo fue guardado",f);
                }
                //openFile(f);


            }catch (IOException ie){
                mostrarDialogo("Error","No se puede descargar el archivo");
                ie.printStackTrace();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PETICION_ACCESO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                try{
                    File f = CreateFile(Data,FileName,FileExtension);
                    if(f.exists()){
                        confirmacion("Informacion","El archivo fue guardado",f);
                    }
                    //openFile(f);
                }catch (IOException ie){
                    mostrarDialogo("Error","No se puede descargar el archivo");
                    ie.printStackTrace();
                }

            }else{
                mostrarDialogo("Permisos a la App", "Debe conceder permisos de lectura y escritura para usar esta función");
            }
        }


    }


    public void openFile(File file){
        Uri uri = Uri.fromFile(file).normalizeScheme();
        Log.e("DIR",uri.toString());
        String mime = get_mime_type(uri.toString());
        Log.e("tipo",mime);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.setType(mime);
        getActivity().startActivity(Intent.createChooser(intent, "Abrir archivo con:"));
    }

    public String get_mime_type(String url) {
        String ext = MimeTypeMap.getFileExtensionFromUrl(url);
        String mime = null;
        if (ext != null) {
            mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
        }
        return mime;
    }



}