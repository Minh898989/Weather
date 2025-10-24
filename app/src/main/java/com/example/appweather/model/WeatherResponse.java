package com.example.appweather.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WeatherResponse {

    @SerializedName("timelines")
    private Timelines timelines;

    @SerializedName("location")
    private Location location;

    public Timelines getTimelines() {
        return timelines;
    }

    public void setTimelines(Timelines timelines) {
        this.timelines = timelines;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public static class Timelines {
        @SerializedName("minutely")
        private List<TimelineData> minutely;

        @SerializedName("hourly")
        private List<TimelineData> hourly;

        @SerializedName("daily")
        private List<TimelineData> daily;

        public List<TimelineData> getMinutely() {
            return minutely;
        }

        public List<TimelineData> getHourly() {
            return hourly;
        }

        public List<TimelineData> getDaily() {
            return daily;
        }
    }

    public static class TimelineData {
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

    public static class Values {
        @SerializedName("temperature")
        private Double temperature;

        @SerializedName("temperatureApparent")
        private Double temperatureApparent;

        @SerializedName("humidity")
        private Double humidity;

        @SerializedName("windSpeed")
        private Double windSpeed;

        @SerializedName("windDirection")
        private Double windDirection;

        @SerializedName("precipitationProbability")
        private Double precipitationProbability;

        @SerializedName("precipitationType")
        private Integer precipitationType;

        @SerializedName("weatherCode")
        private Integer weatherCode;

        @SerializedName("visibility")
        private Double visibility;

        @SerializedName("cloudCover")
        private Double cloudCover;

        @SerializedName("uvIndex")
        private Integer uvIndex;

        // Getters
        public Double getTemperature() {
            return temperature;
        }

        public Double getTemperatureApparent() {
            return temperatureApparent;
        }

        public Double getHumidity() {
            return humidity;
        }

        public Double getWindSpeed() {
            return windSpeed;
        }

        public Double getWindDirection() {
            return windDirection;
        }

        public Double getPrecipitationProbability() {
            return precipitationProbability;
        }

        public Integer getPrecipitationType() {
            return precipitationType;
        }

        public Integer getWeatherCode() {
            return weatherCode;
        }

        public Double getVisibility() {
            return visibility;
        }

        public Double getCloudCover() {
            return cloudCover;
        }

        public Integer getUvIndex() {
            return uvIndex;
        }
    }

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
