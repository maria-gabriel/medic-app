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

    //Ventana para gestión de médicos
public class Admin2 extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {
    EditText name,name2,name3,email,edad,tel,pass1,pass2,nuevo,ide,id;
    String Lid,Lname,Lname2,Lname3,Lemail,Ledad,Ltel,Lpass1;
    RelativeLayout create,read,update,delete;
    Spinner campo;
    RequestQueue rq;
    JsonRequest jrq;
    Button b1,b2,b3,b4;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin2);
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
        id = (EditText)findViewById(R.id.id);
        ide = (EditText)findViewById(R.id.ide);
        nuevo = (EditText)findViewById(R.id.nuevo);
        name = (EditText)findViewById(R.id.name);
        name2 = (EditText)findViewById(R.id.name2);
        name3 = (EditText)findViewById(R.id.name3);
        email = (EditText)findViewById(R.id.email);
        edad = (EditText)findViewById(R.id.edad);
        tel = (EditText)findViewById(R.id.tel);
        pass1 = (EditText)findViewById(R.id.pass1);
        result = (TextView) findViewById(R.id.paci);
        result.setMovementMethod(new ScrollingMovementMethod());
        rq= Volley.newRequestQueue(this);
        create.setVisibility(View.VISIBLE);
        read.setVisibility(View.INVISIBLE);
        update.setVisibility(View.INVISIBLE);
        delete.setVisibility(View.INVISIBLE);
        campo = (Spinner) findViewById(R.id.sp);
        String[] Campo = {"Campo","Nombre","ApellidoP","ApellidoM","Correo","Cedula","Telefono","Contra"};
        campo.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_item,Campo));

    }

    //Función para regresar al menú de administrador
    public void Admin(View view) {
        finish();
        Intent intent = new Intent(this, Inicio3.class);
        startActivity(intent);
    }

    //Función para registrar nuevo doctor
    public void Registrar(View view) {

        if(name.getText().length()==0 || name2.getText().length()==0 || name3.getText().length()==0 ||  email.getText().length()==0 || edad.getText().length()==0 ||
                tel.getText().length()==0 || pass1.getText().length()==0 ){
            Toast.makeText(getApplicationContext(), "Llene todos los campos", Toast.LENGTH_SHORT).show();
        }else{
            Pattern Plantilla = null;
            Matcher Resultado = null;
            Plantilla = Pattern.compile("^([0-9a-zA-Z]([_.w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-w]*[0-9a-zA-Z].)+([a-zA-Z]{2,9}.)+[a-zA-Z]{2,3})$");
            String em = email.getText().toString();
            Resultado = Plantilla.matcher(em);
            if(Resultado.find()==true){
                new Enviar(Admin2.this).execute();
            }else{
                Toast.makeText(getApplicationContext(), "E-mail no válido", Toast.LENGTH_SHORT).show();
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
        String url = "http://"+IP+"/android/obtenerDT.php";
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
                new Enviar2(Admin2.this).execute();
            }
        }
    }

    //Función para validar que los datos para eliminar esten completos
    public void Eliminar(View view) {
        if(id.getText().length()==0){
            Toast.makeText(getApplicationContext(),"Ingrese ID de médico",Toast.LENGTH_SHORT).show();
        }else{
            new Enviar3(Admin2.this).execute();
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
                        Toast.makeText(getApplicationContext(),"Médico creado",Toast.LENGTH_SHORT).show();
                        name.setText("");
                        name2.setText("");
                        name3.setText("");
                        email.setText("");
                        tel.setText("");
                        edad.setText("");
                        pass1.setText("");
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
                        Toast.makeText(getApplicationContext(),"El médico ha sido eliminado",Toast.LENGTH_SHORT).show();
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

    //Función para mandar los datos necesarios para crear un nuevo doctor
    public boolean Insertar(){
        HttpClient httpClient;
        List<NameValuePair> nameValuePairs;
        HttpPost httpPost;
        httpClient = new DefaultHttpClient();
        String IP = getString(R.string.ip);
        httpPost = new HttpPost("http://"+IP+"/android/insertar2.php");
        nameValuePairs = new ArrayList<>(11);

        nameValuePairs.add(new BasicNameValuePair("Nombre",name.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("ApellidoP",name2.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("ApellidoM",name3.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("Correo",email.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("Telefono",tel.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("Cedula",edad.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("Contra",pass1.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("Token","null"));
        nameValuePairs.add(new BasicNameValuePair("Tipo","Medico"));

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

    //Función para mandar los datos necesarios para modificar un doctor
    public boolean Insertar2(){
        HttpClient httpClient;
        List<NameValuePair> nameValuePairs;
        HttpPost httpPost;
        httpClient = new DefaultHttpClient();
        String IP = getString(R.string.ip);
        httpPost = new HttpPost("http://"+IP+"/android/modificar2.php?Id="+ide.getText()+"&Campo="+campo.getSelectedItem());
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

    //Función para mandar los datos necesarios para eliminar un doctor
    public boolean Insertar3(){
        HttpClient httpClient;
        List<NameValuePair> nameValuePairs;
        HttpPost httpPost;
        httpClient = new DefaultHttpClient();
        String IP = getString(R.string.ip);
        httpPost = new HttpPost("http://"+IP+"/android/eliminar2.php?Id="+id.getText());
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

    //Función en caso de que la petición para obtener los médicos no se completo correctamente
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(),"Error al cargar médicos",Toast.LENGTH_SHORT).show();
    }

    //Funcion en caso de que la peticón para obtener los médicos se completo correctamente
    public void onResponse(JSONObject response) {
        result.setText("");
        JSONArray jsonArray = response.optJSONArray("datos");
        JSONObject jsonObject = null;
        try{
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                Lid = jsonObject.optString("Id_Medico");
                Lname = jsonObject.optString("Nombre");
                Lname2 = jsonObject.optString("ApellidoP");
                Lname3 = jsonObject.optString("ApellidoM");
                Lemail = jsonObject.optString("Correo");
                Ledad = jsonObject.optString("Cedula");
                Ltel = jsonObject.optString("Telefono");
                Lpass1 = jsonObject.optString("Contra");
                result.append("ID Médico: "+Lid+"\n"+"Nombre: "+Lname+" "+Lname2+" "+Lname3+"\nCorreo: "+Lemail+
                        "\nCédula: "+Ledad + "\nTelefono: "+Ltel+ "\nContraseña: " + Lpass1);
                result.append("\n___________________________________\n\n");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
