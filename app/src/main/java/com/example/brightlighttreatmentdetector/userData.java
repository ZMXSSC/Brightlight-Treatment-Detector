package com.example.brightlighttreatmentdetector;

public class userData {
    String pass_status, actualTime, averageLux, maxLux, minLux, Percentage, totalTime;

    public userData() {
    }

    public userData(String pass_status, String actualTime, String averageLux, String maxLux, String minLux, String percentage, String totalTime) {
        this.pass_status = pass_status;
        this.actualTime = actualTime;
        this.averageLux = averageLux;
        this.maxLux = maxLux;
        this.minLux = minLux;
        Percentage = percentage;
        this.totalTime = totalTime;
    }

    public String getPass_status() {
        return pass_status;
    }

    public void setPass_status(String pass_status) {
        this.pass_status = pass_status;
    }

    public String getactualTime() {
        return actualTime;
    }

    public void setactualTime(String actualTime) {
        this.actualTime = actualTime;
    }

    public String getAverageLux() {
        return averageLux;
    }

    public void setAverageLux(String averageLux) {
        this.averageLux = averageLux;
    }

    public String getMaxLux() {
        return maxLux;
    }

    public void setMaxLux(String maxLux) {
        this.maxLux = maxLux;
    }

    public String getMinLux() {
        return minLux;
    }

    public void setMinLux(String minLux) {
        this.minLux = minLux;
    }

    public String getPercentage() {
        return Percentage;
    }

    public void setPercentage(String percentage) {
        Percentage = percentage;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }
}
