package com.example.appweather.model;

import com.example.appweather.activity.MainActivity;
import com.google.gson.annotations.SerializedName;

public class RealtimeResponse {

    @SerializedName("data")
    private Data data;

    @SerializedName("location")
    private Location location;

    public Data getData() {
        return data;
    }

    public Location getLocation() {
        return location;
    }

    public static class Data {
        @SerializedName("time")
        private String time;

        @SerializedName("values")
        private Values values;

        public String getTime() {
            return time;
        }

        public Values getValues() {
            return values;
        }
    }
    public static class Values implements MainActivity.WeatherValues {
        @SerializedName("temperature")
        private Double temperature;

        @SerializedName("temperatureApparent")
        private Double temperatureApparent;

        @SerializedName("humidity")
        private Double humidity;

        @SerializedName("dewPoint")
        private Double dewPoint;

        @SerializedName("cloudBase")
        private Double cloudBase;

        @SerializedName("cloudCeiling")
        private Double cloudCeiling;

        @SerializedName("cloudCover")
        private Double cloudCover;

        @SerializedName("precipitationProbability")
        private Double precipitationProbability;

        @SerializedName("rainIntensity")
        private Double rainIntensity;

        @SerializedName("sleetIntensity")
        private Double sleetIntensity;

        @SerializedName("snowIntensity")
        private Double snowIntensity;

        @SerializedName("pressureSurfaceLevel")
        private Double pressureSurfaceLevel;

        @SerializedName("uvHealthConcern")
        private Double uvHealthConcern;

        @SerializedName("uvIndex")
        private Double uvIndex;

        @SerializedName("visibility")
        private Double visibility;

        @SerializedName("weatherCode")
        private Integer weatherCode;

        @SerializedName("windDirection")
        private Double windDirection;

        @SerializedName("windGust")
        private Double windGust;

        @SerializedName("windSpeed")
        private Double windSpeed;

        // --- Getters ---
        public Double getTemperature() {
            return temperature;
        }

        public Double getTemperatureApparent() {
            return temperatureApparent;
        }

        public Double getHumidity() {
            return humidity;
        }

        public Double getDewPoint() {
            return dewPoint;
        }

        public Double getCloudBase() {
            return cloudBase;
        }

        public Double getCloudCeiling() {
            return cloudCeiling;
        }

        public Double getCloudCover() {
            return cloudCover;
        }

        public Double getPrecipitationProbability() {
            return precipitationProbability;
        }

        public Double getRainIntensity() {
            return rainIntensity;
        }

        public Double getSleetIntensity() {
            return sleetIntensity;
        }

        public Double getSnowIntensity() {
            return snowIntensity;
        }

        public Double getPressureSurfaceLevel() {
            return pressureSurfaceLevel;
        }

        public Double getUvHealthConcern() {
            return uvHealthConcern;
        }

        public Double getUvIndex() {
            return uvIndex;
        }

        public Double getVisibility() {
            return visibility;
        }

        public Integer getWeatherCode() {
            return weatherCode;
        }

        public Double getWindDirection() {
            return windDirection;
        }

        public Double getWindGust() {
            return windGust;
        }

        public Double getWindSpeed() {
            return windSpeed;
        }
    }

    // --- Nested class Location ---
    public static class Location {
        @SerializedName("lat")
        private Double lat;

        @SerializedName("lon")
        private Double lon;

        @SerializedName("name")
        private String name;

        @SerializedName("type")
        private String type;

        public Double getLat() {
            return lat;
        }

        public Double getLon() {
            return lon;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }
    }
}
