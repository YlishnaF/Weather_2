package com.example.weather;

import android.os.Bundle;
import android.text.Layout;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LocationFragment extends Fragment{

    private OpenWeather openWeather;
    private TextInputEditText location_et;
    private Button button_ok;
    private static final float AbsoluteZero = 273.15f;
    private RecyclerView recyclerView;
    private CityHistoryAdapter adapter;
    private HistorySource historySource;

        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
          View v = inflater.inflate(R.layout.fragment_location, container, false);
          recyclerView = v.findViewById(R.id.history_recyclerView);
          recyclerView.setHasFixedSize(true);
          recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
          HistoryDao historyDao = App.getInstance()
                  .getHistoryDao();
          historySource = new HistorySource(historyDao);
          adapter = new CityHistoryAdapter(getContext(), historySource);
          recyclerView.setAdapter(adapter);
          location_et =v.findViewById(R.id.location_et);
          button_ok = v.findViewById(R.id.oklocationbtn);
          initRetorfit();
          button_ok.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  HistoryWeather hw = new HistoryWeather();
                  historySource.addHistory(getName(hw));
                  requestRetrifit(location_et.getText().toString(), Key.WEATHER_API_KEY);
              }
          });

          return v;
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case 1:
                HistoryWeather historyWeather = historySource
                        .getCities()
                        .get((int)adapter.getMenuPosition());
                historySource.removeHistory(historyWeather.id);
                adapter.notifyItemRemoved(item.getGroupId());
                return true;
        }
        return super.onContextItemSelected(item);
    }

    public HistoryWeather getName(HistoryWeather historyWeather){
          historyWeather.cityName = location_et.getText().toString();
          return  historyWeather;
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
                                       .replace(R.id.fragment_container, homeFragment).commit();
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