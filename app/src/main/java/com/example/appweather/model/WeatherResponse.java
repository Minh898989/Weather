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
        @SerializedName("temperatureMin")
        private Double temperatureMin;
        @SerializedName("temperatureMax")
        private Double temperatureMax;
        @SerializedName("temperatureAvg")
        private Double temperatureAvg;
        @SerializedName("temperatureApparentAvg")
        private Double temperatureApparentAvg;
        @SerializedName("temperatureApparentMax")
        private Double temperatureApparentMax;
        @SerializedName("temperatureApparentMin")
        private Double temperatureApparentMin;

        // ========== HUMIDITY & DEW POINT ==========
        @SerializedName("humidity")
        private Double humidity;
        @SerializedName("humidityAvg")
        private Double humidityAvg;
        @SerializedName("humidityMax")
        private Double humidityMax;
        @SerializedName("humidityMin")
        private Double humidityMin;
        @SerializedName("dewPoint")
        private Double dewPoint;
        @SerializedName("dewPointAvg")
        private Double dewPointAvg;
        @SerializedName("dewPointMax")
        private Double dewPointMax;
        @SerializedName("dewPointMin")
        private Double dewPointMin;

        // ========== WIND ==========
        @SerializedName("windSpeed")
        private Double windSpeed;
        @SerializedName("windDirection")
        private Double windDirection;
        @SerializedName("windGust")
        private Double windGust;
        @SerializedName("windSpeedAvg")
        private Double windSpeedAvg;
        @SerializedName("windSpeedMax")
        private Double windSpeedMax;
        @SerializedName("windSpeedMin")
        private Double windSpeedMin;
        @SerializedName("windGustAvg")
        private Double windGustAvg;
        @SerializedName("windGustMax")
        private Double windGustMax;
        @SerializedName("windGustMin")
        private Double windGustMin;
        @SerializedName("windDirectionAvg")
        private Double windDirectionAvg;

        // ========== PRECIPITATION ==========
        @SerializedName("precipitationProbability")
        private Double precipitationProbability;
        @SerializedName("precipitationProbabilityAvg")
        private Double precipitationProbabilityAvg;
        @SerializedName("precipitationProbabilityMax")
        private Double precipitationProbabilityMax;
        @SerializedName("precipitationProbabilityMin")
        private Double precipitationProbabilityMin;
        @SerializedName("rainIntensity")
        private Double rainIntensity;
        @SerializedName("rainIntensityAvg")
        private Double rainIntensityAvg;
        @SerializedName("rainIntensityMax")
        private Double rainIntensityMax;
        @SerializedName("rainIntensityMin")
        private Double rainIntensityMin;
        @SerializedName("rainAccumulation")
        private Double rainAccumulation;
        @SerializedName("rainAccumulationAvg")
        private Double rainAccumulationAvg;
        @SerializedName("rainAccumulationMax")
        private Double rainAccumulationMax;
        @SerializedName("rainAccumulationMin")
        private Double rainAccumulationMin;
        @SerializedName("rainAccumulationSum")
        private Double rainAccumulationSum;
        @SerializedName("rainAccumulationLwe")
        private Double rainAccumulationLwe;
        @SerializedName("rainAccumulationLweAvg")
        private Double rainAccumulationLweAvg;
        @SerializedName("rainAccumulationLweMax")
        private Double rainAccumulationLweMax;
        @SerializedName("rainAccumulationLweMin")
        private Double rainAccumulationLweMin;

        // ========== SNOW ==========
        @SerializedName("snowIntensity")
        private Double snowIntensity;
        @SerializedName("snowIntensityAvg")
        private Double snowIntensityAvg;
        @SerializedName("snowIntensityMax")
        private Double snowIntensityMax;
        @SerializedName("snowIntensityMin")
        private Double snowIntensityMin;
        @SerializedName("snowAccumulation")
        private Double snowAccumulation;
        @SerializedName("snowAccumulationAvg")
        private Double snowAccumulationAvg;
        @SerializedName("snowAccumulationMax")
        private Double snowAccumulationMax;
        @SerializedName("snowAccumulationMin")
        private Double snowAccumulationMin;
        @SerializedName("snowAccumulationSum")
        private Double snowAccumulationSum;
        @SerializedName("snowAccumulationLwe")
        private Double snowAccumulationLwe;
        @SerializedName("snowAccumulationLweAvg")
        private Double snowAccumulationLweAvg;
        @SerializedName("snowAccumulationLweMax")
        private Double snowAccumulationLweMax;
        @SerializedName("snowAccumulationLweMin")
        private Double snowAccumulationLweMin;
        @SerializedName("snowAccumulationLweSum")
        private Double snowAccumulationLweSum;
        @SerializedName("snowDepth")
        private Double snowDepth;
        @SerializedName("snowDepthAvg")
        private Double snowDepthAvg;
        @SerializedName("snowDepthMax")
        private Double snowDepthMax;
        @SerializedName("snowDepthMin")
        private Double snowDepthMin;
        @SerializedName("snowDepthSum")
        private Double snowDepthSum;

        // ========== SLEET ==========
        @SerializedName("sleetIntensity")
        private Double sleetIntensity;
        @SerializedName("sleetIntensityAvg")
        private Double sleetIntensityAvg;
        @SerializedName("sleetIntensityMax")
        private Double sleetIntensityMax;
        @SerializedName("sleetIntensityMin")
        private Double sleetIntensityMin;
        @SerializedName("sleetAccumulation")
        private Double sleetAccumulation;
        @SerializedName("sleetAccumulationAvg")
        private Double sleetAccumulationAvg;
        @SerializedName("sleetAccumulationMax")
        private Double sleetAccumulationMax;
        @SerializedName("sleetAccumulationMin")
        private Double sleetAccumulationMin;
        @SerializedName("sleetAccumulationLwe")
        private Double sleetAccumulationLwe;
        @SerializedName("sleetAccumulationLweAvg")
        private Double sleetAccumulationLweAvg;
        @SerializedName("sleetAccumulationLweMax")
        private Double sleetAccumulationLweMax;
        @SerializedName("sleetAccumulationLweMin")
        private Double sleetAccumulationLweMin;
        @SerializedName("sleetAccumulationLweSum")
        private Double sleetAccumulationLweSum;

        // ========== FREEZING RAIN ==========
        @SerializedName("freezingRainIntensity")
        private Double freezingRainIntensity;
        @SerializedName("freezingRainIntensityAvg")
        private Double freezingRainIntensityAvg;
        @SerializedName("freezingRainIntensityMax")
        private Double freezingRainIntensityMax;
        @SerializedName("freezingRainIntensityMin")
        private Double freezingRainIntensityMin;

        // ========== ICE ==========
        @SerializedName("iceAccumulation")
        private Double iceAccumulation;
        @SerializedName("iceAccumulationAvg")
        private Double iceAccumulationAvg;
        @SerializedName("iceAccumulationMax")
        private Double iceAccumulationMax;
        @SerializedName("iceAccumulationMin")
        private Double iceAccumulationMin;
        @SerializedName("iceAccumulationSum")
        private Double iceAccumulationSum;
        @SerializedName("iceAccumulationLwe")
        private Double iceAccumulationLwe;
        @SerializedName("iceAccumulationLweAvg")
        private Double iceAccumulationLweAvg;
        @SerializedName("iceAccumulationLweMax")
        private Double iceAccumulationLweMax;
        @SerializedName("iceAccumulationLweMin")
        private Double iceAccumulationLweMin;
        @SerializedName("iceAccumulationLweSum")
        private Double iceAccumulationLweSum;

        // ========== HAIL ==========
        @SerializedName("hailProbability")
        private Double hailProbability;
        @SerializedName("hailProbabilityAvg")
        private Double hailProbabilityAvg;
        @SerializedName("hailProbabilityMax")
        private Double hailProbabilityMax;
        @SerializedName("hailProbabilityMin")
        private Double hailProbabilityMin;
        @SerializedName("hailSize")
        private Double hailSize;
        @SerializedName("hailSizeAvg")
        private Double hailSizeAvg;
        @SerializedName("hailSizeMax")
        private Double hailSizeMax;
        @SerializedName("hailSizeMin")
        private Double hailSizeMin;

        // ========== CLOUD ==========
        @SerializedName("cloudCover")
        private Double cloudCover;
        @SerializedName("cloudCoverAvg")
        private Double cloudCoverAvg;
        @SerializedName("cloudCoverMax")
        private Double cloudCoverMax;
        @SerializedName("cloudCoverMin")
        private Double cloudCoverMin;
        @SerializedName("cloudBase")
        private Double cloudBase;
        @SerializedName("cloudBaseAvg")
        private Double cloudBaseAvg;
        @SerializedName("cloudBaseMax")
        private Double cloudBaseMax;
        @SerializedName("cloudBaseMin")
        private Double cloudBaseMin;
        @SerializedName("cloudCeiling")
        private Double cloudCeiling;
        @SerializedName("cloudCeilingAvg")
        private Double cloudCeilingAvg;
        @SerializedName("cloudCeilingMax")
        private Double cloudCeilingMax;
        @SerializedName("cloudCeilingMin")
        private Double cloudCeilingMin;

        // ========== PRESSURE ==========
        @SerializedName("pressureSeaLevel")
        private Double pressureSeaLevel;
        @SerializedName("pressureSeaLevelAvg")
        private Double pressureSeaLevelAvg;
        @SerializedName("pressureSeaLevelMax")
        private Double pressureSeaLevelMax;
        @SerializedName("pressureSeaLevelMin")
        private Double pressureSeaLevelMin;
        @SerializedName("pressureSurfaceLevel")
        private Double pressureSurfaceLevel;
        @SerializedName("pressureSurfaceLevelAvg")
        private Double pressureSurfaceLevelAvg;
        @SerializedName("pressureSurfaceLevelMax")
        private Double pressureSurfaceLevelMax;
        @SerializedName("pressureSurfaceLevelMin")
        private Double pressureSurfaceLevelMin;

        // ========== VISIBILITY ==========
        @SerializedName("visibility")
        private Double visibility;
        @SerializedName("visibilityAvg")
        private Double visibilityAvg;
        @SerializedName("visibilityMax")
        private Double visibilityMax;
        @SerializedName("visibilityMin")
        private Double visibilityMin;

        // ========== UV INDEX ==========
        @SerializedName("uvIndex")
        private Integer uvIndex;
        @SerializedName("uvIndexAvg")
        private Integer uvIndexAvg;
        @SerializedName("uvIndexMax")
        private Integer uvIndexMax;
        @SerializedName("uvIndexMin")
        private Integer uvIndexMin;
        @SerializedName("uvHealthConcern")
        private Integer uvHealthConcern;
        @SerializedName("uvHealthConcernAvg")
        private Integer uvHealthConcernAvg;
        @SerializedName("uvHealthConcernMax")
        private Integer uvHealthConcernMax;
        @SerializedName("uvHealthConcernMin")
        private Integer uvHealthConcernMin;

        // ========== WEATHER CODE ==========
        @SerializedName("weatherCode")
        private Integer weatherCode;
        @SerializedName("weatherCodeMax")
        private Integer weatherCodeMax;
        @SerializedName("weatherCodeMin")
        private Integer weatherCodeMin;

        // ========== EVAPOTRANSPIRATION ==========
        @SerializedName("evapotranspiration")
        private Double evapotranspiration;
        @SerializedName("evapotranspirationAvg")
        private Double evapotranspirationAvg;
        @SerializedName("evapotranspirationMax")
        private Double evapotranspirationMax;
        @SerializedName("evapotranspirationMin")
        private Double evapotranspirationMin;
        @SerializedName("evapotranspirationSum")
        private Double evapotranspirationSum;

        // ========== SUN & MOON (DAILY) ==========
        @SerializedName("sunriseTime")
        private String sunriseTime;
        @SerializedName("sunsetTime")
        private String sunsetTime;
        @SerializedName("moonriseTime")
        private String moonriseTime;
        @SerializedName("moonsetTime")
        private String moonsetTime;

        // ========== GETTERS ==========
        public Double getTemperature() {
            return temperature;
        }

        public Double getTemperatureApparent() {
            return temperatureApparent;
        }

        public Double getTemperatureMin() {
            return temperatureMin;
        }

        public Double getTemperatureMax() {
            return temperatureMax;
        }

        public Double getTemperatureAvg() {
            return temperatureAvg;
        }

        public Double getHumidity() {
            return humidity;
        }

        public Double getDewPoint() {
            return dewPoint;
        }

        public Double getWindSpeed() {
            return windSpeed;
        }

        public Double getWindDirection() {
            return windDirection;
        }

        public Double getWindGust() {
            return windGust;
        }

        public Double getPrecipitationProbability() {
            return precipitationProbability;
        }

        public Double getRainIntensity() {
            return rainIntensity;
        }

        public Double getSnowIntensity() {
            return snowIntensity;
        }

        public Double getSleetIntensity() {
            return sleetIntensity;
        }

        public Double getFreezingRainIntensity() {
            return freezingRainIntensity;
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

        public Double getCloudBase() {
            return cloudBase;
        }

        public Double getCloudCeiling() {
            return cloudCeiling;
        }

        public Double getPressureSeaLevel() {
            return pressureSeaLevel;
        }

        public Double getPressureSurfaceLevel() {
            return pressureSurfaceLevel;
        }

        public Integer getUvIndex() {
            return uvIndex;
        }

        public Integer getUvHealthConcern() {
            return uvHealthConcern;
        }

        public Double getHailProbability() {
            return hailProbability;
        }

        public Double getHailSize() {
            return hailSize;
        }

        public String getSunriseTime() {
            return sunriseTime;
        }

        public String getSunsetTime() {
            return sunsetTime;
        }

        public String getMoonriseTime() {
            return moonriseTime;
        }

        public String getMoonsetTime() {
            return moonsetTime;
        }

        // Daily aggregates
        public Double getTemperatureApparentAvg() {
            return temperatureApparentAvg;
        }

        public Double getTemperatureApparentMax() {
            return temperatureApparentMax;
        }

        public Double getTemperatureApparentMin() {
            return temperatureApparentMin;
        }

        public Double getWindSpeedAvg() {
            return windSpeedAvg;
        }

        public Double getWindSpeedMax() {
            return windSpeedMax;
        }

        public Integer getWeatherCodeMax() {
            return weatherCodeMax;
        }

        public Integer getWeatherCodeMin() {
            return weatherCodeMin;
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
