package com.alphacuetech.xian.autotree.Activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alphacuetech.xian.autotree.ItemClickListener;
import com.alphacuetech.xian.autotree.Models.DailyWeatherData;
import com.alphacuetech.xian.autotree.Models.WeatherAPI;
import com.alphacuetech.xian.autotree.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;


public class Menu extends AppCompatActivity implements LocationListener {

    String TAG = "XIAN";
    String TIME_ZONE = "Asia/Dhaka";

    double lat = 23.7972;
    double lon = 90.365;

    //String API_LINK_TEST = "https://api.openweathermap.org/data/2.5/onecall?lat=23.797214&lon=90.365007&exclude=current&appid=c33cae6efe8bce29a5eba0f84d6bad4c";

    RecyclerView RV_weather;
    LinearLayoutManager linearLayoutManager;
    CustomAdapterRV customAdapterRV;

    ProgressBar pb_loading;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        RV_weather = findViewById(R.id.RV_weather);
        pb_loading = findViewById(R.id.pb_loading);

        //locationPermissionRequest();
        getLocation();

        getWeatherData(""+lat, ""+lon, getApplicationContext());

    }

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;


    public void getWeatherData(String lat, String lon, Context context) {

        String url = "https://api.openweathermap.org/data/2.5/onecall?lat=" + lat + "&lon=" + lon + "&exclude=current&appid=c33cae6efe8bce29a5eba0f84d6bad4c";

        mRequestQueue = Volley.newRequestQueue(context);

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                String responseText = response.toString();
                //Toast.makeText(contex,"Response :" + responseText, Toast.LENGTH_LONG).show();//display the response on screen
                //Toast.makeText(getApplicationContext(), "Response received", Toast.LENGTH_LONG).show();
                //Log.i(TAG, responseText);

                /*try {
                    JSONObject jsonObject = new JSONObject(responseText);
                    Log.i(TAG, jsonObject.get("daily").getClass().toString());

                }catch (JSONException err){
                    Log.d(TAG, err.toString());
                }*/

                WeatherAPI weatherAPI = new Gson().fromJson(responseText, WeatherAPI.class);

                TIME_ZONE = weatherAPI.getTimezone();

                pb_loading.setVisibility(View.GONE);

                linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                RV_weather.setLayoutManager(linearLayoutManager);
                customAdapterRV = new CustomAdapterRV(weatherAPI.getDaily());
                RV_weather.setAdapter(customAdapterRV);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error :" + error.toString());
            }
        });

        mRequestQueue.add(mStringRequest);
    }


    public String convertSecToDate(int second, String timezone){

        Date date = new Date(second * 1000L);
        //DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        DateFormat format = new SimpleDateFormat("MMMM dd");
        format.setTimeZone(TimeZone.getTimeZone(timezone));
        String formatted_date = format.format(date);

        String[] months = {
            "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
        };

        //return months[date.getMonth()] + " "+date.getDate();
        return formatted_date;
    }

    public String convertKelvinToCelsius(double kelvin){
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(kelvin - 273.15);
    }

    public class CustomAdapterRV extends RecyclerView.Adapter<CustomAdapterRV.ViewHolder> {

        private ArrayList<DailyWeatherData> daily;

        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder).
         */
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
            private TextView TV_date, TV_status, TV_min, TV_max, TV_morn, TV_day, TV_eve, TV_night;

            public ViewHolder(View view) {
                super(view);
                // Define click listener for the ViewHolder's View

                TV_date = (TextView) view.findViewById(R.id.TV_date);
                TV_status = (TextView) view.findViewById(R.id.TV_status);
                TV_min = (TextView) view.findViewById(R.id.TV_min);
                TV_max = (TextView) view.findViewById(R.id.TV_max);
                TV_morn = (TextView) view.findViewById(R.id.TV_morn);
                TV_day = (TextView) view.findViewById(R.id.TV_day);
                TV_eve = (TextView) view.findViewById(R.id.TV_eve);
                TV_night = (TextView) view.findViewById(R.id.TV_night);

                view.setOnClickListener(this);
                view.setOnLongClickListener(this);

            }

            private ItemClickListener clickListener;

            public void setClickListener(ItemClickListener itemClickListener) {
                this.clickListener = itemClickListener;
            }

            @Override
            public void onClick(View view) {
                clickListener.onClick(view, getPosition(), false);

            }

            @Override
            public boolean onLongClick(View view) {
                clickListener.onClick(view, getPosition(), true);
                return true;
            }
        }

        /**
         * Initialize the dataset of the Adapter.
         *
         */
        public CustomAdapterRV(ArrayList<DailyWeatherData> daily) {
            this.daily = daily;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view, which defines the UI of the list item
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.single_weather, viewGroup, false);

            return new ViewHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {

            // Get element from your dataset at this position and replace the
            // contents of the view with that element
            viewHolder.TV_date.setText(convertSecToDate(daily.get((position)).getDt(), TIME_ZONE));
            viewHolder.TV_status.setText(daily.get((position)).getWeather().get(0).getDescription());
            viewHolder.TV_min.setText(convertKelvinToCelsius(daily.get((position)).getTemp().getMin()));
            viewHolder.TV_max.setText(convertKelvinToCelsius(daily.get((position)).getTemp().getMax()));
            viewHolder.TV_morn.setText(convertKelvinToCelsius(daily.get((position)).getTemp().getMorn()));
            viewHolder.TV_day.setText(convertKelvinToCelsius(daily.get((position)).getTemp().getDay()));
            viewHolder.TV_eve.setText(convertKelvinToCelsius(daily.get((position)).getTemp().getEve()));
            viewHolder.TV_night.setText(convertKelvinToCelsius(daily.get((position)).getTemp().getNight()));

              viewHolder.setClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, final int position, boolean isLongClick) {


                    if (isLongClick) {

                    } else {
                        DailyWeatherData d = daily.get((position));

                        Intent at = new Intent(getApplicationContext(), TreeActivity.class);
                        at.putExtra("DATA", d);
                        startActivity(at);
                    }
                }
            });
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return daily.size();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void locationPermissionRequest() {
        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_FINE_LOCATION, false);
                            Boolean coarseLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_COARSE_LOCATION, false);
                            if (fineLocationGranted != null && fineLocationGranted) {
                                // Precise location access granted.
                                //Toast.makeText(getApplicationContext(), "Precise location access granted.", Toast.LENGTH_LONG).show();
                                getLocation();

                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                // Only approximate location access granted.
                                //Toast.makeText(getApplicationContext(), "Only approximate location access granted.", Toast.LENGTH_LONG).show();
                                getLocation();

                            } else {
                                // No location access granted.
                                //Toast.makeText(getApplicationContext(), "No location access granted.", Toast.LENGTH_LONG).show();
                            }

                        }
                );

        // Before you perform the actual permission request, check whether your app
        // already has the permissions, and whether your app needs to show a permission
        // rationale dialog. For more details, see Request permissions.
        locationPermissionRequest.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }

    LocationManager locationManager;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getLocation() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            locationPermissionRequest();
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100000, 50, this);

    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lon = location.getLongitude();

        Toast.makeText(getApplicationContext(),  lat+ ", " +lon , Toast.LENGTH_LONG).show();
        getWeatherData(""+lat, ""+lon, getApplicationContext());
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getApplicationContext(), "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

}