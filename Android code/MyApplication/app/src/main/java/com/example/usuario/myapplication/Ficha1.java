package com.example.usuario.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

    //Ventana para buscar fichas de pacientes
public class Ficha1 extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {
    RequestQueue rq;
    JsonRequest jrq;
    LinearLayout usu,ficha;
    public Button btn;
    public EditText curp,fechana,sang,gen,estad,naci,fam,paren,tele,name,edad,cont;
    public String LoadCurp,LoadFe,LoadSan,LoadGen,LoadEst,LoadNa,LoadFam,LoadParen,LoadTel,LoadNom,LoadApep,LoadApem,LoadEdad,LoadTel2;
    public EditText Correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ficha1);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.laun);

        //Método para validar la conexión a internet
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
        } else {
            Toasty.error(getApplicationContext(), "Sin conexión a internet", Toast.LENGTH_SHORT, true).show();
        }

        btn = (Button) findViewById(R.id.boton);
        usu = (LinearLayout) findViewById(R.id.usu);
        ficha = (LinearLayout) findViewById(R.id.ficha);
        Correo = (EditText) findViewById(R.id.email);
        name = (EditText) findViewById(R.id.name);
        edad = (EditText) findViewById(R.id.edad);
        cont = (EditText) findViewById(R.id.cont);
        curp = (EditText) findViewById(R.id.curp);
        fechana = (EditText) findViewById(R.id.fechana);
        sang = (EditText) findViewById(R.id.sang);
        estad = (EditText) findViewById(R.id.estad);
        naci = (EditText) findViewById(R.id.naci);
        fam = (EditText) findViewById(R.id.fam);
        paren = (EditText) findViewById(R.id.paren);
        rq= Volley.newRequestQueue(this);
        usu.setVisibility(View.INVISIBLE);
        ficha.setVisibility(View.INVISIBLE);
        btn.setVisibility(View.INVISIBLE);
    }

    //Función para ir a la ventana de citas
    public void Citas(View view) {
        finish();
        Intent intent = new Intent(this, Citas1.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    //Función para ir a la ventana de inicio
    public void Inicio(View view) {
        finish();
        Intent intent = new Intent(this, Inicio1.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    //Función para ir a la ventana de configuración
    public void Config(View view) {
        finish();
        Intent intent = new Intent(this, Config1.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    //Función para validar la busqueda
    public void Buscar(View view) {
        if (Correo.getText().length() == 0) {
            Toast.makeText(getApplicationContext(), "Ingrese correo de paciente", Toast.LENGTH_SHORT).show();
        } else {
            String IP = getString(R.string.ip);
            String url = "http://" + IP + "/android/obtenerF2.php?Correo=" + Correo.getText();
            jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
            rq.add(jrq);
        }
    }


    //Función para regresar a la ventana ficha
    @SuppressLint("WrongConstant")
    public void Regresar(View view) {
        usu.setVisibility(View.VISIBLE);
        btn.setVisibility(View.VISIBLE);
        ficha.setVisibility(View.INVISIBLE);
    }

    //Función en caso de que la petición para obtener un paciente no se completo correctamente
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(),"Paciente no encontrado",Toast.LENGTH_SHORT).show();
        usu.setVisibility(View.INVISIBLE);
        btn.setVisibility(View.INVISIBLE);
    }

    //Función en caso de que la petición para obtener un paciente se completo correctamente
    public void onResponse(JSONObject response) {
        usu.setVisibility(View.VISIBLE);
        btn.setVisibility(View.VISIBLE);
        JSONArray jsonArray = response.optJSONArray("datos");
        JSONObject jsonObject = null;
        try{
            jsonObject = jsonArray.getJSONObject(0);
            LoadNom = jsonObject.optString("Nombre");
            LoadApep = jsonObject.optString("ApellidoP");
            LoadApem = jsonObject.optString("ApellidoM");
            LoadEdad = jsonObject.optString("Edad");
            LoadTel = jsonObject.optString("Telefono");
            LoadCurp = jsonObject.optString("Curp");
            LoadFe = jsonObject.optString("FechaNac");
            LoadSan = jsonObject.optString("TipoSan");
            LoadGen = jsonObject.optString("Genero");
            LoadEst = jsonObject.optString("EstadoCiv");
            LoadNa = jsonObject.optString("Nacionalidad");
            LoadFam = jsonObject.optString("Familiar");
            LoadParen = jsonObject.optString("Parent");
            LoadTel2 = jsonObject.optString("Telefon");
            rellenar();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Función para poner los datos en los campos
    private void rellenar() {
        name.setText(LoadApep+" "+LoadApem+" "+LoadNom);
        edad.setText("Edad: "+LoadEdad+" años");
        cont.setText("Contacto: "+LoadTel);
        curp.setText("Curp: "+LoadCurp);
        fechana.setText("Fecha de nacimiento: "+LoadFe);
        sang.setText("Género: "+LoadGen+" - Tipo sangre: "+LoadSan);
        estad.setText("Estado civil: "+LoadEst);
        naci.setText("Nacionalidad: "+LoadNa);
        fam.setText("Familiar a cargo: "+LoadFam);
        paren.setText("Parentesco: "+LoadParen+" - Tel: "+LoadTel2);
    }

    //Función para mandar a la ventana de observaciones médicas
    public void Ver(View view) {
        finish();
        Intent intent = new Intent(this, Ficha5.class);
        intent.putExtra("Correo",Correo.getText().toString());
        Log.e("",Correo.getText().toString());
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

        //Función para habilitar la ventana de ficha médica
        @SuppressLint("WrongConstant")
        public void Buscar2(View view) {
            usu.setVisibility(View.INVISIBLE);
            btn.setVisibility(View.INVISIBLE);
            ficha.setVisibility(View.VISIBLE);

        }

    }

