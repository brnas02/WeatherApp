package com.example.weatherapp.API;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WeatherResponse {
    @SerializedName("main")
    private Main main;
    @SerializedName("weather")
    private List<Weather> weather;

    public Main getMain() {
        return main;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public static class Main {
        @SerializedName("temp")
        private double temp;

        @SerializedName("temp_max")
        private double tempMax;

        @SerializedName("temp_min")
        private double tempMin;

        public double getTemp() {
            return temp;
        }

        public double getTempMax() {
            return tempMax;
        }

        public double getTempMin() {
            return tempMin;
        }
    }

    public static class Weather {
        @SerializedName("main")
        private String main;

        public String getMain() {
            return main;
        }
    }
}

