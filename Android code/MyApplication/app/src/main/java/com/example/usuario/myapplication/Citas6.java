package com.example.usuario.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.usuario.myapplication.Clases.Appointment2;

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
import java.util.Locale;

    //Ventana para visualizar la cita seleccionada por el médico en las citas asignadas
public class Citas6 extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {
    EditText ed1, ed2, ed3, ed4, ed5, ed6, ed7, ed8, d1, d2, d3, d4;
    RelativeLayout r1, r2;
    LinearLayout r3;
    RequestQueue rq;
    JsonRequest jrq;
    TextView tv1;
    String ID, Correo,Fech, LoadNom, LoadApep, LoadApem, LoadEdad, LoadTel, LoadCor, LoadL, LoadLo, latid,longi;
    Button b2;
    WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.citas6);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.laun);

        Context context = this;
        SharedPreferences shar = getSharedPreferences("USER_SESION", context.MODE_PRIVATE);
        Correo = shar.getString("CorreoUs", "null");
        web = (WebView) findViewById(R.id.webView1);
        tv1 = (TextView) findViewById(R.id.texto);
        b2 = (Button) findViewById(R.id.back);
        d1 = (EditText) findViewById(R.id.d1);
        d2 = (EditText) findViewById(R.id.d2);
        d3 = (EditText) findViewById(R.id.d3);
        d4 = (EditText) findViewById(R.id.d4);
        ed1 = (EditText) findViewById(R.id.ed1);
        ed2 = (EditText) findViewById(R.id.ed2);
        //ed3=(EditText)findViewById(R.id.ed3);
        ed4 = (EditText) findViewById(R.id.ed4);
        ed5 = (EditText) findViewById(R.id.ed5);
        ed6 = (EditText) findViewById(R.id.ed6);
        ed7 = (EditText) findViewById(R.id.ed7);
        r1 = (RelativeLayout) findViewById(R.id.r1);
        r2 = (RelativeLayout) findViewById(R.id.r2);
        r3 = (LinearLayout) findViewById(R.id.linear2);
        r1.setVisibility(View.VISIBLE);
        r2.setVisibility(View.INVISIBLE);
        r3.setVisibility(View.INVISIBLE);
        rq = Volley.newRequestQueue(this);

        Bundle objetoEnviado = getIntent().getExtras();
        Appointment2 cita = null;

        if (objetoEnviado != null) {
            cita = (Appointment2) objetoEnviado.getSerializable("cita");
            tv1.setText("Cita: " + cita.getId_Cita());
            ID = String.valueOf(cita.getId_Cita());
            ed1.setText("Hora: " + cita.getHora().toString());
            ed2.setText("Fecha: " + cita.getFecha().toString());
            Fech = String.valueOf(cita.getFecha());
            ed6.setText("" + cita.getEstado().toString());
            ed7.setText("Referencias: " + cita.getReferencias().toString());
            LoadL=cita.getLatitud();
            LoadLo=cita.getLongitud();

        }

        String IP = getString(R.string.ip);
        String url = "http://" + IP + "/android/obtenerC4.php?Id_Cita=" + ID;
        jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        rq.add(jrq);
        locationStart();

    }

    //Función para regresar a la ventana citas
    public void Regresar(View view) {
        finish();
    }

    //Función para visualizar la ventana de datos de la cita
    public void Cita(View view) {
        r1.setVisibility(View.VISIBLE);
        r2.setVisibility(View.INVISIBLE);
        r3.setVisibility(View.INVISIBLE);
    }

    //Función para visualizar la ventana de datos del paciente
    public void Paciente(View view) {
        r1.setVisibility(View.INVISIBLE);
        r2.setVisibility(View.VISIBLE);
        r3.setVisibility(View.INVISIBLE);
    }

    //Función para obtener la ubicación del médico
    public void Ubicacion(View view) {

        if(latid==null || longi==null){
            Toast.makeText(getApplicationContext(),"Error al cargar ruta. Intente de nuevo",Toast.LENGTH_SHORT).show();

        }else {
            r1.setVisibility(View.INVISIBLE);
            r2.setVisibility(View.INVISIBLE);
            r3.setVisibility(View.VISIBLE);

            web.setWebViewClient(new WebViewClient());
            web.getSettings().setJavaScriptEnabled(true);
            web.loadUrl("http://maps.google.com/maps?" + "saddr=" + latid + "," + longi + "&daddr=" + LoadL + "," + LoadLo);
        }
    }

    //Función para llamar al paciente
    public void Llamar(View view) {

        Uri num = Uri.parse("tel:" + LoadTel);
        Intent i = new Intent(Intent.ACTION_CALL, num);
        if (ActivityCompat.checkSelfPermission(Citas6.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Citas6.this, new String[]{Manifest.permission.CALL_PHONE}, 100);
        }else{
            startActivity(i);
        }

    }

    //Función en caso de que la petición para obtener los datos del paciente no se completo correctamente
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(),"Error al cargar datos de paciente",Toast.LENGTH_SHORT).show();
        r1.setVisibility(View.VISIBLE);
        r2.setVisibility(View.INVISIBLE);
    }

    //Función en caso de que la petición para obtener los datos del paciente se completo correctamente
    public void onResponse(JSONObject response) {
        JSONArray jsonArray = response.optJSONArray("datos");
        JSONObject jsonObject = null;
        try{
            jsonObject = jsonArray.getJSONObject(0);
            LoadNom = jsonObject.optString("Nombre");
            LoadApep = jsonObject.optString("ApellidoP");
            LoadApem = jsonObject.optString("ApellidoM");
            LoadEdad = jsonObject.optString("Edad");
            LoadTel = jsonObject.optString("Telefono");
            LoadCor = jsonObject.optString("Correo");
            String nom=LoadNom+" "+LoadApep+" "+LoadApem;
            d1.setText(nom);
            d2.setText("Edad: "+LoadEdad);
            d3.setText("Correo: " +LoadCor);
            d4.setText("Contacto: "+LoadTel);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Función para validar la cancelación de la cita
    public void Cancelacion(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Seguro que desea cancelar la cita?")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new Enviar3(Citas6.this).execute();
                    }
                });
        builder.setNegativeButton("Cancelar", null);
        AlertDialog alert = builder.create();
        alert.show();
    }

    //Función para ir a la ventana de citas
    public void Citas(){
        finish();
        Intent intent = new Intent(this, Citas1.class);
        startActivity(intent);
    }

    //Función para mandar a la ventana de observaciones médicas
    public void Agregar(View view) {
        Intent intent = new Intent(this, Ficha3.class);
        intent.putExtra("CorreoM",Correo);
        intent.putExtra("CorreoP",LoadCor);
        intent.putExtra("Cita",ID);
        intent.putExtra("Fecha",Fech);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
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
                        Toast.makeText(getApplicationContext(),"La cita ha sido cancelada",Toast.LENGTH_SHORT).show();
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

    //Función para cancelar la cita
    public boolean Insertar3(){
        HttpClient httpClient;
        List<NameValuePair> nameValuePairs;
        HttpPost httpPost;
        httpClient = new DefaultHttpClient();
        String IP = getString(R.string.ip);
        httpPost = new HttpPost("http://"+IP+"/android/cancelar2.php?Id_Cita="+ID);
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

    //Función para validar la finalización de la cita
    public void Finalizar(View view) {
        Context context = this;
        SharedPreferences shar = getSharedPreferences("CITA",context.MODE_PRIVATE);
        String boo = shar.getString("BOOL","false");
        if(boo.toString().equals("true")) {
            new Enviar2(Citas6.this).execute();
            Context context2 = this;
            SharedPreferences shar2 = getSharedPreferences("CITA",context2.MODE_PRIVATE);
            SharedPreferences.Editor editor2 = shar2.edit();
            editor2.putString("BOOL","false");
            editor2.commit();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Para finalizar la cita debe agregar observaciones al paciente.")
                    .setTitle("Aviso")
                    .setPositiveButton("Aceptar",null);
            AlertDialog alert = builder.create();
            alert.show();
        }

    }

    //Función para validar si la petición de finalización se realizó correctamente
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
                        Toast.makeText(getApplicationContext(),"La cita ha sido finalizada",Toast.LENGTH_SHORT).show();
                        Citas();
                    }
                });
            }else{
                contexto.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Error al finalizar cita",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }
    }

    //Función para finalizar la cita
    public boolean Insertar2(){
        HttpClient httpClient;
        List<NameValuePair> nameValuePairs;
        HttpPost httpPost;
        httpClient = new DefaultHttpClient();
        String IP = getString(R.string.ip);
        httpPost = new HttpPost("http://"+IP+"/android/finalizar.php?Id_Cita="+ID);
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

    //Función para obtener ubicación
    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setMainActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Citas6.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
            return;
        }

        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);

    }

    //Función para obtener ubicación
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }

    //Función para obtener ubicación
    public void setLocation(Location loc) {
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            latid= String.valueOf(loc.getLatitude());
            longi=String.valueOf(loc.getLongitude());
            Log.e("",latid);
            Log.e("",longi);
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Función para obtener ubicación
    public class Localizacion implements LocationListener {
        Citas6 mainActivity;
        public Citas6 getMainActivity() {
            return mainActivity;
        }
        public void setMainActivity(Citas6 mainActivity) {
            this.mainActivity = mainActivity;
        }
        @Override
        public void onLocationChanged(Location loc) {
            loc.getLatitude();  //Datos de la longitud y latitud para la Base de datos
            loc.getLongitude();
            this.mainActivity.setLocation(loc);
        }
        @Override
        public void onProviderDisabled(String provider) {
            //Toast.makeText(getApplicationContext(),"GPS Inhabilitado",Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onProviderEnabled(String provider) {
            //Toast.makeText(getApplicationContext(),"GPS Habilitado",Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    }
}
