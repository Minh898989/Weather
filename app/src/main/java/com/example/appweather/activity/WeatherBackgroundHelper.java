package com.example.appweather.activity;

import android.view.View;

import com.example.appweather.R;

import java.util.Locale;

public class WeatherBackgroundHelper {

    public static void updateBackground(View rootView, String weather, boolean isNight) {
        if (rootView == null || rootView.getContext() == null) return;

        int backgroundRes;
        String w = weather.toLowerCase(Locale.ROOT);

        switch (w) {
            case "rain_heavy":
                backgroundRes = R.drawable.bg_rain_heavy;
                break;
            case "rain_light":
                backgroundRes = R.drawable.bg_rain_light;
                break;
            case "cloudy":
                backgroundRes = R.drawable.bg_cloudy;
                break;
            case "moon":
                backgroundRes = R.drawable.bg_gradient;
                break;
            case "sunny":
            default:
                backgroundRes = R.drawable.bg_sunny;
                break;
        }

        rootView.animate()
                .alpha(0f)
                .setDuration(300)
                .withEndAction(() -> {
                    rootView.setBackgroundResource(backgroundRes);
                    rootView.animate().alpha(1f).setDuration(300).start();
                })
                .start();
    }
}
