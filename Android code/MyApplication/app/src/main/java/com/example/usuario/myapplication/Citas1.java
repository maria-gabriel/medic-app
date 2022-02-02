package com.example.usuario.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

    //Ventana para citas de los médicos
public class Citas1 extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {
    public EditText eli;
    public Button search,fech;
    RequestQueue rq,rq2;
    JsonRequest jrq;
    TextView fecha;
    WebView webview;
    RelativeLayout v1;
    LinearLayout v2;
    public int dia, mes, ano;
    public String CERO = "0", ubication;
    public String LoadSin,LoadH,LoadF,LoadEst,LoadName,LoadName2,LoadId,LoadCor,LoadL,LoadLo,LoadR,LoadTel;
    ListView listViewCitas;
    ArrayList<String> listaInformacion;
    ArrayList<Appointment2> listaCitas;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.citas1);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.laun);

        //Método para validar la conexión a internet
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
        } else {
            Toasty.error(getApplicationContext(), "Sin conexión a internet", Toast.LENGTH_SHORT, true).show();
        }

        webview = (WebView) findViewById(R.id.webView1);
        v1 = (RelativeLayout)findViewById(R.id.linear);
        v2 = (LinearLayout)findViewById(R.id.linear2);
        fecha = (TextView) findViewById(R.id.fecha);
        eli = (EditText)findViewById(R.id.eli2);
        fech = (Button) findViewById(R.id.fech);
        search = (Button) findViewById(R.id.buscar);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 0);
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd");
        String fe = dateformat.format(cal.getTime());
        fecha.setText(fe);
        rq= Volley.newRequestQueue(this);
        rq2= Volley.newRequestQueue(this);
        search.callOnClick();
        listViewCitas= (ListView) findViewById(R.id.lista);
        //result = (TextView) findViewById(R.id.citas);
        //result.setMovementMethod(new ScrollingMovementMethod());
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

    //Función para obtener la fecha
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onClick(View v) {
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
                    fecha.setText(year + "/" + m + "/" + d);
                    search.callOnClick();

                }
            }, ano, mes, dia);
            datepickerdialog.show();
        }

    //Función para buscar la cita a traves de la fecha
    public void Buscar(View view) {
        if(fecha.getText().length()==0){
            Toast.makeText(getApplicationContext(),"Ingrese una fecha",Toast.LENGTH_SHORT).show();
        }else {
            String IP = getString(R.string.ip);
            String url = "http://" + IP + "/android/obtenerC2.php?Fecha="+ fecha.getText().toString();
            jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
            rq.add(jrq);
        }
    }

        @Override
        //Función en caso de que la petición para obtener las citas no se completo correctamente
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getApplicationContext(),"No se encontraron citas",Toast.LENGTH_SHORT).show();
            listViewCitas.setVisibility(View.INVISIBLE);
        }

        //Funcion en caso de que la peticón para obtener las citas se completo correctamente
        public void onResponse(JSONObject response) {
            //result.setText("");
            listViewCitas.setVisibility(View.VISIBLE);
            JSONArray jsonArray = response.optJSONArray("datos");
            JSONObject jsonObject = null;
            Appointment2 appointment=null;
            listaCitas=new ArrayList<Appointment2>();
            try{
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    LoadId = jsonObject.optString("Id_Cita");
                    LoadName = jsonObject.optString("Nombre");
                    LoadName2 = jsonObject.optString("ApellidoP");
                    LoadCor = jsonObject.optString("Correo");
                    LoadTel = jsonObject.optString("Telefono");
                    LoadSin = jsonObject.optString("Sintomas");
                    LoadH = jsonObject.optString("Hora");
                    LoadF = jsonObject.optString("Fecha");
                    LoadEst = jsonObject.optString("Estado");
                    LoadL = jsonObject.optString("Latitud");
                    LoadLo = jsonObject.optString("Longitud");
                    LoadR = jsonObject.optString("Referencias");
                    setLocation(LoadL,LoadLo);
                    String nom = LoadName+" "+LoadName2;

                    appointment = new Appointment2();
                    appointment.setId_Cita(Integer.parseInt(LoadId));
                    appointment.setPaciente(nom);
                    appointment.setEmail(LoadCor);
                    appointment.setContacto(ubication);
                    appointment.setSintomas(LoadSin);
                    appointment.setHora(LoadH);
                    appointment.setFecha(LoadF);
                    appointment.setEstado(LoadEst);
                    appointment.setLatitud(LoadL);
                    appointment.setLongitud(LoadLo);
                    appointment.setReferencias(LoadR);

                    listaCitas.add(appointment);

                    //result.append("N. Cita: " + LoadId + "\n" + "Paciente: " + LoadName + " " + LoadName2 + "\n" + "Correo: " + LoadCor + "\n" + "Sintomas: " + LoadSin + "\n" + "A las " + LoadH + " el día " + LoadF +
                    //       "\n" + "Estado de cita: " + LoadEst+"\nUbicación:" +ubication+"\nReferencias: "+LoadR);
                    //result.append("\n__________________________________\n\n");
                }
                obtenerLista();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    //Función para regresar a la ventana citas
    public void Regresar(View view) {
        v1.setVisibility(View.VISIBLE);
        v2.setVisibility(View.INVISIBLE);
    }

    //Función para mostrar las citas que tiene asignadas el médico
    public void Mostrar(View view) {
        finish();
        Intent intent = new Intent(this, Citas5.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    //Función para ir a la ventana configuración
    public void Config(View view) {
        finish();
        Intent intent = new Intent(this, Config1.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    /*public void Ver(View view) {
        v1.setVisibility(View.INVISIBLE);
        v2.setVisibility(View.VISIBLE);
        webview.setVisibility(View.VISIBLE);
        webview.setWebViewClient(new WebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);
        String IP = getString(R.string.ip);
        String url = "http://" + IP + "/android/obtenerU.php?Id_Cita="+ eli.getText().toString();
        jrq2 = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        rq2.add(jrq2);

        webview.loadUrl("https://maps.google.com/?q=18.88897429,-99.16699306");
    }*/

    //Función para validar si la petición de asignación se realizó correctamente
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
                        Toast.makeText(getApplicationContext(),"Error al asignar cita",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }
    }

    //Función para asignar la cita al médico
    public boolean Insertar(){
        HttpClient httpClient;
        List<NameValuePair> nameValuePairs;
        HttpPost httpPost;
        httpClient = new DefaultHttpClient();
        String IP = getString(R.string.ip);
        httpPost = new HttpPost("http://"+IP+"/android/obtenerU.php?Id_Cita="+eli.getText());
        nameValuePairs = new ArrayList<>(2);


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


    //Función para obtener ubicación de la cita a través de la longitud y latitud
    public void setLocation(String L, String Lo) {
        double Ld = Double.parseDouble(L);
        double Lod = Double.parseDouble(Lo);
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        Ld, Lod, 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    ubication = (""+ DirCalle.getAddressLine(0));
                }
            } catch (IOException e) {
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
                informacion+="Paciente: "+listaCitas.get(pos).getPaciente()+"\n";
                informacion+="Email: "+listaCitas.get(pos).getEmail()+"\n";
                informacion+="Telefono: "+listaCitas.get(pos).getContacto()+"\n";
                informacion+="Sintomas: "+listaCitas.get(pos).getSintomas()+"\n";
                informacion+="Hora: "+listaCitas.get(pos).getHora()+"\n";
                informacion+="Fecha: "+listaCitas.get(pos).getFecha()+"\n";
                informacion+="Estado: "+listaCitas.get(pos).getEstado()+"\n";
                informacion+="Latitud: "+listaCitas.get(pos).getLatitud()+"\n";
                informacion+="Longitud: "+listaCitas.get(pos).getLongitud()+"\n";
                informacion+="Referencias: "+listaCitas.get(pos).getReferencias()+"\n";

                Appointment2 cita=listaCitas.get(pos);
                Intent intent=new Intent(Citas1.this,Citas4.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("cita",cita);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }
}
