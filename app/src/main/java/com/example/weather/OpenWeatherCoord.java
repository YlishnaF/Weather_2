package com.example.weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherCoord {
    @GET("data/2.5/weather")
    //api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}
    Call<WeatherRequest> loadWeather(@Query("lat") float lat, @Query("lon") float lon, @Query("appid") String key);
}
