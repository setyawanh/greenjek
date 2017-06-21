package com.greenjek;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Transformers.BaseTransformer;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;

import static android.R.attr.defaultValue;
import static android.R.attr.width;

public class MainActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private static SliderLayout imageSlider;
    private static String baseUrl = "http://greenjek.pe.hu/";
    private static String jsonCache;
    private static LinkedHashMap<String,String> url_maps;
    private static AlertDialog alertDialog;
    private int pixels;
    private int backButtonCount = 0;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private String iklanList;
    private int noIklan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageSlider = (SliderLayout)findViewById(R.id.slider);
        setSlider();

        String bannerUrl = baseUrl + "api.php?tipe=banner";
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String popup = sharedPref.getString("com.greenjek.POPUP","false");
        iklanList = sharedPref.getString("com.greenjek.IKLAN","default");
        noIklan = sharedPref.getInt("com.greenjek.IKLAN_NO",-1);

        //Toast.makeText(this,iklanList,Toast.LENGTH_LONG).show();

        if(popup.equals("true")){
            showIklan();
        }
        volleyCacheRequest(bannerUrl);
    }

    public void openMotor(View view) {
        Intent intent = new Intent(getApplicationContext(),MotorActivity.class);
        startActivity(intent);
    }

    public void openMobil(View view) {
        Intent intent = new Intent(getApplicationContext(),MobilActivity.class);
        startActivity(intent);
    }

    public void openDelivery(View view) {
        Intent intent = new Intent(getApplicationContext(),DeliveryActivity.class);
        startActivity(intent);
    }

    public void openKurir(View view) {
        Intent intent = new Intent(getApplicationContext(),KurirActivity.class);
        startActivity(intent);
    }

    public void openPijat(View view) {
        Intent intent = new Intent(getApplicationContext(),PijatActivity.class);
        startActivity(intent);
    }

    public void openTentang(View view) {
        Intent intent = new Intent(getApplicationContext(),TentangActivity.class);
        startActivity(intent);
    }

    //parsing
    public void getImgUrl(String jsonStr) {
        url_maps = new LinkedHashMap<>();
        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                if(!jsonObj.getString("banners").equals("false")){
                    //Toast.makeText(getApplicationContext(),"tidak kosong ",Toast.LENGTH_LONG).show();
                    JSONArray banners = jsonObj.getJSONArray("banners");

                    for (int i = 0; i < banners.length(); i++) {
                        JSONObject c = banners.getJSONObject(i);

                        String nama = c.getString("nama");
                        String img = baseUrl + "img/uploads/" + c.getString("img");

                        url_maps.put(nama,img);
                    }
                }
            } catch (final JSONException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //imgUrl.add(baseUrl + "img/uploads/default.jpg");
                        //Toast.makeText(getApplicationContext(),"Json parsing error: " + e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //imgUrl.add(baseUrl + "img/uploads/default.jpg");
                    //Toast.makeText(getApplicationContext(),"Couldn't get json from server!",Toast.LENGTH_LONG).show();
                }
            });
        }

        pageViewer();
    }

    public void pageViewer(){
        imageSlider = (SliderLayout)findViewById(R.id.slider);
        imageSlider.removeAllSliders();
        if(!url_maps.isEmpty()) {
            for (String name : url_maps.keySet()) {
                TextSliderView textSliderView = new TextSliderView(getApplicationContext());
                // initialize a SliderLayout
                textSliderView
                        .description(name)
                        .image(url_maps.get(name))
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(this);

                //add your extra information
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle().putString("extra", name);

                imageSlider.addSlider(textSliderView);
            }

            imageSlider.setCustomAnimation(new DescriptionAnimation());
            imageSlider.setDuration(5000);
            imageSlider.addOnPageChangeListener(this);

        }else{
            TextSliderView textSliderView = new TextSliderView(getApplicationContext());
            textSliderView
                    .description("GreenJek")
                    .image(R.drawable.banner)
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putString("extra", "GreenJek");

            imageSlider.addSlider(textSliderView);
        }

        if (url_maps.size()==1 || url_maps.isEmpty()){
            imageSlider.stopAutoCycle();
        } else {
            imageSlider.startAutoCycle();
        }

        imageSlider.setCurrentPosition(0);
        imageSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        imageSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
    }

    public void volleyStringRequest(String url){
        String  REQUEST_TAG = "Banner";
        //imgUrl=new ArrayList<>();

        StringRequest strReq = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(jsonCache) && !response.isEmpty()){
                    getImgUrl(response);
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

    public void volleyCacheRequest(String url){
        Cache cache = AppSingleton.getInstance(getApplicationContext()).getRequestQueue().getCache();
        Cache.Entry reqEntry = cache.get(url);
        if(reqEntry != null){
            try {
                String data = new String(reqEntry.data, "UTF-8");
                jsonCache = data;
                if(!jsonCache.isEmpty()){
                    getImgUrl(jsonCache);
                }
                //Handle the Data here.
                //Toast.makeText(getApplicationContext(),"cache: " + data,Toast.LENGTH_LONG).show();
                volleyStringRequest(url);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        else{
            getImgUrl(null);
            volleyStringRequest(url);
        }
    }

    public void volleyIklan(View view){
        String[] iklans = iklanList.split(",");
        int no = noIklan%iklans.length;
        System.out.println("GREENJEK: " + noIklan);
        if(iklans.length > 0 && noIklan != -1) {
            String iklanImg = baseUrl + "img/uploads/" + iklans[no];
            System.out.println("GREENJEK: "+iklanImg);

            ImageView imageView = (ImageView) view.findViewById(R.id.imgPopup);
            Picasso.with(getApplicationContext()).load(iklanImg).fit().into(imageView);

            alertDialog.show();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.getWindow().setLayout(pixels, pixels);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    private void setSlider(){
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        float scale = this.getResources().getDisplayMetrics().density;
        int margin = (int) (8 * scale + 0.5f);

        final int width = metrics.widthPixels - (2 * margin);
        final int height = width/2;

        ViewTreeObserver vto = imageSlider.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ViewGroup.LayoutParams params = imageSlider.getLayoutParams();
                params.width = width;
                params.height = height;
                imageSlider.setLayoutParams(params);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(backButtonCount>=1){
            backButtonCount = 0;
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Tekan tombol kembali sekali lagi untuk keluar",Toast.LENGTH_LONG).show();
            backButtonCount++;
        }
    }

    private void showIklan() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.popup, null);

        float scale = this.getResources().getDisplayMetrics().density;
        pixels = (int) (300 * scale + 0.5f);

        alertDialog = builder.setView(dialogView).create();

        volleyIklan(dialogView);

        ImageButton btn = (ImageButton) dialogView.findViewById(R.id.btnClose);

        btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                alertDialog.dismiss();
                editor = sharedPref.edit();
                editor.putString("com.greenjek.POPUP", "false");
                editor.putInt("com.greenjek.IKLAN_NO", ++noIklan);
                editor.commit();
            }});

        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                alertDialog.dismiss();
                editor = sharedPref.edit();
                editor.putString("com.greenjek.POPUP", "false");
                editor.putInt("com.greenjek.IKLAN_NO", ++noIklan);
                editor.commit();
            }
        });
    }
}
