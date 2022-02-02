package com.example.usuario.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.usuario.myapplication.Administrador.Admin5;
import com.example.usuario.myapplication.Administrador.Admin6;
import com.example.usuario.myapplication.Administrador.Admin7;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Config3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config3);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.laun);

    }

    //Función para regresar al menú de administrador
    public void Admin(View view) {
        finish();
        Intent intent = new Intent(this, Inicio3.class);
        startActivity(intent);
    }

    //Función ir al reporte de total de citas de médicos
    public void Reporte1(View view) {
        finish();
        Intent intent = new Intent(this, Admin5.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    //Función ir al reporte de motivos de citas
    public void Reporte2(View view) {
        finish();
        Intent intent = new Intent(this, Admin6.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    //Función ir al reporte de citas de pacientes
    public void Reporte3(View view) {
        finish();
        Intent intent = new Intent(this, Admin7.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    //Función para realizar el respaldo
    public void Respaldar(View view) {
        new Enviar(Config3.this).execute();
    }

    //Función para validar el respaldo
    public class Enviar extends AsyncTask<String,String,String> {
        private Activity contexto;
        Enviar (Activity context){
            this.contexto = context;
        }
        @Override
        protected String doInBackground(String... strings) {
            if(Insertar()){
                contexto.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Los datos han sido respaldados",Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                contexto.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Error al respaldar los datos",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }
    }

    //Función para respaldar la base de datos
    public boolean Insertar(){
        HttpClient httpClient;
        List<NameValuePair> nameValuePairs;
        HttpPost httpPost;
        httpClient = new DefaultHttpClient();
        String IP = getString(R.string.ip);
        httpPost = new HttpPost("http://"+IP+"/android/reporte5.php");
        nameValuePairs = new ArrayList<>(11);

        try{
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpClient.execute(httpPost);
            return true;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
