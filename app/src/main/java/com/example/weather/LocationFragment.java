package com.example.weather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LocationFragment extends Fragment{

    //private OnFragmetLocationListener listener;
    private OpenWeather openWeather;
    private TextInputEditText location_et;
    private Button button_ok;
    private static final float AbsoluteZero = 273.15f;

      @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
          View v = inflater.inflate(R.layout.fragment_location, container, false);
          location_et =v.findViewById(R.id.location_et);
          button_ok = v.findViewById(R.id.oklocationbtn);
          initRetorfit();
          button_ok.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  requestRetrifit(location_et.getText().toString(), Key.WEATHER_API_KEY);
              }
          });
          return v;
    }

    private void initRetorfit(){
        Retrofit retrofit;
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        openWeather = retrofit.create(OpenWeather.class);

    }

    private void requestRetrifit(String city, String apiKey){
          openWeather.loadWeather(city, apiKey)
                  .enqueue(new Callback<WeatherRequest>() {
                      @Override
                      public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                          final WeatherRequest request = response.body();
                          if(request!=null){
                              float result = response.body().getMain().getTemp() - AbsoluteZero;
                              float pressure = response.body().getMain().getPressure();
                              float humidity = response.body().getMain().getHumidity();
                              float wind = response.body().getWind().getSpeed();

                              Bundle bundle = new Bundle();
                              bundle.putString("key", response.body().getName());
                              bundle.putString("temperature", Float.toString(result));
                              bundle.putString("pressure", Float.toString(pressure));
                              bundle.putString("humidity", Float.toString(humidity));
                              bundle.putString("wind", Float.toString(wind));
                              HomeFragment homeFragment = new HomeFragment();
                              homeFragment.setArguments(bundle);
                               getActivity().getSupportFragmentManager()
                                       .beginTransaction()
                                       .replace(R.id.nav_host_fragment, homeFragment).commit();
                              Toast.makeText(getContext(),Float.toString(result), Toast.LENGTH_SHORT).show();
                          }
                      }

                      @Override
                      public void onFailure(Call<WeatherRequest> call, Throwable t) {
                             Toast.makeText(getContext(),"error", Toast.LENGTH_SHORT).show();
                      }
                  });
    }


}