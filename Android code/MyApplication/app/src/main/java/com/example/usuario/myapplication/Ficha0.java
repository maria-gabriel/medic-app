package com.example.usuario.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

    //Ventana para crear ficha médica
public class Ficha0 extends AppCompatActivity {
    Spinner tipo,estado,dia, mes,año,sexo;
    EditText curp,naci,name,paren,tel;
    TextView text,text2;
    Button btn,btn2;
    public String Correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ficha0);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.laun);
        Context context = this;
        SharedPreferences shar = getSharedPreferences("USER_SESION",context.MODE_PRIVATE);
        Correo = shar.getString("CorreoUs","null");

        curp = (EditText)findViewById(R.id.curp);
        naci = (EditText)findViewById(R.id.naci);
        name = (EditText)findViewById(R.id.name);
        paren = (EditText)findViewById(R.id.paren);
        tel = (EditText)findViewById(R.id.tel);
        text2 = (TextView) findViewById(R.id.txt);
        text = (TextView) findViewById(R.id.salto);
        SpannableString mitextoU = new SpannableString("Ya tengo una ficha en esta cuenta");
        mitextoU.setSpan(new UnderlineSpan(), 0, mitextoU.length(), 0);
        text.setText(mitextoU);
        btn = (Button)findViewById(R.id.button);
        btn2 = (Button)findViewById(R.id.button2);

        tipo = (Spinner) findViewById(R.id.sp);
        String[] TipoSangre = {"Tipo Sangre","A+","B+","AB+","O+","A-","B-","AB-","O-"};
        tipo.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_item,TipoSangre));

        estado = (Spinner) findViewById(R.id.sp2);
        String[] EstadoCivil = {"Estado Civil","Soltero(a)","Casado(a)","Separado(a)","Divorciado(a)","Viudo(a)"};
        estado.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_item,EstadoCivil));

        sexo = (Spinner) findViewById(R.id.sp6);
        String[] Sexo = {"Genero","Masculino","Femenino"};
        sexo.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_item,Sexo));

        dia = (Spinner) findViewById(R.id.sp3);
        String[] Dia = {"Día","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20",
                "21","22","23","24","25","26","27","28","29","30","31"};
        dia.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_item,Dia));

        mes = (Spinner) findViewById(R.id.sp4);
        String[] Mes = {"Mes","01","02","03","04","05","06","07","08","09","10","11","12"};
        mes.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_item,Mes));

        año = (Spinner) findViewById(R.id.sp5);
        String[] Año = {"Año","2010","2009","2008","2007","2006","2005","2004","2003","2002","2001","2000","1999","1998","1997","1996",
                "1995","1994","1993","1992","1991","1990","1989","1988","1987","1986","1985","1984","1983","1982","1981","1980",
                "1979","1978","1977","1976","1975","1974","1973","1972","1971","1970","1969","1968","1967","1966","1965","1964",
                "1963","1962","1961","1960","1959","1958","1957","1956","1955","1954","1953","1952","1951","1950"};
        año.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_item,Año));
    }

    //Función para validar los datos
    public void Guardar(View view) {
        Pattern Plantilla = null;
        Matcher Resultado = null;
        Plantilla = Pattern.compile("[A-Z]{1}[AEIOU]{1}[A-Z]{2}[0-9]{2}"+"(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])"+"[HM]{1}" +
                        "(AS|BC|BS|CC|CS|CH|CL|CM|DF|DG|GT|GR|HG|JC|MC|MN|MS|NT|NL|OC|PL|QT|QR|SP|SL|SR|TC|TS|TL|VZ|YN|ZS|NE)" +
                        "[B-DF-HJ-NP-TV-Z]{3}" +"[0-9A-Z]{1}[0-9]{1}$");
        String cur = curp.getText().toString();
        Resultado = Plantilla.matcher(cur);
        if(curp.getText().length()==0 || naci.getText().length()==0 ||
                dia.getSelectedItem().toString().equals("Día") || mes.getSelectedItem().toString().equals("Mes") ||
                año.getSelectedItem().toString().equals("Año") || tipo.getSelectedItem().toString().equals("Tipo Sangre") ||
                estado.getSelectedItem().toString().equals("Estado Civil") || sexo.getSelectedItem().toString().equals("Género")){
            Toast.makeText(getApplicationContext(), "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
        }else {
            if(Resultado.find()==true){
                if(name.getText().length()==0){
                    name.setText("NA");
                }
                if(paren.getText().length()==0){
                    paren.setText("NA");
                }
                if(tel.getText().length()==0){
                    tel.setText("NA");
                }
                new Enviar(Ficha0.this).execute();
            }else{
                Toast.makeText(getApplicationContext(), "Tamaño de CURP no válido ", Toast.LENGTH_SHORT).show();
            }

        }

    }

    //Función para saltar a la siguiente ventana
    public void Saltar(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("La ficha médica es un recurso importante del sistema ¿Está seguro que desea continuar?")
                .setTitle("Confirmación")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        btn2.callOnClick();
                    }
                });
        builder.setNegativeButton("Cancelar", null);
        AlertDialog alert = builder.create();
        alert.show();
    }

    //Función para pasar a la siguiente ventana
    public void Continuar(View view) {
        finish();
        Intent intent = new Intent(this, Inicio2.class);
        //intent.putExtra("Correo", Correo);
        startActivity(intent);
        overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
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
                        Toast.makeText(getApplicationContext(),"Tu ficha médica se ha guardado",Toast.LENGTH_SHORT).show();
                        new Enviar2(Ficha0.this).execute();
                        btn2.callOnClick();
                    }
                });
            }else{
                contexto.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Error al guardar datos",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }
    }

    //Función para crear la ficha
    public boolean Insertar(){
        HttpClient httpClient;
        List<NameValuePair> nameValuePairs;
        HttpPost httpPost;
        httpClient = new DefaultHttpClient();
        String IP = getString(R.string.ip);
        httpPost = new HttpPost("http://"+IP+"/android/ficha.php");
        nameValuePairs = new ArrayList<>(11);
        String fech = año.getSelectedItem().toString()+"-"+mes.getSelectedItem().toString()+"-"+dia.getSelectedItem().toString();

        nameValuePairs.add(new BasicNameValuePair("Curp",curp.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("FechaNac",fech));
        nameValuePairs.add(new BasicNameValuePair("TipoSan",tipo.getSelectedItem().toString()));
        nameValuePairs.add(new BasicNameValuePair("Genero",sexo.getSelectedItem().toString()));
        nameValuePairs.add(new BasicNameValuePair("EstadoCiv",estado.getSelectedItem().toString()));
        nameValuePairs.add(new BasicNameValuePair("Nacionalidad",naci.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("Familiar",name.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("Parent",paren.getText().toString().trim()));
        nameValuePairs.add(new BasicNameValuePair("Telefon",tel.getText().toString().trim()));

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
                        Toast.makeText(getApplicationContext(),"Tu ficha médica se ha guardado",Toast.LENGTH_SHORT).show();
                        btn2.callOnClick();
                    }
                });
            }else{
                contexto.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Error al guardar datos",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }
    }

    //Función para asignar la ficha al médico
    public boolean Insertar2(){
        HttpClient httpClient;
        List<NameValuePair> nameValuePairs;
        HttpPost httpPost;
        httpClient = new DefaultHttpClient();
        String IP = getString(R.string.ip);
        httpPost = new HttpPost("http://"+IP+"/android/insertarPF.php");
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

}
