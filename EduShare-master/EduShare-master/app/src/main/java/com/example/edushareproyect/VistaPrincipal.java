package com.example.edushareproyect;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.edushareproyect.databinding.ActivityVistaPrincipalBinding;

public class VistaPrincipal extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityVistaPrincipalBinding binding;

    String token;
    Integer perfilID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        validarSesion();

        binding = ActivityVistaPrincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarVistaPrincipal.toolbar);
        binding.appBarVistaPrincipal.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent fileUp = new Intent(getApplicationContext(), FileUpload.class);
                startActivity(fileUp);
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_vista_principal);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.vista_principal, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_vista_principal);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    //-----------------------------------------------------------------------------------------------------------------------//
    private void validarSesion(){
        SharedPreferences informacionSession = getSharedPreferences("session", Context.MODE_PRIVATE);
        String token = informacionSession.getString("token","");
        Integer perfilID = informacionSession.getInt("perfilID",0);
        Boolean active = informacionSession.getBoolean("active",false);
        if(!active){
            Intent vistaP = new Intent(getApplicationContext(), VistaPrincipal.class);
            startActivity(vistaP);
        }
    }

    //-----------------------------------------------------------------------------------------------------------------------//
}