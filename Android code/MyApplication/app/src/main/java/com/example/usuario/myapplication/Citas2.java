package com.example.usuario.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
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
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

    //Ventana para citas de los pacientes
public class Citas2 extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {
    private final Handler handler = new Handler();
    private boolean isNetworkLocation, isGPSLocation;
    LocationManager mlocManager;
    RequestQueue rq;
    JsonRequest jrq;
    Button bhora, bfecha, ubicacion;
    EditText sin, ubic2, ubic;
    public int dia, mes, ano, hora, min, ampm;
    public String CERO = "0", latid,longi,ubication,Correo;
    public String LoadSin,LoadH,LoadF,LoadEst,LoadName,LoadName2,LoadId;
    TextView result;
    RelativeLayout v1,v2;
    ListView listViewCitas;
    ArrayList<String> listaInformacion;
    ArrayList<Appointment> listaCitas;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.citas2);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.laun);
        Context context = this;
        SharedPreferences shar = getSharedPreferences("USER_SESION",context.MODE_PRIVATE);
        Correo = shar.getString("CorreoUs","null");

        //Método para validar la conexión a internet
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
        } else {
            Toasty.error(getApplicationContext(), "Sin conexión a internet", Toast.LENGTH_SHORT, true).show();
        }

        v1 = (RelativeLayout) findViewById(R.id.v1);
        v2 = (RelativeLayout) findViewById(R.id.v2);
        ubicacion = (Button) findViewById(R.id.ubicacion);
        sin = (EditText) findViewById(R.id.sinto);
        ubic = (EditText) findViewById(R.id.ubic);
        ubic2 = (EditText) findViewById(R.id.ubic2);
        bhora = (Button) findViewById(R.id.bhora);
        bfecha = (Button) findViewById(R.id.bfecha);
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        rq= Volley.newRequestQueue(this);
        listViewCitas= (ListView) findViewById(R.id.lista);
        v1.setVisibility(View.VISIBLE);
        v2.setVisibility(View.INVISIBLE);

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

    //Función para ir a la ventana de configuración
    public void Config(View view) {
        finish();
        Intent intent = new Intent(this, Config2.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    //Función para ir a la ventana citas
    public void Citas(){
        finish();
        Intent intent = new Intent(this, Citas2.class);
        startActivity(intent);
    }

    //Función para validar que los datos para crear una cita estén completos
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public void Crear(View view) {
        if(sin.getText().length()==0 || ubic.getText().length()==0 || ubic2.getText().length()==0 || longi.length()==0 || latid.length()==0
                || bhora.getText().length()==0 || bfecha.getText().length()==0){
            Toast.makeText(getApplicationContext(), "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
        }else{
            new Enviar(Citas2.this).execute();
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
                        Toast.makeText(getApplicationContext(),"Tu cita se ha creado",Toast.LENGTH_SHORT).show();
                        Citas();
                        new Enviar2(Citas2.this).execute();

                    }
                });
            }else{
                contexto.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Hubo un error al crear cita",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }
    }

    //Función para validar si la petición de asignación se realizó correctamente
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

    //Función para mandar los datos necesarios para crear la cita
    public boolean Insertar(){
        HttpClient httpClient;
        List<NameValuePair> nameValuePairs;
        HttpPost httpPost;
        httpClient = new DefaultHttpClient();
        String IP = getString(R.string.ip);
        httpPost = new HttpPost("http://"+IP+"/android/insertarC.php");
        nameValuePairs = new ArrayList<>(11);

        nameValuePairs.add(new BasicNameValuePair("Sintomas",sin.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("Hora",bhora.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("Fecha",bfecha.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("Latitud",latid));
        nameValuePairs.add(new BasicNameValuePair("Longitud",longi));
        nameValuePairs.add(new BasicNameValuePair("Referencias",ubic2.getText().toString()));
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

    //Función para asignar la cita al paciente
    public boolean Insertar2(){
        HttpClient httpClient;
        List<NameValuePair> nameValuePairs;
        HttpPost httpPost;
        httpClient = new DefaultHttpClient();
        String IP = getString(R.string.ip);
        httpPost = new HttpPost("http://"+IP+"/android/insertarC2.php");
        nameValuePairs = new ArrayList<>(2);

        nameValuePairs.add(new BasicNameValuePair("Correo",Correo.toString()));

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

    //Función para obtener la fecha y hora
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onClick(View v) {
        if (v == bfecha) {
            final Calendar c = Calendar.getInstance();
            dia = c.get(Calendar.DAY_OF_MONTH);
            mes = c.get(Calendar.MONTH);
            ano = c.get(Calendar.YEAR);

            DatePickerDialog datepickerdialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    int mesActual = month + 1;
                    String d = (dayOfMonth < 10) ? CERO + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                    String m = (mesActual < 10) ? CERO + String.valueOf(mesActual) : String.valueOf(mesActual);
                    bfecha.setText(year + "/" + m + "/" + d);
                }
            }, ano, mes, dia);
            datepickerdialog.show();
        }

        if (v == bhora) {
            final Calendar c = Calendar.getInstance();
            hora = c.get(Calendar.HOUR_OF_DAY);
            min = c.get(Calendar.MINUTE);
            ampm = c.get(Calendar.AM_PM);

            TimePickerDialog timepickerdialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    if (hourOfDay < 12) {
                        bhora.setText(hourOfDay + ":" + minute + " AM");
                    } else {
                        bhora.setText(hourOfDay + ":" + minute + " PM");
                    }

                }
            }, hora, min, false);
            timepickerdialog.show();
        }
    }

    //Función para obtener ubicación del paciente
    public void Obtener(View view) {
        locationStart();
        Toast.makeText(getApplicationContext(), "Obteniendo ubicación...", Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), "Espere...", Toast.LENGTH_SHORT).show();
    }

    //Función para obtener ubicación
    private void locationStart() {
        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setMainActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Citas2.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
            return;
        }

        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);

    }

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
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                   ubication = (""+ DirCalle.getAddressLine(0));
                    ubic.setText(ubication);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Función para obtener ubicación
    public class Localizacion implements LocationListener {
        Citas2 mainActivity;
        public Citas2 getMainActivity() {
            return mainActivity;
        }
        public void setMainActivity(Citas2 mainActivity) {
            this.mainActivity = mainActivity;
        }
        @Override
        public void onLocationChanged(Location loc) {
            loc.getLatitude();  //Datos de la longitud y latitud
            loc.getLongitude();
            latid= String.valueOf(loc.getLatitude());
            longi=String.valueOf(loc.getLongitude());
            Log.e("",latid);
            Log.e("",longi);
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

    //Función para mostrar las citas del paciente
    public void Mostrar(View view) {
        v1.setVisibility(View.INVISIBLE);
        v2.setVisibility(View.VISIBLE);
        String IP = getString(R.string.ip);
        String url = "http://"+IP+"/android/obtenerC.php?Correo="+Correo;
        jrq=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        rq.add(jrq);
    }

    //Función para regresar a la ventana de citas
    public void Regresar(View view) {
        v1.setVisibility(View.VISIBLE);
        v2.setVisibility(View.INVISIBLE);
    }

    @Override
    //Función en caso de que la petición para obtener las citas no se completo correctamente
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(),"Error al cargar las citas",Toast.LENGTH_SHORT).show();
    }

    //Funcion en caso de que la peticón para obtener las citas se completo correctamente
    public void onResponse(JSONObject response) {
        JSONArray jsonArray = response.optJSONArray("datos");
        JSONObject jsonObject = null;
        Appointment appointment=null;
        listaCitas=new ArrayList<Appointment>();
        try{
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                LoadId = jsonObject.optString("Id_Cita");
                LoadName = jsonObject.optString("Nombre");
                LoadName2 = jsonObject.optString("ApellidoP");
                LoadSin = jsonObject.optString("Sintomas");
                LoadH = jsonObject.optString("Hora");
                LoadF = jsonObject.optString("Fecha");
                LoadEst = jsonObject.optString("Estado");

                appointment = new Appointment();
                appointment.setId_Cita(Integer.parseInt(LoadId));
                appointment.setSintomas(LoadSin);
                appointment.setHora(LoadH);
                appointment.setFecha(LoadF);
                appointment.setEstado(LoadEst);
                listaCitas.add(appointment);

                //result.append("N. Cita: " + LoadId + "\n" + "Paciente: " + LoadName + " " + LoadName2 + "\n" + "Correo: " + Correo + "\n" + "Sintomas: " + LoadSin + "\n" + "A las " + LoadH + " el día " + LoadF + "\n" + "Estado de cita: " + LoadEst);
                //result.append("\n__________________________________\n\n");
            }
            obtenerLista();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Función para mostrar las citas en una lista
    private void obtenerLista() {
        listaInformacion=new ArrayList<String>();

        for (int i=0; i<listaCitas.size();i++){
            listaInformacion.add("Cita: "+listaCitas.get(i).getId_Cita()+"\nFecha: "+listaCitas.get(i).getFecha()+"\nHora: "
                    +listaCitas.get(i).getHora());
        }

        ArrayAdapter adaptador=new ArrayAdapter(this,R.layout.listita_item,listaInformacion);
        listViewCitas.setAdapter(adaptador);

        //Método para mostrar la cita seleccionada
        listViewCitas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                String informacion="Id: "+listaCitas.get(pos).getId_Cita()+"\n";
                informacion+="Sintomas: "+listaCitas.get(pos).getSintomas()+"\n";
                informacion+="Hora: "+listaCitas.get(pos).getHora()+"\n";
                informacion+="Fecha: "+listaCitas.get(pos).getFecha()+"\n";
                informacion+="Estado: "+listaCitas.get(pos).getEstado()+"\n";

                //Toast.makeText(getApplicationContext(),informacion,Toast.LENGTH_LONG).show();
                Appointment cita=listaCitas.get(pos);
                Intent intent=new Intent(Citas2.this,Citas3.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("cita",cita);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

}
