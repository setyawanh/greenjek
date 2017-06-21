package com.greenjek;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.io.UnsupportedEncodingException;

public class TentangActivity extends AppCompatActivity {
    private static String teksCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tentang);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView txtBody2 = (TextView) findViewById(R.id.txtBody2);
        String url = "http://greenjek.pe.hu/api.php?tipe=teks&nama=info";
        String body2 = "Aplikasi ini dibuat untuk memudahkan dalam pemesanan jasa Greenjek. Semua informasi pemesanan dikirim ke operator via WhatsApp. Aplikasi ini hanya tersedia untuk Android versi 4.1 keatas.";
        volleyTeksCacheRequest(url);
        txtBody2.setText(body2);
    }

    public void volleyTeksCacheRequest(String url){
        Cache cache = AppSingleton.getInstance(getApplicationContext()).getRequestQueue().getCache();
        Cache.Entry reqEntry = cache.get(url);
        if(reqEntry != null){
            try {
                String data = new String(reqEntry.data, "UTF-8");
                teksCache = data;
                if(!teksCache.isEmpty()){
                    setBody(teksCache);
                }
                //Handle the Data here.
                //Toast.makeText(getApplicationContext(),"cache: " + data,Toast.LENGTH_LONG).show();
                volleyTeksRequest(url);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        else{
            volleyTeksRequest(url);
        }
    }

    public void volleyTeksRequest(String url){
        String  REQUEST_TAG = "Teks";
        StringRequest strReq = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(),"res "+response,Toast.LENGTH_LONG).show();
                if (!response.isEmpty() && !response.equals("false") && !response.equals(teksCache)){
                    setBody(response);
                } else {
                    //Toast.makeText(getApplicationContext(),"null",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(),"no data",Toast.LENGTH_LONG).show();
            }
        });
        // Adding String request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, REQUEST_TAG);
    }

    public void setBody(String jsonStr) {
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            String body = jsonObj.getString("isi");
            HtmlTextView htmlTextView = (HtmlTextView) findViewById(R.id.txtBody);
            htmlTextView.setHtml(body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
