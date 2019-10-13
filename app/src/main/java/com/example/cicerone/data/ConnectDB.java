package com.example.cicerone.data;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ListActivity;
import android.net.ParseException;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.protocol.HTTP;
import cz.msebera.android.httpclient.util.EntityUtils;

public class ConnectDB{ /*qualcuno qui ha esteso ad una
ListActivity ma io preferisco gestirmi da me la risposta*/
    JSONArray jArray;
    String result = null;
    InputStream is = null;
    StringBuilder sb = null;

    public void ConnectDB() {

    }

    public void conn(){
        String result = "";
        String stringaFinale = "";
/*queste due righe di codice che vedete qui sotto sono un piccolo trucchetto per ovviare
 (solo per il momento
 all'eccezione “NetworkOnMainThreadException", questa eccezione è importantissima in quanto
 ci dice che siccome stiamo effettuando una connessione nel thread principale avremo dei
problemi in quanto tutto ciò che riguarda le connessioni o tutto ciò che comporta il
superamento del limite di tempo massimo tra richiesta/risposta superiore a 5secondi la nostra
applicazione Android la metterà in pausa appunto perchè stiamo programmando per uno smartphone
 e non possiamo attendere processi che impegnino per troppo tempo la nostra app. Ricordatevi
 una volta testato tutto, create nella classe i thread e gestite tutto dal thread.*/
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
//http post

        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://18.232.247.191/conn.php");
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.e("log_tag", "Success in http connection ");
        }catch(Exception e){
            Log.e("log_tag", "Error in http connection "+e.toString());

        }
    }
}