package com.alphacuetech.xian.autotree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.alphacuetech.xian.autotree.functions.SharedPref;

public class MainActivity extends AppCompatActivity {

    Spinner sp_location;
    Button btn_done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String country[] = {"China", "Bangladesh"};
        final String[] selected_country = {"China"};

        sp_location = findViewById(R.id.sp_location);
        btn_done = findViewById(R.id.btn_done);


        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,country);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        sp_location.setAdapter(adapter);

        sp_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_country[0] = country[position];
                Toast.makeText(getApplicationContext(), "Country"+country[position], Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPref sf = new SharedPref(getApplicationContext());
                sf.set_country(selected_country[0]);

                Intent i = new Intent(getApplicationContext(), Menu.class);
                startActivity(i);
                finish();
            }
        });
    }


}