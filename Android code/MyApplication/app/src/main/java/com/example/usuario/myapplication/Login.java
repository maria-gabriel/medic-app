package com.example.usuario.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

    //Ventana para iniciar sesión
public class Login extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {
    public EditText user;
    public EditText pass;
    CheckBox check;
    Button x;
    RequestQueue rq;
    JsonRequest jrq;
    Boolean che;
    private static final String PREFERENT = "SESION_USER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.login);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.laun);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //Método para validar la conexión a internet
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
        } else {
            Toasty.error(getApplicationContext(), "Sin conexión a internet", Toast.LENGTH_SHORT, true).show();
        }

        user = (EditText) findViewById(R.id.user);
        pass = (EditText) findViewById(R.id.pass);
        check = (CheckBox)findViewById(R.id.checkBox);
        x=(Button)findViewById(R.id.button);
        rq=Volley.newRequestQueue(this);

    }

    //Función para validar entrada
    public void Entrar (View view) {
        if(user.getText().length()==0 || pass.getText().length()==0){
            Toast.makeText(getApplicationContext(),"Debe llenar los campos",Toast.LENGTH_SHORT).show();
        }else {
            if(user.getText().toString().equals("Admin01") && pass.getText().toString().equals("0000")){
                finish();
                Intent intent = new Intent(this, Inicio3.class);
                Context context2 = this;
                SharedPreferences shar2 = getSharedPreferences("USER_TYPE",context2.MODE_PRIVATE);
                SharedPreferences.Editor editor2 = shar2.edit();
                editor2.putString("Type","Admin");
                editor2.commit();
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
            }else {
                String IP = getString(R.string.ip);
                String url = "http://"+IP+"/android/login.php?Correo="+user.getText().toString()+"&Contra="+pass.getText().toString();
                Log.e("",user.getText().toString());
                Log.e("",pass.getText().toString());
                jrq=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
                rq.add(jrq);
            }
        }

    }

    //Función para regresar a ventena principal
    public void Cancelar (View view) {
        finish();
        Intent intent = new Intent(this, Index.class);
        startActivity(intent);
        overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
    }

    //Función en caso de que la petición para obtener datos no se completo correctamente
    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(),"Verifique que sus datos sean correctos",Toast.LENGTH_SHORT).show();
        user.setText("");
        pass.setText("");
    }

    //Función en caso de que la petición para obtener datos se completo correctamente
    @Override
    public void onResponse(JSONObject response) {

        JSONArray jsonArray = response.optJSONArray("datos");
        JSONObject jsonObject = null;
        try{
            jsonObject = jsonArray.getJSONObject(0);
            String Correo = jsonObject.optString("Correo");
            String Tipo = jsonObject.optString("Tipo");

            if(Tipo.equals("Paciente")){
                finish();
                Intent intent = new Intent(this, Ficha0.class);
                intent.putExtra("Correo", Correo);
                Context context = this;
                SharedPreferences shar = getSharedPreferences("USER_SESION",context.MODE_PRIVATE);
                SharedPreferences.Editor editor = shar.edit();
                editor.putString("CorreoUs",Correo);
                editor.commit();
                Context context2 = this;
                SharedPreferences shar2 = getSharedPreferences("USER_TYPE",context2.MODE_PRIVATE);
                SharedPreferences.Editor editor2 = shar2.edit();
                editor2.putString("Type","Paciente");
                editor2.commit();
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
            }else if(Tipo.equals("Medico")){
                finish();
                Intent intent = new Intent(this, Inicio1.class);
                intent.putExtra("Correo", Correo);
                Context context = this;
                SharedPreferences shar = getSharedPreferences("USER_SESION",context.MODE_PRIVATE);
                SharedPreferences.Editor editor = shar.edit();
                editor.putString("CorreoUs",Correo);
                editor.commit();
                Context context2 = this;
                SharedPreferences shar2 = getSharedPreferences("USER_TYPE",context2.MODE_PRIVATE);
                SharedPreferences.Editor editor2 = shar2.edit();
                editor2.putString("Type","Medico");
                editor2.commit();
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
