package com.example.appweather.model;

public class AiRequest {
    public double temp;
    public double humidity;
    public double uv;
    public double wind;
    public double rainProb;
    public double rainIntensity;

    public AiRequest(double temp, double humidity, double uv, double wind, double rainProb, double rainIntensity) {
        this.temp = temp;
        this.humidity = humidity;
        this.uv = uv;
        this.wind = wind;
        this.rainProb = rainProb;
        this.rainIntensity = rainIntensity;
    }
}
