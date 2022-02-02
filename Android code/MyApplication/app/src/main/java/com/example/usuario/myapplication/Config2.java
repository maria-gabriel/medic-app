package com.example.usuario.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

    //Ventana para configuración de cuenta de paciente
public class Config2 extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {
    RequestQueue rq;
    JsonRequest jrq;
    String Correo, LoadTel,LoadCon,LoadEda;
    EditText cor,tel,con,con2,eda,edit;
    Button b1,b2,b3;
    TextView text;
    LinearLayout linearLayout;
    RelativeLayout relativeLayout;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config2);
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

        webView = (WebView)findViewById(R.id.webView1);
        relativeLayout=(RelativeLayout)findViewById(R.id.linear);
        linearLayout = (LinearLayout) findViewById(R.id.linear2);
        cor = (EditText) findViewById(R.id.cor);
        tel = (EditText) findViewById(R.id.tel);
        eda = (EditText) findViewById(R.id.eda);
        con = (EditText) findViewById(R.id.con);
        con2 = (EditText) findViewById(R.id.con2);
        b1 = (Button) findViewById(R.id.ed);
        b2 = (Button) findViewById(R.id.show);
        b3 = (Button) findViewById(R.id.show2);
        b2.setVisibility(View.INVISIBLE);
        b3.setVisibility(View.INVISIBLE);
        con2.setVisibility(View.INVISIBLE);
        linearLayout.setVisibility(View.INVISIBLE);
        text = (TextView) findViewById(R.id.texto2);
        SpannableString mitextoU = new SpannableString("Necesitas ayuda?");
        mitextoU.setSpan(new UnderlineSpan(), 0, mitextoU.length(), 0);
        text.setText(mitextoU);
        rq= Volley.newRequestQueue(this);
        String IP = getString(R.string.ip);
        String url = "http://"+IP+"/android/obtenerP.php?Correo="+Correo;
        jrq=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        rq.add(jrq);
    }

    //Función en caso de que la petición para obtener los datos del paciente no se completo correctamente
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(),"Error al cargar datos",Toast.LENGTH_SHORT).show();
    }

    //Función en caso de que la petición para obtener los datos del paciente se completo correctamente
    public void onResponse(JSONObject response) {
        JSONArray jsonArray = response.optJSONArray("datos");
        JSONObject jsonObject = null;
        try{
            jsonObject = jsonArray.getJSONObject(0);
            LoadTel = jsonObject.optString("Telefono");
            LoadCon = jsonObject.optString("Contra");
            LoadEda = jsonObject.optString("Edad");

            cor.setText(Correo);
            tel.setText(LoadTel);
            eda.setText(LoadEda);
            con.setText(LoadCon);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    //Función para ir a la ventana de fichas
    public void Ficha(View view) {
        finish();
        Intent intent = new Intent(this, Ficha2.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    //Función para validar la modificación de la cuenta
    public void Configurar(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_item, null);
        edit= v.findViewById(R.id.Pasaword);
        builder.setMessage("Ingrese contraseña:");
        builder.setTitle("Confirmación");
        builder.setView(v);

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                Log.e("",edit.getText().toString());
                if(edit.getText().toString().length()==0){
                    Toast.makeText(getApplicationContext(),"Debe ingresar su contraseña",Toast.LENGTH_SHORT).show();
                }else{
                    if(edit.getText().toString().equals(LoadCon)){
                        Modificar();
                    }else{
                        Toast.makeText(getApplicationContext(),"Contraseña incorrecta",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        builder.show();
    }

    //Función para habilitar la ventana de configuración
    public void Modificar(){
        cor.setEnabled(true);
        tel.setEnabled(true);
        eda.setEnabled(true);
        con.setEnabled(true);
        con2.setVisibility(View.VISIBLE);
        b1.setVisibility(View.INVISIBLE);
        b2.setVisibility(View.VISIBLE);
        b3.setVisibility(View.VISIBLE);
        text.setVisibility(View.INVISIBLE);
    }

    //Función para validar los cambios
    public void Guardar(View view) {
        if(cor.getText().length()==0 || tel.getText().length()==0 || con.getText().length()==0 || con.getText().length()==0 || eda.getText().length()==0){
            Toast.makeText(getApplicationContext(),"Debe llenar todos los campos",Toast.LENGTH_SHORT).show();
        }else{
            Pattern Plantilla = null;
            Matcher Resultado = null;
            Plantilla = Pattern.compile("^([0-9a-zA-Z]([_.w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-w]*[0-9a-zA-Z].)+([a-zA-Z]{2,9}.)+[a-zA-Z]{2,3})$");
            String em = cor.getText().toString();
            Resultado = Plantilla.matcher(em);
            if(Resultado.find()==true){
                if(con.getText().toString().equals(con2.getText().toString())){
                    new Config2.Enviar(Config2.this).execute();
                }else{
                    Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getApplicationContext(), "E-mail no válido", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Función para validar si la petición de modificación se realizó correctamente
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
                        Toast.makeText(getApplicationContext(),"Los cambios se han guardado",Toast.LENGTH_SHORT).show();
                        b3.callOnClick();

                    }
                });
            }else{
                contexto.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Hubo un error de modificación",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }
    }

    //Función para realizar los cambios
    public boolean Insertar(){
        HttpClient httpClient;
        List<NameValuePair> nameValuePairs;
        HttpPost httpPost;
        httpClient = new DefaultHttpClient();
        String IP = getString(R.string.ip);
        httpPost = new HttpPost("http://"+IP+"/android/modificar6.php?Correo="+Correo);
        nameValuePairs = new ArrayList<>(11);

        nameValuePairs.add(new BasicNameValuePair("Correo",cor.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("Telefono",tel.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("Edad",eda.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("Contra",con.getText().toString()));

        Context context = this;
        SharedPreferences shar = getSharedPreferences("USER_SESION",context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shar.edit();
        editor.putString("CorreoUs",cor.getText().toString());
        editor.commit();

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

    //Función para recargar la ventana
    public void iniciar(){
        finish();
        Intent intent = new Intent(this, Config2.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    //Función para cancelar la modificación
    public void Cancelar(View view){
        cor.setEnabled(false);
        tel.setEnabled(false);
        eda.setEnabled(false);
        con.setEnabled(false);
        con2.setVisibility(View.INVISIBLE);
        b1.setVisibility(View.VISIBLE);
        b2.setVisibility(View.INVISIBLE);
        b3.setVisibility(View.INVISIBLE);
        text.setVisibility(View.VISIBLE);
        iniciar();
    }

    //Función para regresar a la ventana configuración
    public void Atras(View view){
        linearLayout.setVisibility(View.INVISIBLE);
    }

    //Función para abrir el sitio web de ayuda
    public void Ayudar(View view) {
        linearLayout.setVisibility(View.VISIBLE);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        String IP = getString(R.string.ip);
        webView.loadUrl("http://"+IP+"/android/Index.html");
    }
}
