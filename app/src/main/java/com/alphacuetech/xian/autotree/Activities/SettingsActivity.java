package com.alphacuetech.xian.autotree.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.alphacuetech.xian.autotree.R;
import com.alphacuetech.xian.autotree.functions.SharedPref;

public class SettingsActivity extends AppCompatActivity {

    TextView TV_uid, TV_loc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        TV_uid = findViewById(R.id.TV_uid);
        TV_loc = findViewById(R.id.TV_loc);

        SharedPref sp = new SharedPref(getApplicationContext());

        TV_uid.setText("Your User ID is "+sp.getUID());
        TV_loc.setText("Your last know Location was "+sp.getLastLat()+", "+sp.getLastLon());

    }
}