package com.example.weatherapp.Domains;

public class TomorrowDomain {
    private String day;
    private String picPath;
    private String status;
    private String highTemp;
    private String lowTemp;

    public TomorrowDomain(String day, String lowTemp, String picPath, String status, String highTemp) {
        this.day = day;
        this.lowTemp = lowTemp;
        this.picPath = picPath;
        this.status = status;
        this.highTemp = highTemp;
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
