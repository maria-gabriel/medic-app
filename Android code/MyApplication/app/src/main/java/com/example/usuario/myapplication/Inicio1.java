package com.example.usuario.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

    //Ventana de inicio de médico
public class Inicio1 extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener{
    public ImageButton img, img2;
    public ImageView img3;
    private static final int SELECT_FILE = 100;
    RequestQueue rq;
    JsonRequest jrq;
    Uri selectedImage = null;
    public String Correo, Murl;
    String LoadNom,LoadApep,LoadApem,LoadEdad,LoadTel,Imagen,LoadImg;
    public EditText name,edad,corr,num;
    Button btn;
    int def= R.drawable.pic;
    String defaul = String.valueOf(def);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.inicio1);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.laun);

        Context context = this;
        Context context2 = this;
        SharedPreferences shar = getSharedPreferences("USER_SESION",context.MODE_PRIVATE);
        Correo = shar.getString("CorreoUs","null");
        SharedPreferences shar2 = getSharedPreferences("USER_IMAGE",context2.MODE_PRIVATE);
        Murl = shar2.getString("ImagePer",defaul);

        //Método para validar la conexión a internet
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
        } else {
            Toasty.error(getApplicationContext(), "Sin conexión a internet", Toast.LENGTH_SHORT, true).show();
        }

        img = (ImageButton) findViewById(R.id.img);
        img2 = (ImageButton) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        name = (EditText) findViewById(R.id.name);
        edad = (EditText) findViewById(R.id.edad);
        corr = (EditText) findViewById(R.id.email);
        num = (EditText) findViewById(R.id.num);
        btn = (Button) findViewById(R.id.button);

        Log.e("",Murl+" -menu");
        Log.e("",Correo+"correo");
        rq= Volley.newRequestQueue(this);
        img3.setBackgroundResource(R.drawable.loading);
        String IP = getString(R.string.ip);
        String url = "http://"+IP+"/android/obtenerM.php?Correo="+Correo;
        jrq=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        rq.add(jrq);
        Log.e("",LoadImg+" -mmmmmmm");
        File imagefile = new File(Murl);
        InputStream output = null;
        try {
            output = new FileInputStream(imagefile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bmp = BitmapFactory.decodeStream(output);
        img.setImageBitmap(bmp);
        AnimationDrawable f = (AnimationDrawable) img3.getBackground();
        f.start();
    }

    //Función para abrir la galería
    public void openGallery(View view) {
        if (ActivityCompat.checkSelfPermission(Inicio1.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Inicio1.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, SELECT_FILE);
        }else{
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(gallery, SELECT_FILE);
        }
    }

    //Función para seleccionar imagen de la galería
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK && requestCode == SELECT_FILE){
            Uri dat = selectedImage = data.getData();
            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String url=getPathFromURI(dat);
            Context context = this;
            Bitmap bmp = BitmapFactory.decodeStream(imageStream);

            Log.e("",url +" nnn");
            SharedPreferences shar = getSharedPreferences("USER_IMAGE",context.MODE_PRIVATE);
            SharedPreferences.Editor editor = shar.edit();
            editor.putString("ImagePer", url);
            Imagen=url;
            //new Enviar2(Inicio1.this).execute();
            editor.commit();
            //img.setImageURI(Uri.fromFile(new File(url)));
            img.setImageBitmap(bmp);
        }else{
            Toast.makeText(getApplicationContext(),"No se pudo poner imagen",Toast.LENGTH_SHORT).show();
        }
    }

    //Función para obtener la ruta de la imagen
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    //Función en caso de que la petición para obtener datos de paciente no se completo correctamente
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(),"Error al cargar datos del perfil",Toast.LENGTH_SHORT).show();
    }

    //Función en caso de que la petición para obtener datos de paciente se completo correctamente
    public void onResponse(JSONObject response) {
        JSONArray jsonArray = response.optJSONArray("datos");
        JSONObject jsonObject = null;
        try{
            jsonObject = jsonArray.getJSONObject(0);
            LoadNom = jsonObject.optString("Nombre");
            LoadApep = jsonObject.optString("ApellidoP");
            LoadApem = jsonObject.optString("ApellidoM");
            LoadEdad = jsonObject.optString("Cedula");
            LoadTel = jsonObject.optString("Telefono");
            LoadImg = jsonObject.optString("Token");
            rellenar();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Función para poner los datos en los campos
    private void rellenar() {
        name.setText(LoadApep+" "+LoadApem+" "+LoadNom);
        edad.setText("Cédula: "+LoadEdad);
        corr.setText("Email: "+Correo);
        num.setText("Contacto: "+LoadTel);
    }

    //Función para cerrar sesión
    public void Terminar(View view) {
        Context context = this;
        SharedPreferences shar = getSharedPreferences("USER_SESION",context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shar.edit();
        editor.putString("CorreoUs","");
        editor.commit();
        SharedPreferences shar2 = getSharedPreferences("USER_IMAGE",context.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = shar2.edit();
        editor2.putString("ImagePer",defaul);
        editor2.commit();
        SharedPreferences shar3 = getSharedPreferences("USER_TYPE",context.MODE_PRIVATE);
        SharedPreferences.Editor editor3 = shar3.edit();
        editor3.putString("Type","");
        editor3.commit();
        finish();
        Intent intent = new Intent(this, Index.class);
        startActivity(intent);
        overridePendingTransition(R.anim.zoom_forward_in, R.anim.zoom_forward_out);
    }

    //Función para ir a la ventana de citas
    public void Citas(View view) {
        finish();
        Intent intent = new Intent(this, Citas1.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    //Función para ir a la ventana de fichas
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

}