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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

    //Ventana para gestión de fichas médicas
public class Admin3 extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {
    EditText name,tel,curp,fech,san,gen,est,naci,paren,nuevo,ide,id;
    String Lid,Lname,Ltel,Lcurp,Lsan,Lgen,Lest,Lnaci,Lparen,Lfech;
    RelativeLayout create,read,update,delete;
    Spinner campo;
    RequestQueue rq;
    JsonRequest jrq;
    Button b1,b2,b3,b4;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin3);
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
        name = (EditText)findViewById(R.id.name);
        curp = (EditText)findViewById(R.id.curp);
        fech = (EditText)findViewById(R.id.fech);
        san = (EditText)findViewById(R.id.sang);
        gen = (EditText)findViewById(R.id.gen);
        tel = (EditText)findViewById(R.id.tel);
        est = (EditText)findViewById(R.id.est);
        naci = (EditText)findViewById(R.id.naci);
        paren = (EditText)findViewById(R.id.paren);
        result = (TextView) findViewById(R.id.paci);
        result.setMovementMethod(new ScrollingMovementMethod());
        rq= Volley.newRequestQueue(this);
        create.setVisibility(View.VISIBLE);
        read.setVisibility(View.INVISIBLE);
        update.setVisibility(View.INVISIBLE);
        delete.setVisibility(View.INVISIBLE);
        campo = (Spinner) findViewById(R.id.sp);
        String[] Campo = {"Campo","Curp","FechaNac","TipoSan","Genero","EstadoCiv","Nacionalidad","Familiar","Parent","Telefon"};
        campo.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_item,Campo));

    }

    //Función para regresar al menú de administrador
    public void Admin(View view) {
        finish();
        Intent intent = new Intent(this, Inicio3.class);
        startActivity(intent);
    }

    //Función para registrar nueva ficha
    public void Registrar(View view) {
        Pattern Plantilla = null;
        Matcher Resultado = null;
        Plantilla = Pattern.compile("[A-Z]{1}[AEIOU]{1}[A-Z]{2}[0-9]{2}"+"(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])"+"[HM]{1}" +
                "(AS|BC|BS|CC|CS|CH|CL|CM|DF|DG|GT|GR|HG|JC|MC|MN|MS|NT|NL|OC|PL|QT|QR|SP|SL|SR|TC|TS|TL|VZ|YN|ZS|NE)" +
                "[B-DF-HJ-NP-TV-Z]{3}" +"[0-9A-Z]{1}[0-9]{1}$");
        String cur = curp.getText().toString();
        Resultado = Plantilla.matcher(cur);
        if(name.getText().length()==0 || curp.getText().length()==0 || fech.getText().length()==0 ||  san.getText().length()==0 || gen.getText().length()==0 ||
                tel.getText().length()==0 || est.getText().length()==0 ||  naci.getText().length()==0 ||  paren.getText().length()==0){
            Toast.makeText(getApplicationContext(), "Llene todos los campos", Toast.LENGTH_SHORT).show();
        }else{
            if(Resultado.find()==true){
                new Enviar(Admin3.this).execute();
            }else{
                Toast.makeText(getApplicationContext(), "Tamaño de CURP no válido ", Toast.LENGTH_SHORT).show();
            }
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
        String url = "http://"+IP+"/android/obtenerFT.php";
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
                new Enviar2(Admin3.this).execute();
            }
        }
    }

    //Función para validar que los datos para eliminar esten completos
    public void Eliminar(View view) {
        if(id.getText().length()==0){
            Toast.makeText(getApplicationContext(),"Ingrese ID de ficha",Toast.LENGTH_SHORT).show();
        }else{
            new Enviar3(Admin3.this).execute();
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
                        Toast.makeText(getApplicationContext(),"Ficha creada",Toast.LENGTH_SHORT).show();
                        name.setText("");
                        curp.setText("");
                        san.setText("");
                        gen.setText("");
                        tel.setText("");
                        est.setText("");
                        naci.setText("");
                        fech.setText("");
                        paren.setText("");
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
                        Toast.makeText(getApplicationContext(),"La ficha ha sido eliminada",Toast.LENGTH_SHORT).show();
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

    //Función para mandar los datos necesarios para crear una nueva ficha
    public boolean Insertar(){
        HttpClient httpClient;
        List<NameValuePair> nameValuePairs;
        HttpPost httpPost;
        httpClient = new DefaultHttpClient();
        String IP = getString(R.string.ip);
        httpPost = new HttpPost("http://"+IP+"/android/ficha.php");
        nameValuePairs = new ArrayList<>(11);

        nameValuePairs.add(new BasicNameValuePair("Curp",curp.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("FechaNac",fech.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("TipoSan",san.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("Genero",gen.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("EstadoCiv",est.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("Nacionalidad",naci.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("Familiar",name.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("Parent",paren.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("Telefon",tel.getText().toString().trim()));

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

    //Función para mandar los datos necesarios para modificar una ficha
    public boolean Insertar2(){
        HttpClient httpClient;
        List<NameValuePair> nameValuePairs;
        HttpPost httpPost;
        httpClient = new DefaultHttpClient();
        String IP = getString(R.string.ip);
        httpPost = new HttpPost("http://"+IP+"/android/modificar3.php?Id="+ide.getText()+"&Campo="+campo.getSelectedItem());
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

    //Función para mandar los datos necesarios para eliminar una ficha
    public boolean Insertar3(){
        HttpClient httpClient;
        List<NameValuePair> nameValuePairs;
        HttpPost httpPost;
        httpClient = new DefaultHttpClient();
        String IP = getString(R.string.ip);
        httpPost = new HttpPost("http://"+IP+"/android/eliminar3.php?Id="+id.getText());
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

    //Función en caso de que la petición para obtener las fichas no se completo correctamente
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(),"Error al cargar fichas",Toast.LENGTH_SHORT).show();
    }

    //Funcion en caso de que la peticón para obtener las fichas se completo correctamente
    public void onResponse(JSONObject response) {
        result.setText("");
        JSONArray jsonArray = response.optJSONArray("datos");
        JSONObject jsonObject = null;
        try{
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                Lid = jsonObject.optString("Id_Ficha");
                Lcurp = jsonObject.optString("Curp");
                Lfech = jsonObject.optString("FechaNac");
                Lsan = jsonObject.optString("TipoSan");
                Lgen = jsonObject.optString("Genero");
                Lest = jsonObject.optString("EstadoCiv");
                Lnaci = jsonObject.optString("Nacionalidad");
                Lname = jsonObject.optString("Familiar");
                Lparen = jsonObject.optString("Parent");
                Ltel = jsonObject.optString("Telefon");
                result.append("ID Ficha: "+Lid+"\n"+"Curp: "+Lcurp+"\nFecha nacimiento: "+Lfech+
                        "\nTipo de sangre: "+Lsan + "\nGenero: "+Lgen+ "\nEstado civil: " + Lest+ "\nNacionalidad: " + Lnaci
                        + "\nFamiliar a cargo: " + Lname+ "\nParentesco: " + Lparen+ "\nTeléfono: " + Ltel);
                result.append("\n___________________________________\n\n");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
