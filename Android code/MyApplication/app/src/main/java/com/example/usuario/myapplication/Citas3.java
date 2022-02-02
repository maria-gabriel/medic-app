package com.example.usuario.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.usuario.myapplication.Clases.Appointment;

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

    //Ventana para visualizar la cita seleccionada por el paciente
public class Citas3 extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener{
    EditText ed1,ed2,ed3,ed4,ed8,ed9,ed10;
    TextView tv1,tv3;
    String ID,LoadNom,LoadApep,LoadTel,LoadCor;
    Button b2;
    RequestQueue rq;
    JsonRequest jrq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.citas3);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.laun);

        tv1=(TextView)findViewById(R.id.texto);
        tv3=(TextView)findViewById(R.id.texto3);
        b2=(Button)findViewById(R.id.call);
        ed1=(EditText)findViewById(R.id.ed1);
        ed2=(EditText)findViewById(R.id.ed2);
        ed3=(EditText)findViewById(R.id.ed3);
        ed4=(EditText)findViewById(R.id.ed4);
        ed8=(EditText)findViewById(R.id.ed8);
        ed9=(EditText)findViewById(R.id.ed9);
        ed10=(EditText)findViewById(R.id.ed10);
        rq= Volley.newRequestQueue(this);
        ed9.setVisibility(View.INVISIBLE);
        ed10.setVisibility(View.INVISIBLE);
        b2.setVisibility(View.INVISIBLE);
        Bundle objetoEnviado=getIntent().getExtras();
        Appointment cita=null;

        if(objetoEnviado!=null){
            cita= (Appointment) objetoEnviado.getSerializable("cita");
            tv1.setText("Cita: "+cita.getId_Cita());
            ID= String.valueOf(cita.getId_Cita());
            ed1.setText("Hora: "+cita.getHora().toString());
            ed2.setText("Fecha: "+cita.getFecha().toString());
            ed3.setText("Sintomas: "+cita.getSintomas().toString());
            ed4.setText("Estado: "+cita.getEstado().toString());

        }

        String IP = getString(R.string.ip);
        String url = "http://"+IP+"/android/asignar2.php?Id_Cita="+ID;
        jrq=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        rq.add(jrq);
    }

    //Función para ir a la ventana de inicio
    public void Inicio(View view) {
        finish();
        Intent intent = new Intent(this, Inicio2.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    //Función para ir a la ventana de fichas médicas
    public void Ficha(View view) {
        finish();
        Intent intent = new Intent(this, Ficha2.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    //Función para ir a la ventana de citas
    public void Citas() {
        finish();
        Intent intent = new Intent(this, Citas2.class);
        startActivity(intent);
    }

    //Función para llamar al doctor
    public void Llamar(View view) {

        Uri num = Uri.parse("tel:" + LoadTel);
        Intent i = new Intent(Intent.ACTION_CALL, num);
        if (ActivityCompat.checkSelfPermission(Citas3.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Citas3.this, new String[]{Manifest.permission.CALL_PHONE}, 100);
        }else{
            startActivity(i);
        }

    }

    //Función para regresar
    public void Regresar(View view) {
        finish();
    }

    //Función para validar la cancelación de una cita
    public void Cancelacion(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Seguro que desea cancelar su cita?")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new Enviar3(Citas3.this).execute();
                    }
                });
        builder.setNegativeButton("Cancelar", null);
        AlertDialog alert = builder.create();
        alert.show();
    }

    //Función para validar si la petición de cancelación se realizó correctamente
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
                        Toast.makeText(getApplicationContext(),"Tu cita ha sido cancelada",Toast.LENGTH_SHORT).show();
                        Citas();
                    }
                });
            }else{
                contexto.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Error al cancelar cita",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }
    }

    //Función para cancelar una cita
    public boolean Insertar3(){
        HttpClient httpClient;
        List<NameValuePair> nameValuePairs;
        HttpPost httpPost;
        httpClient = new DefaultHttpClient();
        String IP = getString(R.string.ip);
        httpPost = new HttpPost("http://"+IP+"/android/cancelar.php?Id_Cita="+ID);
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

    //Función en caso de que la petición para obtener médico asignado no se completo correctamente
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(),"No tiene médico asignado aun",Toast.LENGTH_SHORT).show();
        ed8.setText("Médico no asiganado");
    }

    //Función en caso de que la petición para obtener médico asignado se completo correctamente
    public void onResponse(JSONObject response) {
        JSONArray jsonArray = response.optJSONArray("datos");
        JSONObject jsonObject = null;
        ed9.setVisibility(View.VISIBLE);
        ed10.setVisibility(View.VISIBLE);
        b2.setVisibility(View.VISIBLE);
        try{
            jsonObject = jsonArray.getJSONObject(0);
            LoadNom = jsonObject.optString("Nombre");
            LoadApep = jsonObject.optString("ApellidoP");
            LoadTel = jsonObject.optString("Telefono");
            LoadCor = jsonObject.optString("Correo");

            ed8.setText(LoadNom+" "+LoadApep);
            ed9.setText("Teléfono: "+LoadTel);
            ed10.setText("Correo: "+LoadCor);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
