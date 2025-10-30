package com.example.appweather.api;

import com.example.appweather.model.RealtimeResponse;
import com.example.appweather.model.TimeLineResponse;
import com.example.appweather.model.TimelineRequest;
import com.example.appweather.model.WeatherResponse;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {

        @Headers({
                        "accept: application/json",
                        "content-type: application/json"
        })
        @GET("v4/weather/forecast")
        Call<WeatherResponse> getForecast(
                        @Query("location") String location,
                        @Query("apikey") String apiKey,
                        @Query("units") String units,
                        @Query("timesteps") String timesteps);
        @Headers({
                "accept: application/json",
                "content-type: application/json"
        })
        @GET("v4/weather/history/recent")
        Call<WeatherResponse> getForecastHistory(
                @Query("location") String location,
                @Query("apikey") String apiKey,
                @Query("units") String units,
                @Query("timesteps") String timesteps,
                @Query("startTime") String startTime,
                @Query("endTime") String endTime);
        @Headers({
                        "accept: application/json",
                        "content-type: application/json"
        })
        @GET("v4/weather/realtime")
        Call<RealtimeResponse> getRealtimeWeather(
                        @Query("location") String location,
                        @Query("apikey") String apiKey,
                        @Query("units") String units);

        @Headers({
                        "accept: application/json",
                        "content-type: application/json"
        })
        @POST("v4/timelines")
        Call<TimeLineResponse> getTimelines(
                        @Query("apikey") String apiKey,
                        @Body TimelineRequest body);
}
