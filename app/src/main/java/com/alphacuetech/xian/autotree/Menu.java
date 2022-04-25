package com.alphacuetech.xian.autotree;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;



public class Menu extends AppCompatActivity {

    String API_LINK = "https://api.openweathermap.org/data/2.5/onecall?lat=23.797214&lon=90.365007&exclude=current&appid=c33cae6efe8bce29a5eba0f84d6bad4c";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }
}