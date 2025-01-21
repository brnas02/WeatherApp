package com.example.weatherapp.API;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TomorrowResponse {

    @SerializedName("cod")
    private String cod;

    @SerializedName("message")
    private int message;

    @SerializedName("cnt")
    private int cnt;

    @SerializedName("list")
    private List<WeatherResponse> list;

    public String getCod() {
        return cod;
    }

    public int getMessage() {
        return message;
    }

    public int getCnt() {
        return cnt;
    }

    public List<WeatherResponse> getList() {
        return list;
    }

}
