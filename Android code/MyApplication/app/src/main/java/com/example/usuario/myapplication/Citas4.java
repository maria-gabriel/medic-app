package com.example.usuario.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usuario.myapplication.Clases.Appointment2;

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

    //Ventana para visualizar la cita seleccionada por el médico
public class Citas4 extends AppCompatActivity {
    EditText ed1,ed2,ed3,ed4,ed5,ed6,ed7,ed8;
    TextView tv1;
    String ID,Correo;
    Button b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.citas4);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.laun);

        Context context = this;
        SharedPreferences shar = getSharedPreferences("USER_SESION",context.MODE_PRIVATE);
        Correo = shar.getString("CorreoUs","null");
        tv1=(TextView)findViewById(R.id.texto);
        b2=(Button)findViewById(R.id.back);
        ed1=(EditText)findViewById(R.id.ed1);
        ed2=(EditText)findViewById(R.id.ed2);
        ed3=(EditText)findViewById(R.id.ed3);
        ed4=(EditText)findViewById(R.id.ed4);
        ed5=(EditText)findViewById(R.id.ed5);
        ed6=(EditText)findViewById(R.id.ed6);
        ed7=(EditText)findViewById(R.id.ed7);

        Bundle objetoEnviado=getIntent().getExtras();
        Appointment2 cita=null;

        if(objetoEnviado!=null){
            cita= (Appointment2) objetoEnviado.getSerializable("cita");
            tv1.setText("Cita: "+cita.getId_Cita());
            ID= String.valueOf(cita.getId_Cita());
            ed5.setText("Paciente: "+cita.getPaciente().toString());
            ed6.setText("Email: "+cita.getEmail().toString());
            ed1.setText("Hora: "+cita.getHora().toString());
            ed2.setText("Fecha: "+cita.getFecha().toString());
            ed3.setText("Sintomas: "+cita.getSintomas().toString());
            ed4.setText(""+cita.getContacto().toString());
            ed7.setText("Referencias: "+cita.getReferencias().toString());

        }
    }

    //Función para ir a la ventana de inicio
    public void Inicio(View view) {
        finish();
        Intent intent = new Intent(this, Inicio1.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    //Función para ir a la ventana de fichas médicas
    public void Fichas(View view) {
        finish();
        Intent intent = new Intent(this, Ficha1.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    //Función para ir a la ventana de citas
    public void Citas(View view) {
        finish();
        Intent intent = new Intent(this, Citas1.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    //Función para regresar a la ventana citas
    public void Regresar(View view) {
        finish();
    }

    //Función para validar la asignación de la cita al médico
    public void Asignar(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Su cuenta será asignada a esta cita para que acuda a ella. ¿Desea continuar?")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new Enviar3(Citas4.this).execute();
                    }
                });
        builder.setNegativeButton("Cancelar", null);
        AlertDialog alert = builder.create();
        alert.show();
    }

    //Función para validar si la petición de asignación se realizó correctamente
    public class Enviar3 extends AsyncTask<String,String,String> {

        private Activity contexto;
        Enviar3 (Activity context){
            this.contexto = context;
        }
        @Override
        protected String doInBackground(String... strings) {
            if(Insertar3()){
                contexto.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Tu cita a sido asignada",Toast.LENGTH_SHORT).show();
                        b2.callOnClick();
                    }
                });
            }else{
                contexto.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Error al asignar cita",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }
    }

    //Función para asignar la cita seleccionada al médico
    public boolean Insertar3(){
        HttpClient httpClient;
        List<NameValuePair> nameValuePairs;
        HttpPost httpPost;
        httpClient = new DefaultHttpClient();
        String IP = getString(R.string.ip);
        Log.e("",Correo);
        httpPost = new HttpPost("http://"+IP+"/android/asignar.php?Id_Cita="+ID+"&Correo="+Correo);
        nameValuePairs = new ArrayList<>(2);
        Log.e("",ID);

        //nameValuePairs.add(new BasicNameValuePair("Id_cita",eli.getText().toString()));

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
