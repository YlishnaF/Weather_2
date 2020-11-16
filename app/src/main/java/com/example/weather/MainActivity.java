package com.example.weather;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    EditText location;
    TextView showLocation;
    TextView city;
    Button button;
    private TextView temperature;
    private TextView pressure;
    private TextView humidity;
    private TextView windSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

// боковое меню
        drawerLayout= findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.nav_open,R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

//BottomDialogFragment
        showLocation = findViewById(R.id.locationshowtv);
        location = findViewById(R.id.find_location_tv);
        findViewById(R.id.point_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyBottomSheet dialogFragment = MyBottomSheet.newInstance();
                dialogFragment.setDialogListener(dialogListener);
                dialogFragment.show(getSupportFragmentManager(), "dialog_fragment");


            }
        });
   }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_home:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment, new HomeFragment())
                        .commit();
                break;
            case R.id.nav_location:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment, new LocationFragment())
                        .commit();
                break;
            case R.id.nav_email:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment, new SendFragment())
                        .commit();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }


    private OnDialogListener dialogListener = new OnDialogListener() {

        @Override
        public void onDialogOk(final String data) {
            showLocation.setText(data);
            try {
                Log.d("MyLog", "City " + ((TextView)findViewById(R.id.locationshowtv)).getText());

                final String urls = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s",((TextView)findViewById(R.id.locationshowtv)).getText(), Key.WEATHER_API_KEY);
                final URL url = new URL(urls);

                final Handler handler = new Handler();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("MyLog", "зашли в новый поток");
                        HttpsURLConnection urlConnection = null;
                        try {
                            urlConnection = (HttpsURLConnection) url.openConnection();
                            urlConnection.setRequestMethod("GET");
                            urlConnection.setReadTimeout(10000);
                            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                            Log.d("MyLog", "получили BuffredReader in");
                            String result = getLines(in);
                            Log.d("MyLog", "получили result");
                            Gson gson = new Gson();
                            final WeatherRequest weatherRequest = gson.fromJson(result, WeatherRequest.class);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    displayWeather(weatherRequest);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (null != urlConnection) {
                                urlConnection.disconnect();
                            }
                        }
                    }
                }).start();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        private String getLines(BufferedReader in){
            return in.lines().collect(Collectors.joining("\n"));
        }
        private void displayWeather(WeatherRequest weatherRequest){
            String s = weatherRequest.getName();
            String t = String.valueOf(Math.round((weatherRequest.getMain().getTemp())-273.15));
            String p = String.format("%d", weatherRequest.getMain().getPressure());
            String h = String.format("%d", weatherRequest.getMain().getHumidity());
            String w = String.format("%d", weatherRequest.getWind().getSpeed());
        }

    };

}