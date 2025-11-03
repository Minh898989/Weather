package com.example.appweather.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.appweather.R;
import com.example.appweather.api.ApiClient;
import com.example.appweather.api.ApiService;
import com.example.appweather.model.AiRequest;
import com.example.appweather.model.AiSuggestionResponse;
import com.example.appweather.model.TimeLineResponse;
import com.example.appweather.model.TimelineRequest;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherNotificationReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "weather_channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("WeatherReceiver", "✅ onReceive được gọi!");

        PendingResult pendingResult = goAsync();
        String apiKey = intent.getStringExtra("api_key");

        if (apiKey == null) {
            showNotification(context, "Thiếu API key!");
            pendingResult.finish();
            return;
        }

        fetchWeatherData(context, apiKey, pendingResult);
    }

    private void fetchWeatherData(Context context, String apiKey, PendingResult pendingResult) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String startTime = sdf.format(Calendar.getInstance().getTime());

        Calendar endCal = Calendar.getInstance();
        endCal.add(Calendar.DAY_OF_YEAR, 1);
        String endTime = sdf.format(endCal.getTime());

        // Gửi request với các field cần thiết
        TimelineRequest request = new TimelineRequest(
                "21.0285,105.8542", // Hà Nội
                Arrays.asList(
                        "temperature",
                        "humidity",
                        "windSpeed",
                        "uvIndex",
                        "precipitationProbability",
                        "rainIntensity"
                ),
                Arrays.asList("1h"),
                "metric",
                startTime,
                endTime,
                null
        );

        Call<TimeLineResponse> call = apiService.getTimelines(apiKey, request);
        call.enqueue(new Callback<TimeLineResponse>() {
            @Override
            public void onResponse(Call<TimeLineResponse> call, Response<TimeLineResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        if (response.body().getData() != null &&
                                !response.body().getData().getTimelines().isEmpty() &&
                                !response.body().getData().getTimelines().get(0).getIntervals().isEmpty()) {

                            List<TimeLineResponse.Interval> intervals =
                                    response.body().getData().getTimelines().get(0).getIntervals();

                            double sumTemp = 0, sumHum = 0, sumUv = 0, sumWind = 0;
                            double sumRainProb = 0, sumRainIntensity = 0;
                            int count = 0;

                            for (TimeLineResponse.Interval i : intervals) {
                                TimeLineResponse.Values v = i.getValues();
                                if (v != null) {
                                    if (v.getTemperature() != null) sumTemp += v.getTemperature();
                                    if (v.getHumidity() != null) sumHum += v.getHumidity();
                                    if (v.getUvIndex() != null) sumUv += v.getUvIndex();
                                    if (v.getWindSpeed() != null) sumWind += v.getWindSpeed();
                                    if (v.getPrecipitationProbability() != null) sumRainProb += v.getPrecipitationProbability();
                                    if (v.getRainIntensity() != null) sumRainIntensity += v.getRainIntensity();
                                    count++;
                                }
                            }

                            if (count > 0) {
                                double avgTemp = sumTemp / count;
                                double avgHum = sumHum / count;
                                double avgUv = sumUv / count;
                                double avgWind = sumWind / count;
                                double avgRainProb = sumRainProb / count;
                                double avgRainIntensity = sumRainIntensity / count;

                                String msg = String.format(
                                        Locale.getDefault(),
                                        "🌡 %.1f°C | 💧 %.0f%% | ☀️ UV %.1f\n🌬 %.1f m/s | 🌧 %.0f%% (%.2f mm/h)",
                                        avgTemp, avgHum, avgUv, avgWind, avgRainProb, avgRainIntensity
                                );

                                AiRequest req = new AiRequest(avgTemp, avgHum, avgUv, avgWind, avgRainProb, avgRainIntensity);

                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl("http://10.0.2.2:3000/")
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();

                                ApiService service = retrofit.create(ApiService.class);

                                service.getAiSuggestion(req).enqueue(new Callback<AiSuggestionResponse>() {
                                    @Override
                                    public void onResponse(Call<AiSuggestionResponse> call, Response<AiSuggestionResponse> response) {
                                        if (response.isSuccessful() && response.body() != null) {
                                            String aiMsg = response.body().getSuggestion();
                                            String fullMsg = msg + "\n🤖 " + aiMsg;
                                            showNotification(context, fullMsg);
                                        } else {
                                            showNotification(context, msg + "\n🤖 Không nhận được phản hồi AI");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<AiSuggestionResponse> call, Throwable t) {
                                        showNotification(context, msg + "\n🤖 Lỗi AI: " + t.getMessage());
                                    }
                                });

                            } else {
                                showNotification(context, "Không có dữ liệu trung bình!");
                            }

                        } else {
                            showNotification(context, "Không có dữ liệu thời tiết hợp lệ.");
                        }
                    } catch (Exception e) {
                        showNotification(context, "Lỗi đọc dữ liệu: " + e.getMessage());
                    }
                } else {
                    showNotification(context, "API phản hồi lỗi: " + response.code());
                }
                pendingResult.finish();
            }

            @Override
            public void onFailure(Call<TimeLineResponse> call, Throwable t) {
                showNotification(context, "Lỗi kết nối API: " + t.getMessage());
                pendingResult.finish();
            }
        });
    }


    private void showNotification(Context context, String message) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Weather Updates",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_aqi)
                .setContentTitle("🌦 Trung bình thời tiết hôm nay")
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        int notificationId = (int) System.currentTimeMillis();
        notificationManager.notify(notificationId, builder.build());
    }

}