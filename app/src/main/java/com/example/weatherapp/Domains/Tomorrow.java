package com.example.weatherapp.Domains;

public class Tomorrow {
    private String hour;
    private String day;
    private String picPath;
    private String status;
    private String highTemp;
    private String lowTemp;

    public Tomorrow(String hour, String day, String lowTemp, String picPath, String status, String highTemp) {
        this.hour = hour;
        this.day = day;
        this.lowTemp = lowTemp;
        this.picPath = picPath;
        this.status = status;
        this.highTemp = highTemp;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHighTemp() {
        return highTemp;
    }

    public void setHighTemp(String highTemp) {
        this.highTemp = highTemp;
    }

    public String getLowTemp() {
        return lowTemp;
    }

    public void setLowTemp(String lowTemp) {
        this.lowTemp = lowTemp;
    }
}
