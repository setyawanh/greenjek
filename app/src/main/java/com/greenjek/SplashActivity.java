package com.greenjek;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SplashActivity extends Activity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();
        editor.putString("com.greenjek.POPUP", "true");
        editor.commit();

        if(sharedPreferences.contains("com.greenjek.IKLAN")) {
            String[] iklans = sharedPreferences.getString("com.greenjek.IKLAN","default").split(",");
            System.out.println("SIZE= "+iklans.length);
            int noIklan = sharedPreferences.getInt("com.greenjek.IKLAN_NO",-1);
            if(iklans.length <= noIklan){
                System.out.println("REFRESH");
                volleyStringRequest("http://greenjek.pe.hu/api.php?tipe=alliklan");
                sleep(2000);
            } else {
                sleep(1500);
            }
        } else {
            volleyStringRequest("http://greenjek.pe.hu/api.php?tipe=alliklan");
            sleep(2000);
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

    public void volleyStringRequest(String url){
        String  REQUEST_TAG = "Banner";
        //imgUrl=new ArrayList<>();

        StringRequest strReq = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()){
                    editor.putString("com.greenjek.IKLAN",response);
                    editor.putInt("com.greenjek.IKLAN_NO",0);
                    editor.commit();
                    System.out.println("GREENJEK RESPONSE: "+response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(),"Gagal! " + error.getMessage(),Toast.LENGTH_LONG).show();
                //getImgUrl(jsonCache);
            }
        });
        // Adding String request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, REQUEST_TAG);
    }

    private void sleep(final int time) {
        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(time);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    //intent.putExtra("popup", "true");
                    startActivity(intent);
                }
            }
        };
        timerThread.start();
    }
}
