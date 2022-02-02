package com.example.usuario.myapplication;

import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

    //Ventana para ver observaciones médicas para el paciente
public class Ficha4 extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener{
    TextView txt;
    RequestQueue rq;
    JsonRequest jrq;
    String LoadId,LoadDiag,LoadPre,LoadS1,LoadS2,LoadS3,LoadS4,LoadMot,LoadFe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ficha4);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.laun);

        txt = (TextView) findViewById(R.id.result);
        txt.setMovementMethod(new ScrollingMovementMethod());
        rq= Volley.newRequestQueue(this);
        String Correo = getIntent().getExtras().getString("Correo");
        String IP = getString(R.string.ip);
        String url = "http://"+IP+"/android/obtenerO.php?Correo="+Correo;
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

    //Función para regresar a la ventana ficha
    public void Regresar(View view) {
        finish();
        Intent intent = new Intent(this, Ficha2.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    //Función en caso de que la petición para obtener las observaciones no se completo correctamente
    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(),"No se tienen observaciones",Toast.LENGTH_SHORT).show();
    }

    //Función en caso de que la petición para obtener las observaciones se completo correctamente
    @Override
    public void onResponse(JSONObject response) {
        txt.setText("");
        JSONArray jsonArray = response.optJSONArray("datos");
        JSONObject jsonObject = null;
        try{
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                LoadId = jsonObject.optString("Id_Observacion");
                LoadDiag = jsonObject.optString("Diagnostico");
                LoadPre = jsonObject.optString("Prescripcion");
                LoadS1 = jsonObject.optString("Presion");
                LoadS2 = jsonObject.optString("Respiracion");
                LoadS3 = jsonObject.optString("Pulso");
                LoadS4 = jsonObject.optString("Temperatura");
                LoadMot = jsonObject.optString("Motivo");
                LoadFe = jsonObject.optString("Fecha");
                txt.append("Id Observacion: " + LoadId + "\nDiagnóstico: " + LoadDiag + "\nPrescripción: " + LoadPre + "\nPresión: " + LoadS1 +
                        "\nRespiración: " + LoadS2 + "\nPulso: " + LoadS3 + "\nTemperatura: " + LoadS4 + "\nMotivo cita: " + LoadMot + "\nFecha cita: " + LoadFe);
                txt.append("\n_____________________________________\n\n");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
