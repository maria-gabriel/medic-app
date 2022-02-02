package com.example.usuario.myapplication.Administrador;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.usuario.myapplication.Inicio3;
import com.example.usuario.myapplication.R;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

//Ventana para gestión de citas
public class Admin4 extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {
    EditText sin,hora,fecha,lat,lon,ref,pac,nuevo,ide,id;
    String Lid,Lsin,Lh,Lf,Llat,Llon,Lref,Lpac,Lest;
    RelativeLayout create,read,update,delete;
    Spinner campo;
    RequestQueue rq;
    JsonRequest jrq;
    Button b1,b2,b3,b4;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin4);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.laun);

        create = (RelativeLayout) findViewById(R.id.create);
        read = (RelativeLayout) findViewById(R.id.read);
        update = (RelativeLayout) findViewById(R.id.update);
        delete = (RelativeLayout) findViewById(R.id.delete);
        b1 = (Button) findViewById(R.id.button1);
        b2 = (Button) findViewById(R.id.button2);
        b3 = (Button) findViewById(R.id.button3);
        b4 = (Button) findViewById(R.id.button4);
        b1.setBackgroundColor(0xFF818080);
        nuevo = (EditText)findViewById(R.id.nuevo);
        ide = (EditText)findViewById(R.id.ide);
        id = (EditText)findViewById(R.id.id);
        sin = (EditText)findViewById(R.id.sin);
        hora = (EditText)findViewById(R.id.hora);
        fecha = (EditText)findViewById(R.id.fecha);
        lat = (EditText)findViewById(R.id.lat);
        lon = (EditText)findViewById(R.id.lon);
        ref = (EditText)findViewById(R.id.ref);
        pac = (EditText)findViewById(R.id.pac);
        result = (TextView) findViewById(R.id.paci);
        result.setMovementMethod(new ScrollingMovementMethod());
        rq= Volley.newRequestQueue(this);
        create.setVisibility(View.VISIBLE);
        read.setVisibility(View.INVISIBLE);
        update.setVisibility(View.INVISIBLE);
        delete.setVisibility(View.INVISIBLE);
        campo = (Spinner) findViewById(R.id.sp);
        String[] Campo = {"Campo","Sintomas","Hora","Fecha","Latitud","Longitud","Referencias","Estado","Patien"};
        campo.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_item,Campo));

    }

    //Función para regresar al menú de administrador
    public void Admin(View view) {
        finish();
        Intent intent = new Intent(this, Inicio3.class);
        startActivity(intent);
    }

    //Función para registrar nueva cita
    public void Registrar(View view) {
        if(sin.getText().length()==0 || hora.getText().length()==0 || fecha.getText().length()==0 ||  lat.getText().length()==0 || lon.getText().length()==0 ||
                ref.getText().length()==0 || pac.getText().length()==0 ){
            Toast.makeText(getApplicationContext(), "Llene todos los campos", Toast.LENGTH_SHORT).show();
        }else{
                new Enviar(Admin4.this).execute();
        }
    }

    //Función para habilitar la ventana de creación
    public void crear(View view) {
        b1.setBackgroundColor(0xFF818080);
        b2.setBackgroundColor(0xFF969595);
        b3.setBackgroundColor(0xFF969595);
        b4.setBackgroundColor(0xFF969595);
        create.setVisibility(View.VISIBLE);
        read.setVisibility(View.INVISIBLE);
        update.setVisibility(View.INVISIBLE);
        delete.setVisibility(View.INVISIBLE);
    }

    //Función para habilitar la ventana de lectura
    public void leer(View view) {
        b1.setBackgroundColor(0xFF969595);
        b2.setBackgroundColor(0xFF818080);
        b3.setBackgroundColor(0xFF969595);
        b4.setBackgroundColor(0xFF969595);
        read.setVisibility(View.VISIBLE);
        create.setVisibility(View.INVISIBLE);
        update.setVisibility(View.INVISIBLE);
        delete.setVisibility(View.INVISIBLE);
        String IP = getString(R.string.ip);
        String url = "http://"+IP+"/android/obtenerCT.php";
        jrq=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        rq.add(jrq);
    }

    //Función para habilitar la ventana de modificación
    public void Modificar(View view) {
        b1.setBackgroundColor(0xFF969595);
        b2.setBackgroundColor(0xFF969595);
        b3.setBackgroundColor(0xFF818080);
        b4.setBackgroundColor(0xFF969595);
        read.setVisibility(View.INVISIBLE);
        create.setVisibility(View.INVISIBLE);
        update.setVisibility(View.VISIBLE);
        delete.setVisibility(View.INVISIBLE);
    }

    //Función para habilitar la ventana de eliminación
    public void Delete(View view) {
        b1.setBackgroundColor(0xFF969595);
        b2.setBackgroundColor(0xFF969595);
        b3.setBackgroundColor(0xFF969595);
        b4.setBackgroundColor(0xFF818080);
        read.setVisibility(View.INVISIBLE);
        create.setVisibility(View.INVISIBLE);
        delete.setVisibility(View.VISIBLE);
        update.setVisibility(View.INVISIBLE);
    }

    //Función para validar que los datos para modificar esten completos
    public void Modifi(View view) {
        if(nuevo.getText().length()==0 || ide.getText().length()==0){
            Toast.makeText(getApplicationContext(),"Llene todos los campos",Toast.LENGTH_SHORT).show();
        }else{
            if(campo.getSelectedItem().toString().equals("Campo")){
                Toast.makeText(getApplicationContext(),"Elija campo a modificar",Toast.LENGTH_SHORT).show();
            }else{
                new Enviar2(Admin4.this).execute();
            }
        }
    }

    //Función para validar que los datos para eliminar esten completos
    public void Eliminar(View view) {
        if(id.getText().length()==0){
            Toast.makeText(getApplicationContext(),"Ingrese ID de cita",Toast.LENGTH_SHORT).show();
        }else{
            new Enviar3(Admin4.this).execute();
        }
    }

    //Función para validar si la petición de creación se realizó correctamente
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
                        Toast.makeText(getApplicationContext(),"Cita creada",Toast.LENGTH_SHORT).show();
                        sin.setText("");
                        hora.setText("");
                        fecha.setText("");
                        lat.setText("");
                        lon.setText("");
                        ref.setText("");
                        pac.setText("");
                    }
                });
            }else{
                contexto.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Hubo un error de creación",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }
    }

    //Función para validar si la petición de modificación se realizó correctamente
    public class Enviar2 extends AsyncTask<String,String,String> {
        private Activity contexto;
        Enviar2 (Activity context){
            this.contexto = context;
        }
        @Override
        protected String doInBackground(String... strings) {
            if(Insertar2()){
                contexto.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Campo modificado",Toast.LENGTH_SHORT).show();
                        nuevo.setText("");
                        ide.setText("");
                        campo.setSelection(0);
                    }
                });
            }else{
                contexto.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Hubo un error de modificación",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }
    }

    //Función para validar si la petición de eliminación se realizó correctamente
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
                        Toast.makeText(getApplicationContext(),"La cita ha sido eliminada",Toast.LENGTH_SHORT).show();
                        id.setText("");
                    }
                });
            }else{
                contexto.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Hubo un error de eliminación",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }
    }

    //Función para mandar los datos necesarios para crear una nueva cita
    public boolean Insertar(){
        HttpClient httpClient;
        List<NameValuePair> nameValuePairs;
        HttpPost httpPost;
        httpClient = new DefaultHttpClient();
        String IP = getString(R.string.ip);
        httpPost = new HttpPost("http://"+IP+"/android/insertarC.php");
        nameValuePairs = new ArrayList<>(11);

        nameValuePairs.add(new BasicNameValuePair("Sintomas",sin.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("Hora",hora.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("Fecha",fecha.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("Latitud",lat.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("Longitud",lon.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("Referencias",ref.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("Patien",pac.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("Estado","Pendiente"));

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

    //Función para mandar los datos necesarios para modificar una cita
    public boolean Insertar2(){
        HttpClient httpClient;
        List<NameValuePair> nameValuePairs;
        HttpPost httpPost;
        httpClient = new DefaultHttpClient();
        String IP = getString(R.string.ip);
        httpPost = new HttpPost("http://"+IP+"/android/modificar4.php?Id="+ide.getText()+"&Campo="+campo.getSelectedItem());
        nameValuePairs = new ArrayList<>(4);

        nameValuePairs.add(new BasicNameValuePair("Valor",nuevo.getText().toString()));

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

    //Función para mandar los datos necesarios para eliminar una cita
    public boolean Insertar3(){
        HttpClient httpClient;
        List<NameValuePair> nameValuePairs;
        HttpPost httpPost;
        httpClient = new DefaultHttpClient();
        String IP = getString(R.string.ip);
        httpPost = new HttpPost("http://"+IP+"/android/eliminar4.php?Id="+id.getText());
        nameValuePairs = new ArrayList<>(4);

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

    //Función en caso de que la petición para obtener las citas no se completo correctamente
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(),"Error al cargar citas",Toast.LENGTH_SHORT).show();
    }

    //Función en caso de que la petición para obtener las citas se completo correctamente
    public void onResponse(JSONObject response) {
        result.setText("");
        JSONArray jsonArray = response.optJSONArray("datos");
        JSONObject jsonObject = null;
        try{
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                Lid = jsonObject.optString("Id_Cita");
                Lsin = jsonObject.optString("Sintomas");
                Lh = jsonObject.optString("Hora");
                Lf = jsonObject.optString("Fecha");
                Llat = jsonObject.optString("Latitud");
                Llon = jsonObject.optString("Longitud");
                Lref = jsonObject.optString("Referencias");
                Lest = jsonObject.optString("Estado");
                Lpac = jsonObject.optString("Patien");
                result.append("ID Ficha: "+Lid+"\n"+"Sintomas: "+Lsin+"\nHora: "+Lh+
                        "\nFecha: "+Lf + "\nLatitud: "+Llat+ "\nLongitud: " + Llon+ "\nReferencias: " + Lref
                        + "\nEstado: " + Lest+ "\nPaciente: " + Lpac);
                result.append("\n___________________________________\n\n");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
