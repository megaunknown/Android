package com.practice.mega.openweather.OpenWeatherService;

import org.json.JSONObject;

public interface IOpenWeather {
    public String getCity();
    public double getMinTemperature();
    public String getCountry() ;
    public double getMaxTemperature();
    public double getTemperature() ;
    public double getHumidity();
    public double getPressure() ;
    public void setData(JSONObject data);
    public Boolean isOk();
}
