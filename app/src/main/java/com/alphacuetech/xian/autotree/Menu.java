package com.alphacuetech.xian.autotree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class Menu extends AppCompatActivity {

    String TAG = "XIAN";

    String API_LINK_TEST = "https://api.openweathermap.org/data/2.5/onecall?lat=23.797214&lon=90.365007&exclude=current&appid=c33cae6efe8bce29a5eba0f84d6bad4c";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        getWeatherData("23.797214", "90.365007", getApplicationContext());
    }

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;

    public void getWeatherData(String lat, String lon, Context contex){

        String url = "https://api.openweathermap.org/data/2.5/onecall?lat="+lat+"&lon="+lon+"&exclude=current&appid=c33cae6efe8bce29a5eba0f84d6bad4c";

        mRequestQueue  = Volley.newRequestQueue(contex);

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                String responseText = response.toString();
                Toast.makeText(contex,"Response :" + responseText, Toast.LENGTH_LONG).show();//display the response on screen
                Log.i(TAG, responseText);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i(TAG,"Error :" + error.toString());
            }
        });

        mRequestQueue.add(mStringRequest);


    }
}