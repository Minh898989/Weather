package com.example.appweather.model;

public class CityWeatherItem {
    private String cityName;
    private String temperature;
    private String weatherCondition;
    private Integer weatherCode;
    private String highLowTemp;
    private boolean isLoading;
    private boolean hasError;

    public CityWeatherItem(String cityName) {
        this.cityName = cityName;
        this.isLoading = true;
        this.hasError = false;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }

    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
    }

    public Integer getWeatherCode() {
        return weatherCode;
    }

    public void setWeatherCode(Integer weatherCode) {
        this.weatherCode = weatherCode;
    }

    public String getHighLowTemp() {
        return highLowTemp;
    }

    public void setHighLowTemp(String highLowTemp) {
        this.highLowTemp = highLowTemp;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }
}