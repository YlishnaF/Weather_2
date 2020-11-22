package com.example.weather;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomeFragment extends Fragment {
    TextView location_tv;
    TextView mainTemper;
    TextView pres;
    TextView humidity;
    TextView wind;
    ImageView iv;
    private TextView temperature;
    private static final float AbsoluteZero = -273.15f;
    private SharedPreferences sharedPreferences;
    private static final String myPref = "myPref";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        location_tv = v.findViewById(R.id.locationshowtv);
        mainTemper = v.findViewById(R.id.temperature_tv);
        pres = v.findViewById(R.id.pressuretv);
        humidity = v.findViewById(R.id.humiditytv);
        wind = v.findViewById(R.id.windtv);
        iv = v.findViewById(R.id.imageView);
        ImageView background = v.findViewById(R.id.imageView6);
        PointView poiintView = v.findViewById(R.id.point_view);

        poiintView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new LocationFragment()).commit();
            }
        });

        Bundle bundle = this.getArguments();
        if(bundle!=null){
            String data = bundle.getString("key");
            String temper = bundle.getString("temperature");
            String pressure = bundle.getString("pressure");
            String hum = bundle.getString("humidity");
            String windSpeed = bundle.getString("wind");
            location_tv.setText(data);
            mainTemper.setText(temper);
            pres.setText(pressure);
            humidity.setText(hum);
            wind.setText(windSpeed);
            if (Float.parseFloat(temper) < 0 ){
                Picasso.get()
                        .load("https://cdn.icon-icons.com/icons2/345/PNG/128/15_35877.png")

                        .into(background);
            } else if(Float.parseFloat(temper) >= 0 && Float.parseFloat(temper) < 10){
                Picasso.get()
                        .load("https://cdn.icon-icons.com/icons2/347/PNG/128/128_(36)_35927.png")
                        .fit()
                        .into(background);
            } else if(Float.parseFloat(temper) >10){
                Picasso.get()
                        .load("https://cdn.icon-icons.com/icons2/347/PNG/128/128_(1)_35963.png")
                        .into(background);
            }
        }
        sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences(myPref, Context.MODE_PRIVATE);

        if(sharedPreferences.contains("city") && bundle == null){
            location_tv.setText(sharedPreferences.getString("city", ""));
        }
       return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        String textValue = location_tv.getText().toString();
        SharedPreferences.Editor editor =sharedPreferences.edit();
        editor.putString("city", textValue);
        editor.apply();
    }

}