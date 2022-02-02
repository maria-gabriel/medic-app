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
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

    //Ventana para agregar observaciones médicas
public class Ficha3 extends AppCompatActivity {
    Spinner tipo;
    EditText diag,pres,s1,s2,s3,s4;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ficha3);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.laun);

        diag = (EditText)findViewById(R.id.diag);
        pres = (EditText)findViewById(R.id.pres);
        s1 = (EditText)findViewById(R.id.sig1);
        s2 = (EditText)findViewById(R.id.sig2);
        s3 = (EditText)findViewById(R.id.sig3);
        s4 = (EditText)findViewById(R.id.sig4);
        text = (TextView) findViewById(R.id.salto);
        SpannableString mitextoU = new SpannableString("Cancelar la observación médica");
        mitextoU.setSpan(new UnderlineSpan(), 0, mitextoU.length(), 0);
        text.setText(mitextoU);
        tipo = (Spinner) findViewById(R.id.sp);
        String[] TipoSangre = {"Motivo de consulta","Enfermedad aguda","Enfermedad crónica","Enfermedad mental","Enfermedad inmunológica",
                "Enfermedad endocrina","Enfermedad neurológica","Enfermedad visual","Enfermedad auditiva","Enfermedad respiratoria",
                "Enfermedad cardiovascular","Enfermedad digestiva","Enfermedad dermatológica","Enfermedad locomotora","Enfermedad genitourinaria",
                "Enfermedad infecciosa","Enfermedad traumática","Enfermedad por causa externa","Otro"};
        tipo.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_item,TipoSangre));
    }

    //Función para validar los datos
    public void Guardar(View view) {
        if(diag.getText().length()==0 || pres.getText().length()==0 || tipo.getSelectedItem().toString().equals("Motivo de consulta")){
            Toast.makeText(getApplicationContext(), "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
        }else{
            if(s1.getText().length()==0){
                s1.setText("0");
            }
            if(s2.getText().length()==0){
                s2.setText("0");
            }
            if(s3.getText().length()==0){
                s3.setText("0");
            }
            if(s4.getText().length()==0){
                s4.setText("0");
            }
            new Enviar3(Ficha3.this).execute();
        }
    }

    //Función para validar si la petición de creación se realizó correctamente
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
                        Toast.makeText(getApplicationContext(),"Se ha guardado la observación",Toast.LENGTH_SHORT).show();
                        save();
                        finish();
                    }
                });
            }else{
                contexto.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Error al guardar la observación",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }
    }

    //Función para validar que si se agregaron observaciones al paciente
    public void save(){
        Context context2 = this;
        SharedPreferences shar2 = getSharedPreferences("CITA",context2.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = shar2.edit();
        editor2.putString("BOOL","true");
        editor2.commit();
    }

    public void Regresar(){
        finish();
    }

    //Función para agregar las observaciones
    public boolean Insertar3(){
        HttpClient httpClient;
        List<NameValuePair> nameValuePairs;
        HttpPost httpPost;
        httpClient = new DefaultHttpClient();
        String IP = getString(R.string.ip);
        httpPost = new HttpPost("http://"+IP+"/android/insertarO.php");
        nameValuePairs = new ArrayList<>(2);
        String c1 = getIntent().getExtras().getString("CorreoM");
        String c2 = getIntent().getExtras().getString("CorreoP");
        String id = getIntent().getExtras().getString("Cita");
        String fe = getIntent().getExtras().getString("Fecha");

        nameValuePairs.add(new BasicNameValuePair("Correo",c1));
        nameValuePairs.add(new BasicNameValuePair("Correo2",c2));
        nameValuePairs.add(new BasicNameValuePair("Id_cita",id));
        nameValuePairs.add(new BasicNameValuePair("Diagnostico",diag.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("Prescripcion",pres.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("Presion",s1.getText().toString()+"Hg"));
        nameValuePairs.add(new BasicNameValuePair("Respiracion",s2.getText().toString()+"rpm"));
        nameValuePairs.add(new BasicNameValuePair("Pulso",s3.getText().toString()+"lpm"));
        nameValuePairs.add(new BasicNameValuePair("Temperatura",s4.getText().toString()+"°C"));
        nameValuePairs.add(new BasicNameValuePair("Motivo",tipo.getSelectedItem().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("Fecha",fe));

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

    //Función para cancelar
    public void Saltar(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Las observaciones del paciente son un recurso importante del sistema ¿Está seguro que desea continuar?")
                .setTitle("Confirmación")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Regresar();
                    }
                });
        builder.setNegativeButton("Cancelar", null);
        AlertDialog alert = builder.create();
        alert.show();
    }

}
