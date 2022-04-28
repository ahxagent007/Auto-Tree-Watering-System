package com.alphacuetech.xian.autotree.Models;

import java.util.ArrayList;

public class WeatherAPI {
    String timezone;
    String timezone_offset;
    ArrayList<DailyWeatherData> daily = new ArrayList<DailyWeatherData>();

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getTimezone_offset() {
        return timezone_offset;
    }

    public void setTimezone_offset(String timezone_offset) {
        this.timezone_offset = timezone_offset;
    }

    public ArrayList<DailyWeatherData> getDaily() {
        return daily;
    }

    public void setDaily(ArrayList<DailyWeatherData> daily) {
        this.daily = daily;
    }
}
