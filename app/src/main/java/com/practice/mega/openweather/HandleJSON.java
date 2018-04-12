package com.practice.mega.openweather;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by MEGA on 4/5/2018.
 */

public class HandleJSON {
    private  String _strAppID = "7a59ab38677b2b24aba6f6dab13a2ac4";
    private  String _strUrl = "https://api.openweathermap.org/data/2.5/weather";
    private  String _strCity = "hangzhou";

    private String country = "county";
    private double _temperature = 0;
    private double _humidity = 0;
    private double _pressure = 0;
    private double _dMinTemp = 0;
    private double _dMaxTemp = 0;
    private String _urlString = null;
    public volatile boolean parsingComplete = true;
   private  JSONObject _data = null;

    public HandleJSON(String city) {
        this._strCity = city;
    }

    public double getMinTemperature(){return _dMinTemp;}
    public String getCountry() {
        return country;
    }
    public double getMaxTemperature(){return _dMaxTemp;}
    public double getTemperature() { return _temperature;}
    public double getHumidity() {
        return _humidity;
    }
    public double getPressure() { return _pressure; }

    public void readAndParseJSON(String in) {
        Log.i("Tag", in);
        try {
            JSONObject reader = new JSONObject(in);
            if(in != null) {
                JSONObject sys = reader.getJSONObject("sys");
                country = sys.getString("country");

                JSONObject main = reader.getJSONObject("main");
                _temperature = main.getDouble("temp");
                _pressure = main.getDouble("pressure");
                _humidity = main.getDouble("humidity");
                _dMaxTemp = main.getDouble("temp_max");
                _dMinTemp = main.getDouble("temp_min");
            }
            else
            {
                country = "nil";
                _temperature =  _pressure=_humidity=_dMaxTemp=_dMinTemp = 0;
            }
            parsingComplete = false;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*

    *
    cod	"404"
    message	"city not found"
     //URL url = new URL(_strUrl + "?q=" + _strCity+ "&APPID=" + _strAppID);
    * */
    public void fetchJSON() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {

                    Log.i("Tag","Process Started.");

                    URL url = new URL(_strUrl + "?q=" + _strCity+ "&APPID=" + _strAppID);

                    Log.i("Tag",url.toString());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    StringBuffer json = new StringBuffer(1024);
                    String strRequestResult = "";

                    Log.i("Tag","Fetching Data.");

                    while ((strRequestResult = reader.readLine()) != null)
                        json.append(strRequestResult).append("\n");
                    reader.close();

                    _data = new JSONObject(json.toString());

                    Log.i("Tag",_data.toString());

                    if (_data.getInt("cod") != 200) {
                        Log.i("Tag","Cancelled, Unexpected Error has occurred");
                        _data = null;
                        return null;
                    }
                    else
                    {
                        Log.i("Tag","Fetched Successfully.");
                    }

                    if(_data.getString("cod") != null)
                    {
                        readAndParseJSON(_data.toString());
                    }

                } catch (Exception e) {

                    System.out.println("Exception " + e.getMessage());
                    return null;
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void Void) {
                if (_data != null) {
                    Log.i("Tag", "My weather received.");
                }
                else
                {
                    Log.i("Tag", "Unexpected Error.");
                }
            }
        }.execute();
    }
}