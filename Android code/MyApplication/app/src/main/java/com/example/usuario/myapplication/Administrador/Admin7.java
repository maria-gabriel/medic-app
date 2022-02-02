package com.example.usuario.myapplication.Administrador;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.usuario.myapplication.Config3;
import com.example.usuario.myapplication.Inicio3;
import com.example.usuario.myapplication.R;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

//Ventana para visualizacón de citas de los pacientes al mes
public class Admin7 extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener{
    String Lid,Lname,Lname2,Lname3,Lemail,Lpass1;
    RequestQueue rq;
    JsonRequest jrq;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin7);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.laun);

        result = (TextView) findViewById(R.id.paci);
        result.setMovementMethod(new ScrollingMovementMethod());
        rq= Volley.newRequestQueue(this);
        new Admin7.Enviar(Admin7.this).execute();
        String IP = getString(R.string.ip);
        //Manda petición para obtener datos de la base de datos
        String url = "http://"+IP+"/android/reporte4.php";
        jrq=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        rq.add(jrq);
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

    //Función hacer respaldo de BD
    public void Reporte4(View view) {
        finish();
        Intent intent = new Intent(this, Config3.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

        //Función para validar la insercción de las citas
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
                        }
                    });
                }else{
                    contexto.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                }
                return null;
            }
        }

    //Función para insertar el número de citas en el mes actual
    public boolean Insertar(){
        HttpClient httpClient;
        List<NameValuePair> nameValuePairs;
        HttpPost httpPost;
        httpClient = new DefaultHttpClient();
        String IP = getString(R.string.ip);
        httpPost = new HttpPost("http://"+IP+"/android/reporte3.php");
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

    //Función en caso de que la petición para obtener los datos no se completo correctamente
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(),"Error al cargar los datos",Toast.LENGTH_SHORT).show();
    }

    //Funcion en caso de que la peticón para obtener los datos se completo correctamente
    public void onResponse(JSONObject response) {
        result.setText("");
        JSONArray jsonArray = response.optJSONArray("datos");
        JSONObject jsonObject = null;
        try{
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                Lid = jsonObject.optString("Id_Paciente");
                Lname = jsonObject.optString("Nombre");
                Lname2 = jsonObject.optString("ApellidoP");
                Lname3 = jsonObject.optString("ApellidoM");
                Lemail = jsonObject.optString("Correo");
                Lpass1 = jsonObject.optString("NumCitas");

                result.append("\nID Paciente: "+Lid+"\n"+"Nombre: "+Lname+" "+Lname2+" "+Lname3+"\nCorreo: "+Lemail+
                        "\nNúmero de citas: " + Lpass1);
                result.append("\n___________________________________\n");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
