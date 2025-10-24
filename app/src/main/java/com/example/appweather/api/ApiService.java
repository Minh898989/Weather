package com.example.appweather.api;

import com.example.appweather.model.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("v4/weather/forecast")
    Call<WeatherResponse> getWeatherByLocation(
            @Query("location") String location,
            @Query("apikey") String apiKey
    );
    @GET("v4/weather/realtime")
    Call<WeatherResponse> getRealtimeWeather(
            @Query("location") String location,
            @Query("apikey") String apiKey,
            @Query("units") String unit
    );
}
