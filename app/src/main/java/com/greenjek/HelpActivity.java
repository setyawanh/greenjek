package com.greenjek;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;

public class HelpActivity extends AppCompatActivity {
    public static String callback;
    private static TextView txtTitle;
    private static HtmlTextView htmlTextView;
    private static String teksCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        String title = getIntent().getStringExtra("title");
        String body = getIntent().getStringExtra("body");
        callback = getIntent().getStringExtra("back");
        String url = "http://greenjek.pe.hu/api.php?tipe=teks&nama="+callback;
        volleyTeksCacheRequest(url,getWindow().getDecorView().getRootView());
        TextView txtTitle = (TextView) findViewById(R.id.txtTitle);
        htmlTextView = (HtmlTextView) findViewById(R.id.txtHtml);

        txtTitle.setText(title);
    }


    public void callback(View view){
        switch (callback){
            case "motor":
                Intent motor = new Intent(this,MotorActivity.class);
                startActivity(motor);
                finish();
                break;
            case "mobil":
                Intent mobil = new Intent(this,MobilActivity.class);
                startActivity(mobil);
                finish();
                break;
            case "delivery":
                Intent delivery = new Intent(this,DeliveryActivity.class);
                startActivity(delivery);
                finish();
                break;
            case "kurir":
                Intent kurir = new Intent(this,KurirActivity.class);
                startActivity(kurir);
                finish();
                break;
            case "pijat":
                Intent pijat = new Intent(this,PijatActivity.class);
                startActivity(pijat);
                finish();
                break;
            default:
                break;
        }
    }

    public void volleyTeksCacheRequest(String url, View view){
        Cache cache = AppSingleton.getInstance(getApplicationContext()).getRequestQueue().getCache();
        Cache.Entry reqEntry = cache.get(url);
        if(reqEntry != null){
            try {
                String data = new String(reqEntry.data, "UTF-8");
                teksCache = data;
                if(!teksCache.isEmpty()){
                    setBody(view,teksCache);
                }
                //Handle the Data here.
                //Toast.makeText(getApplicationContext(),"cache: " + data,Toast.LENGTH_LONG).show();
                volleyTeksRequest(view, url);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        else{
            volleyTeksRequest(view, url);
        }
    }

    public void volleyTeksRequest(final View view, String url){
        String  REQUEST_TAG = "Teks";
        StringRequest strReq = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(),"res "+response,Toast.LENGTH_LONG).show();
                if (!response.isEmpty() && !response.equals("false") && !response.equals(teksCache)){
                    setBody(view,response);
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

    public void setBody(View view, String jsonStr) {
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            String body = jsonObj.getString("isi");
            HtmlTextView html = (HtmlTextView) view.findViewById(R.id.txtHtml);
            html.setHtml(body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
