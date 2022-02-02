package com.example.usuario.myapplication;

//Universidad Politécnica del estado de Morelos
//Proyecto de Estancia II: "Medical Appointments"
//Equipo de desarrollo: Gabriel Romero Maria Arely y Suarez Gil Jose Guadalupe
//Grado y grupo: 7°C - Carrera: Informática - Turno: Vespertino
//Asesor: Miguel Angel Ruiz Jaimes

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

    //Ventana principal de la aplicación
public class Index extends AppCompatActivity {
    String Correo,Tipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.laun);

        Context context = this;
        SharedPreferences shar = getSharedPreferences("USER_SESION",context.MODE_PRIVATE);
        Correo = shar.getString("CorreoUs","null");
        Context context2 = this;
        SharedPreferences shar2 = getSharedPreferences("USER_TYPE",context2.MODE_PRIVATE);
        Tipo = shar2.getString("Type","null");

        //Método para validar la conexión a internet
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
        } else {
            Toasty.error(getApplicationContext(), "Sin conexión a internet", Toast.LENGTH_SHORT, true).show();
        }

        if(Tipo!=""){
            if(Tipo.equals("Medico")){
                finish();
                Intent intent = new Intent(this, Inicio1.class);
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
            }else{
                if(Tipo.equals("Paciente")) {
                    finish();
                    Intent intent = new Intent(this, Inicio2.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
                }else{
                    if(Tipo.equals("Admin")) {
                        finish();
                        Intent intent = new Intent(this, Inicio3.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
                    }
                }
            }

        }

    }

    //Función para ir a la ventana de login
    public void Ingresar (View view) {
        finish();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
    }

    //Función para ir a la ventana de registro
    public void Registrar (View view) {
        Intent intent = new Intent(this, Registro1.class);
        startActivity(intent);
    }

}
