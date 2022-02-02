package com.example.usuario.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.usuario.myapplication.Administrador.Admin1;
import com.example.usuario.myapplication.Administrador.Admin2;
import com.example.usuario.myapplication.Administrador.Admin3;
import com.example.usuario.myapplication.Administrador.Admin4;
import com.example.usuario.myapplication.Administrador.Admin5;

//Menu principal de administrador
public class Inicio3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio3);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.laun);
    }

    //Funcion para regresar a la ventana inicial
    public void Terminar(View view) {
        finish();
        Context context = this;
        Intent intent = new Intent(this, Index.class);
        SharedPreferences shar3 = getSharedPreferences("USER_TYPE",context.MODE_PRIVATE);
        SharedPreferences.Editor editor3 = shar3.edit();
        editor3.putString("Type","");
        editor3.commit();
        startActivity(intent);
        overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
    }

    //Función para mandar a la gestión de pacientes
    public void Pacientes(View view) {
        finish();
        Intent intent = new Intent(this, Admin1.class);
        startActivity(intent);
    }

    //Función para mandar a la gestión de doctores
    public void Doctores(View view) {
        finish();
        Intent intent = new Intent(this, Admin2.class);
        startActivity(intent);
    }

    //Función para mandar a la gestión de fichas
    public void Fichas(View view) {
        finish();
        Intent intent = new Intent(this, Admin3.class);
        startActivity(intent);
    }

    //Función para mandar a la gestión de citas
    public void Citas(View view) {
        finish();
        Intent intent = new Intent(this, Admin4.class);
        startActivity(intent);
    }

    //Función para mandar a la ventana de reportes
    public void Reportes(View view) {
        finish();
        Intent intent = new Intent(this, Admin5.class);
        startActivity(intent);
    }
}
