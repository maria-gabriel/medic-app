package com.example.usuario.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.usuario.myapplication.Clases.Appointment2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

    //Ventana para visualizar las citas del médico
public class Citas5 extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener{
    RequestQueue rq;
    JsonRequest jrq;
    ListView listViewCitas;
    String LoadSin,LoadH,LoadF,LoadEst,LoadName,LoadName2,LoadId,LoadCor,LoadL,LoadLo,LoadR,LoadTel;
    ArrayList<String> listaInformacion;
    ArrayList<Appointment2> listaCitas;
    String Correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.citas5);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.laun);
        Context context = this;
        SharedPreferences shar = getSharedPreferences("USER_SESION",context.MODE_PRIVATE);
        Correo = shar.getString("CorreoUs","null");

        rq= Volley.newRequestQueue(this);
        listViewCitas= (ListView) findViewById(R.id.lista);
        String IP = getString(R.string.ip);
        String url = "http://"+IP+"/android/obtenerC3.php?Correo="+Correo;
        jrq=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        rq.add(jrq);
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

    //Función para ir a la ventana de configuración
    public void Config(View view) {
        finish();
        Intent intent = new Intent(this, Config1.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    //Función para regresar a la ventana de citas
    public void Regresar(View view) {
        finish();
        Intent intent = new Intent(this, Citas1.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    //Función en caso de que la petición para obtener las citas no se completo correctamente
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(),"Error al cargar las citas",Toast.LENGTH_SHORT).show();
    }

    //Función en caso de que la petición para obtener las citas se completo correctamente
    public void onResponse(JSONObject response) {
        JSONArray jsonArray = response.optJSONArray("datos");
        JSONObject jsonObject = null;
        Appointment2 appointment=null;
        listaCitas=new ArrayList<Appointment2>();
        try{
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                LoadId = jsonObject.optString("Id_Cita");
                LoadSin = jsonObject.optString("Sintomas");
                LoadH = jsonObject.optString("Hora");
                LoadF = jsonObject.optString("Fecha");
                LoadEst = jsonObject.optString("Estado");
                LoadL = jsonObject.optString("Latitud");
                LoadLo = jsonObject.optString("Longitud");
                LoadR = jsonObject.optString("Referencias");
                String nom = LoadName+" "+LoadName2;

                appointment = new Appointment2();
                appointment.setId_Cita(Integer.parseInt(LoadId));
                appointment.setSintomas(LoadSin);
                appointment.setHora(LoadH);
                appointment.setFecha(LoadF);
                appointment.setEstado(LoadEst);
                appointment.setLatitud(LoadL);
                appointment.setLongitud(LoadLo);
                appointment.setReferencias(LoadR);

                listaCitas.add(appointment);

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

        //Función para ver la cita seleccionada
        listViewCitas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                String informacion="Id: "+listaCitas.get(pos).getId_Cita()+"\n";
                informacion+="Sintomas: "+listaCitas.get(pos).getSintomas()+"\n";
                informacion+="Hora: "+listaCitas.get(pos).getHora()+"\n";
                informacion+="Fecha: "+listaCitas.get(pos).getFecha()+"\n";
                informacion+="Estado: "+listaCitas.get(pos).getEstado()+"\n";
                informacion+="Latitud: "+listaCitas.get(pos).getLatitud()+"\n";
                informacion+="Longitud: "+listaCitas.get(pos).getLongitud()+"\n";
                informacion+="Referencias: "+listaCitas.get(pos).getReferencias()+"\n";

                Appointment2 cita=listaCitas.get(pos);
                Intent intent=new Intent(Citas5.this,Citas6.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("cita",cita);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }
}
