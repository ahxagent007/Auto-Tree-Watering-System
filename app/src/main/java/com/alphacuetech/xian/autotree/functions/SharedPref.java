package com.alphacuetech.xian.autotree.functions;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    Context context;
    public SharedPref(Context context) {
        this.context = context;
    }

    public String getUID(){
        return "123";
    }

    public void set_country(String country){

        // Storing data into SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        // Creating an Editor object to edit(write to the file)
                SharedPreferences.Editor myEdit = sharedPreferences.edit();

        // Storing the key and its value as the data fetched from edittext
                myEdit.putString("country", country);

        myEdit.commit();

    }

    public String get_country(){
        // Retrieving the value using its keys the file name
        // must be same in both saving and retrieving the data
        SharedPreferences sh = context.getSharedPreferences("MySharedPref", context.MODE_PRIVATE);

        // The value will be default as empty string because for
        // the very first time when the app is opened, there is nothing to show
        String country = sh.getString("name", "#");

        return country;

    }

}
