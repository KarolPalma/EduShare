package com.example.edushareproyect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class FileUpload extends AppCompatActivity {

    private static final int CREATE_FILE = 1;
    private static final int PICK_FILE = 2;

    byte[] byteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_upload);

        Button btnFileSelect = (Button) findViewById(R.id.btnSelectFile);
        btnFileSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFile(Uri.parse("/storage"));
            }
        });





    }

    private void openFile(Uri urlInicial){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");

        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, urlInicial);
        startActivityForResult(intent, PICK_FILE);
    }

    //-----------------------------------------------------------------------------------------------------------------------//
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE && resultCode == RESULT_OK) {
                //getBytes(data);

                Log.d("sasd",data.toString());
            ClipData cpdata = data.getClipData();
            Uri tmpUri = data.getData();
            Log.d("uri ",tmpUri.toString());

        }
    }
    //-----------------------------------------------------------------------------------------------------------------------//


    //-----------------------------------------------------------------------------------------------------------------------//
    private void getBytes(Intent data) {
        File file = (File) data.getExtras().get("data");
        //FotoUser.setImageBitmap(photo);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        /*photo.compress(Bitmap.CompressFormat.PNG, 100, stream);*/

        byteArray = stream.toByteArray();

        //SETEO DE DATOS EN EL OBJETO (FOTO BASE64 Y NOMBRE DEL ARCHIVO)
        String encode = Base64.encodeToString(byteArray, Base64.DEFAULT);
        //alumno.setFoto(encode);
        Log.d("Bytes: ",encode);

    }
    //----------------------------------------------------------------------------------------------------------------------//





}