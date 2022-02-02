package com.example.usuario.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

    //Ventana para registro de paciente
public class Registro1 extends AppCompatActivity {
    EditText name,name2,name3,email,edad,tel,pass1,pass2;
    Spinner spinner;
    Button btn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro1);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.laun);

        //Método para validar la conexión a internet
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
        } else {
            Toasty.error(getApplicationContext(), "Sin conexión a internet", Toast.LENGTH_SHORT, true).show();
        }

        name = (EditText)findViewById(R.id.name);
        name2 = (EditText)findViewById(R.id.name2);
        name3 = (EditText)findViewById(R.id.name3);
        email = (EditText)findViewById(R.id.email);
        edad = (EditText)findViewById(R.id.edad);
        tel = (EditText)findViewById(R.id.tel);
        pass1 = (EditText)findViewById(R.id.pass1);
        pass2 = (EditText)findViewById(R.id.pass2);
        btn = (Button)findViewById(R.id.cancel);

    }

    //Función para ir a la ventana de registro de médicos
    public void Registrar2 (View view) {
        finish();
        Intent intent = new Intent(this, Registro2.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    //Función para cancelar registro
    public void Cancelar (View view) {
        finish();
    }

    //Función para validar registro
    public void Registrarme(View view) {

        if(name.getText().length()==0 || name2.getText().length()==0 || name3.getText().length()==0 ||  email.getText().length()==0 || edad.getText().length()==0 ||
                tel.getText().length()==0 || pass1.getText().length()==0 || pass2.getText().length()==0 ){
            Toast.makeText(getApplicationContext(), "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
        }else{
            Pattern Plantilla = null;
            Matcher Resultado = null;
            Plantilla = Pattern.compile("^([0-9a-zA-Z]([_.w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-w]*[0-9a-zA-Z].)+([a-zA-Z]{2,9}.)+[a-zA-Z]{2,3})$");
            String em = email.getText().toString();
            Resultado = Plantilla.matcher(em);
            if(Resultado.find()==true){
                if(pass1.getText().toString().equals(pass2.getText().toString())){
                    new Enviar(Registro1.this).execute();
            }else{
                    Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getApplicationContext(), "E-mail no válido", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Función para validar si la petición de creación se realizó correctamente
    public class Enviar extends AsyncTask<String,String,String>{

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
                        Toast.makeText(getApplicationContext(),"Tu registro ha sido realizado",Toast.LENGTH_SHORT).show();
                        btn.callOnClick();
                    }
                });
            }else{
                contexto.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Hubo un error de registro",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }
    }

    //Función para crear paciente
    public boolean Insertar(){
        HttpClient httpClient;
        List<NameValuePair> nameValuePairs;
        HttpPost httpPost;
        httpClient = new DefaultHttpClient();
        String IP = getString(R.string.ip);
        httpPost = new HttpPost("http://"+IP+"/android/insertar.php");
        nameValuePairs = new ArrayList<>(11);

        nameValuePairs.add(new BasicNameValuePair("Nombre",name.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("ApellidoP",name2.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("ApellidoM",name3.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("Correo",email.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("Telefono",tel.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("Edad",edad.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("Contra",pass1.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("Token","null"));
        nameValuePairs.add(new BasicNameValuePair("Tipo","Paciente"));

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
