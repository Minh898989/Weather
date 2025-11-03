package com.example.appweather.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TimeLineResponse {

    @SerializedName("data")
    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data {

        @SerializedName("timelines")
        private List<Timeline> timelines;

        public List<Timeline> getTimelines() {
            return timelines;
        }
    }

    public static class Timeline {

        @SerializedName("timestep")
        private String timestep;

        @SerializedName("endTime")
        private String endTime;

        @SerializedName("startTime")
        private String startTime;

        @SerializedName("intervals")
        private List<Interval> intervals;

        public String getTimestep() {
            return timestep;
        }

        public String getEndTime() {
            return endTime;
        }

        public String getStartTime() {
            return startTime;
        }

        public List<Interval> getIntervals() {
            return intervals;
        }
    }

    public static class Interval {

        @SerializedName("startTime")
        private String startTime;

        @SerializedName("values")
        private Values values;

        public String getStartTime() {
            return startTime;
        }

        public Values getValues() {
            return values;
        }
    }

    public static class Values {

        @SerializedName("temperature")
        private Double temperature;

        @SerializedName("temperatureMax")
        private Double temperatureMax;

        @SerializedName("temperatureMin")
        private Double temperatureMin;

        @SerializedName("weatherCode")
        private Integer weatherCode;

        @SerializedName("humidity")
        private Double humidity;

        @SerializedName("windSpeed")
        private Double windSpeed;

        @SerializedName("precipitationProbability")
        private Double precipitationProbability;

        @SerializedName("uvIndex")
        private Double uvIndex;

        @SerializedName("temperatureApparent")
        private Double temperatureApparent;

        private Double RainIntensity;

        public Double getTemperature() {
            return temperature;
        }

        public Double getTemperatureMax() {
            return temperatureMax;
        }

        public Double getTemperatureMin() {
            return temperatureMin;
        }

        public Integer getWeatherCode() {
            return weatherCode;
        }

        public Double getHumidity() {
            return humidity;
        }

        public Double getWindSpeed() {
            return windSpeed;
        }

        public Double getPrecipitationProbability() {
            return precipitationProbability;
        }

        public Double getUvIndex() {
            return uvIndex;
        }

        public Double getTemperatureApparent() {
            return temperatureApparent;
        }

        public Double getRainIntensity() {
            return RainIntensity;
        }
    }

}
