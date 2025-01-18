package com.example.weatherapp.API;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WeatherResponse {

    @SerializedName("main")
    private Main main;

    @SerializedName("weather")
    private List<Weather> weather;

    @SerializedName("wind")  // Wind data
    private Wind wind;

    @SerializedName("rain")  // Rain data
    private Rain rain;

    public Main getMain() {
        return main;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public Wind getWind() {
        return wind;
    }

    public Rain getRain() {
        return rain;
    }

    public static class Main {
        @SerializedName("temp")
        private double temp;

        @SerializedName("temp_max")
        private double tempMax;

        @SerializedName("temp_min")
        private double tempMin;

        @SerializedName("humidity")  // Adding humidity field
        private int humidity;  // Humidity value as an integer percentage

        public double getTemp() {
            return temp;
        }

        public double getTempMax() {
            return tempMax;
        }

        public double getTempMin() {
            return tempMin;
        }

        public int getHumidity() {
            return humidity;
        }
    }

    public static class Weather {
        @SerializedName("main")
        private String main;

        @SerializedName("description")  // Adding description field
        private String description;

        public String getMain() {
            return main;
        }

        public String getDescription() {
            return description;
        }
    }

    public static class Rain {
        @SerializedName("1h")
        private double oneHourRain;  // Rain volume in the last hour

        public double getOneHourRain() {
            return oneHourRain;
        }
    }

    public static class Wind {
        @SerializedName("speed")
        private double speed;  // Wind speed in meters per second (m/s)

        public double getSpeed() {
            return speed;
        }
    }
}
