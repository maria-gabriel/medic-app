package com.example.usuario.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

    //Ventana para visualizar ficha médica de paciente
public class Ficha2 extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener{
    public EditText curp,fechana,sang,gen,estad,naci,fam,paren,tele;
    public String LoadCurp,LoadFe,LoadSan,LoadGen,LoadEst,LoadNa,LoadFam,LoadParen,LoadTel;
    public String Correo;
    TextView txt;
    RequestQueue rq;
    JsonRequest jrq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ficha2);
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

        curp = (EditText) findViewById(R.id.curp);
        fechana = (EditText) findViewById(R.id.fechana);
        sang = (EditText) findViewById(R.id.sang);
        gen = (EditText) findViewById(R.id.gen);
        estad = (EditText) findViewById(R.id.estad);
        naci = (EditText) findViewById(R.id.naci);
        fam = (EditText) findViewById(R.id.fam);
        paren = (EditText) findViewById(R.id.paren);
        tele = (EditText) findViewById(R.id.tele);
        txt = (TextView) findViewById(R.id.txt);
        rq= Volley.newRequestQueue(this);
        String IP = getString(R.string.ip);
        String url = "http://"+IP+"/android/obtenerF.php?Correo="+Correo;
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

    //Función para ir a la ventana de citas
    public void Citas(View view) {
        finish();
        Intent intent = new Intent(this, Citas2.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    //Función para ir a la ventana de configuración
    public void Config(View view) {
        finish();
        Intent intent = new Intent(this, Config2.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    //Función en caso de que la petición para obtener la ficha no se completo correctamente
    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(),"Error al cargar datos de ficha médica",Toast.LENGTH_SHORT).show();
    }

    //Función en caso de que la petición para obtener la ficha se completo correctamente
    @Override
    public void onResponse(JSONObject response) {
        JSONArray jsonArray = response.optJSONArray("datos");
        JSONObject jsonObject = null;
        try{
            jsonObject = jsonArray.getJSONObject(0);
            LoadCurp = jsonObject.optString("Curp");
            LoadFe = jsonObject.optString("FechaNac");
            LoadSan = jsonObject.optString("TipoSan");
            LoadGen = jsonObject.optString("Genero");
            LoadEst = jsonObject.optString("EstadoCiv");
            LoadNa = jsonObject.optString("Nacionalidad");
            LoadFam = jsonObject.optString("Familiar");
            LoadParen = jsonObject.optString("Parent");
            LoadTel = jsonObject.optString("Telefon");
            rellenar();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Función para poner los datos en los campos
    private void rellenar() {
        curp.setText("Curp: "+LoadCurp);
        fechana.setText("Fecha de nacimiento: "+LoadFe);
        sang.setText("Tipo de sangre: "+LoadSan);
        gen.setText("Género: "+LoadGen);
        estad.setText("Estado civil: "+LoadEst);
        naci.setText("Nacionalidad: "+LoadNa);
        fam.setText("Familiar a cargo: "+LoadFam);
        paren.setText("Parentesco: "+LoadParen);
        tele.setText("Teléfono: "+LoadTel);
    }

    //Función para mandar a la ventana de observaciones médicas
    public void Ver(View view) {
        finish();
        Intent intent = new Intent(this, Ficha4.class);
        intent.putExtra("Correo",Correo);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

}
