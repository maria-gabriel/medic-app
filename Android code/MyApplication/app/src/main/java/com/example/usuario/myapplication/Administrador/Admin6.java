package com.example.usuario.myapplication.Administrador;

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
import com.example.usuario.myapplication.Config3;
import com.example.usuario.myapplication.Inicio3;
import com.example.usuario.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

 //Ventana para visualizacón de motivos de las citas
public class Admin6 extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener{
    String L1,L2,L3,L4,L5,L6,L7,L8,L9,L10,L11,L12,L13,L14,L15,L16,L17,L18;
    RequestQueue rq;
    JsonRequest jrq;
    TextView result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin6);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.laun);

        result = (TextView) findViewById(R.id.paci);
        result.setMovementMethod(new ScrollingMovementMethod());
        rq= Volley.newRequestQueue(this);
        String IP = getString(R.string.ip);
        //Manda petición para obtener datos de la base de datos
        String url = "http://"+IP+"/android/reporte2.php";
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

     //Función ir al reporte de citas de pacientes
     public void Reporte3(View view) {
         finish();
         Intent intent = new Intent(this, Admin7.class);
         startActivity(intent);
         overridePendingTransition(R.anim.left_in, R.anim.left_out);
     }

     //Función hacer respaldo de BD
     public void Reporte4(View view) {
         finish();
         Intent intent = new Intent(this, Config3.class);
         startActivity(intent);
         overridePendingTransition(R.anim.left_in, R.anim.left_out);
     }

    //Función en caso de que la petición para obtener los motivos no se completo correctamente
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(),"Error al cargar los datos",Toast.LENGTH_SHORT).show();
    }

    //Funcion en caso de que la peticón para obtener los motivos se completo correctamente
    public void onResponse(JSONObject response) {
        result.setText("");
        JSONArray jsonArray = response.optJSONArray("datos");
        JSONObject jsonObject = null;
        try{
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                L1 = jsonObject.optString("Motivo1");
                L2 = jsonObject.optString("Motivo2");
                L3 = jsonObject.optString("Motivo3");
                L4 = jsonObject.optString("Motivo4");
                L5 = jsonObject.optString("Motivo5");
                L6 = jsonObject.optString("Motivo6");
                L7 = jsonObject.optString("Motivo7");
                L8 = jsonObject.optString("Motivo8");
                L9 = jsonObject.optString("Motivo9");
                L10 = jsonObject.optString("Motivo10");
                L11 = jsonObject.optString("Motivo11");
                L12 = jsonObject.optString("Motivo12");
                L13 = jsonObject.optString("Motivo13");
                L14 = jsonObject.optString("Motivo14");
                L15 = jsonObject.optString("Motivo15");
                L16 = jsonObject.optString("Motivo16");
                L17 = jsonObject.optString("Motivo17");
                L18 = jsonObject.optString("Motivo18");

                result.append("\nMotivo             No. citas\n\nEnfermedad aguda: "+L1+"\n\nEnfermedad crónica: "+L2+"\n\nEnfermedad mental: "+L3
                        +"\n\nEnfermedad inmunológica: "+L4+"\n\nEnfermedad endocrina: "+L5+"\n\nEnfermedad neurológica: "+L6+"\n\nEnfermedad visual: "+L7
                        +"\n\nEnfermedad auditiva: "+L8+"\n\nEnfermedad respiratoria: "+L9+"\n\nEnfermedad cardiovascular: "+L10+"\n\nEnfermedad digestiva: "+L11
                        +"\n\nEnfermedad dermatológica: "+L12+"\n\nEnfermedad locomotora: "+L13+"\n\nEnfermedad genitourinaria: "+L14+"\n\nEnfermedad infecciosa: "+L15
                        +"\n\nEnfermedad traumática: "+L16+"\n\nEnfermedad por causa externa: "+L17+"\n\nOtro: "+L18);
                result.append("\n\n");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
