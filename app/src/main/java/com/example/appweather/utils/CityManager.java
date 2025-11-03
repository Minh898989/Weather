package com.example.appweather.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CityManager {
    private static final String PREF_NAME = "CityPreferences";
    private static final String KEY_SAVED_CITIES = "saved_cities";
    private static final String KEY_FIRST_LAUNCH = "first_launch";

    private final SharedPreferences prefs;
    private final Gson gson;

    public CityManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();

        // Khởi tạo danh sách mặc định nếu là lần đầu
        if (isFirstLaunch()) {
            initDefaultCities();
            setFirstLaunchComplete();
        }
    }

    private boolean isFirstLaunch() {
        return prefs.getBoolean(KEY_FIRST_LAUNCH, true);
    }

    private void setFirstLaunchComplete() {
        prefs.edit().putBoolean(KEY_FIRST_LAUNCH, false).apply();
    }

    private void initDefaultCities() {
        List<String> defaultCities = Arrays.asList("Hà Nội", "Hồ Chí Minh");
        saveCities(defaultCities);
    }

    public List<String> getSavedCities() {
        String json = prefs.getString(KEY_SAVED_CITIES, null);
        if (json == null) {
            return new ArrayList<>(Arrays.asList("Hà Nội", "Hồ Chí Minh"));
        }

        Type type = new TypeToken<List<String>>(){}.getType();
        List<String> cities = gson.fromJson(json, type);
        return cities != null ? cities : new ArrayList<>(Arrays.asList("Hà Nội", "Hồ Chí Minh"));
    }

    public void saveCities(List<String> cities) {
        String json = gson.toJson(cities);
        prefs.edit().putString(KEY_SAVED_CITIES, json).apply();
    }

    public void addCity(String cityName) {
        List<String> cities = getSavedCities();
        if (!cities.contains(cityName)) {
            cities.add(cityName);
            saveCities(cities);
        }
    }

    public void removeCity(String cityName) {
        List<String> cities = getSavedCities();
        cities.remove(cityName);
        saveCities(cities);
    }

    public boolean isCitySaved(String cityName) {
        return getSavedCities().contains(cityName);
    }

    public void clearAllCities() {
        prefs.edit().remove(KEY_SAVED_CITIES).apply();
    }
}