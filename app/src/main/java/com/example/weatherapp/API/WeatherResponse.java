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

    @SerializedName("dt_txt")
    private String dt_txt;

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

    public String getDate() {
        return dt_txt;
    }

    public static class Main {
        @SerializedName("temp")
        private String temp;

        @SerializedName("temp_max")
        private String tempMax;

        @SerializedName("temp_min")
        private String tempMin;

        @SerializedName("humidity")  // Adding humidity field
        private String humidity;  // Humidity value as an integer percentage

        public String getTemp() {
            return temp;
        }

        public String getTempMax() {
            return tempMax;
        }

        public String getTempMin() {
            return tempMin;
        }

        public String getHumidity() {
            return humidity;
        }
    }

    public static class Weather {
        @SerializedName("main")
        private String main;

        public String getMain() {
            return main;
        }
    }

    public static class Rain {
        @SerializedName("3h")
        private String oneHourRain;  // Rain volume in the last hour

        public String getOneHourRain() {
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
