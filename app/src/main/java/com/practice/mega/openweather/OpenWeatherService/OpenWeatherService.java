package com.practice.mega.openweather.OpenWeatherService;

import org.json.JSONObject;

/*
Created by : Mohamed Abdelaziz
E-mail : Mohamedsaleh1984@hotmail.com
*/
//https://battuta.medunes.net/
public class OpenWeatherService
        implements IOpenWeather {
    private String _strCity = "nil";
    private JSONObject jsonRoot = null;
    private String country = "county";
    private double _temperature = 0;
    private double _humidity = 0;
    private double _pressure = 0;
    private double _dMinTemp = 0;
    private double _dMaxTemp = 0;
    private Boolean _isOk = false;

    public JSONObject get_data() {
        return jsonRoot;
    }

    public void setData(JSONObject jsonObject) {
        jsonRoot = jsonObject;
        parseServiceCallback();
    }

    public OpenWeatherService(String city) {
        this._strCity = city;
    }

    public String getCity() {
        return this._strCity;
    }

    public double getMinTemperature() {
        return _dMinTemp;
    }

    public String getCountry() {
        return country;
    }

    public double getMaxTemperature() {
        return _dMaxTemp;
    }

    public double getTemperature() {
        return _temperature;
    }

    public double getHumidity() {
        return _humidity;
    }

    public double getPressure() {
        return _pressure;
    }

    public Boolean isOk() {
        return _isOk;
    }

    /**
     * Parse Service received String and Populate \n
     * properties with the values.
     */
    private void parseServiceCallback() {
        try {
            if(jsonRoot.getInt("cod") == 200)
                _isOk = true;

            JSONObject sys = jsonRoot.getJSONObject("sys");
            country = sys.getString("country");

            JSONObject main = jsonRoot.getJSONObject("main");
            _temperature = main.getDouble("temp");
            _pressure = main.getDouble("pressure");
            _humidity = main.getDouble("humidity");
            _dMaxTemp = main.getDouble("temp_max");
            _dMinTemp = main.getDouble("temp_min");
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}