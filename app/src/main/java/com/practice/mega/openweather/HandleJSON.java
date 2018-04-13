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

/*
Created by : Mohamed Abdelaziz
E-mail : Mohamedsaleh1984@hotmail.com
*/
//https://battuta.medunes.net/
public class HandleJSON {
    private  String _strAppID = "7a59ab38677b2b24aba6f6dab13a2ac4";
    private  String _strUrl = "https://api.openweathermap.org/data/2.5/weather";
    private  String _strCity = "";

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


    /**
     * Parse Service received String and Populate \n
     * properties with the values.
     * @param   strInput JSON from the service.
     */
    public void parseServiceCallback(String strInput) {
        Log.i("Tag", strInput);
        try {
            JSONObject jsonRoot = new JSONObject(strInput);
            if(strInput != null) {
                JSONObject sys = jsonRoot.getJSONObject("sys");
                country = sys.getString("country");

                JSONObject main = jsonRoot.getJSONObject("main");
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
    cod	"404"
    message	"city not found"
     //URL url = new URL(_strUrl + "?q=" + _strCity+ "&APPID=" + _strAppID);

    */

    public  void fetchJSON() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected Void doInBackground(Void... params) {
                try {

                    Log.v("Tag","Process Started.");

                    URL url = new URL(_strUrl + "?q=" + _strCity+ "&APPID=" + _strAppID);

                    Log.i("Tag",url.toString());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    StringBuffer json = new StringBuffer(1024);
                    String strRequestResult = "";

                    Log.v("Tag","Fetching Data.");

                    while ((strRequestResult = reader.readLine()) != null)
                        json.append(strRequestResult).append("\n");
                    reader.close();

                    _data = new JSONObject(json.toString());

                    Log.v("Tag",_data.toString());

                    if (_data.getInt("cod") != 200) {
                        Log.i("Tag","Cancelled, Unexpected Error has occurred");
                        _data = null;
                        return null;
                    }
                    else
                        Log.v("Tag","Fetched Successfully.");


                    if(_data.getString("cod") != null)
                        parseServiceCallback(_data.toString());


                } catch (Exception e) {
                    Log.v("Tag", e.getMessage());
                    return null;
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void Void) {
                if (_data != null) {
                    Log.v("Tag", "My weather received.");
                }
                else
                {
                    Log.v("Tag", "Unexpected Error.");
                }
            }
        }.execute();
    }


    /**
     * Get Service String call back result.
     * properties with the values.
     */
    /*
    public  boolean getServiceCall() {
        new AsyncTask<Void,Void,Boolean>()
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //Check the Place.
                /*
                if(_strCity )
                {

                }


            }

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    Log.v("Tag","Process Started.");

                    URL url = new URL(_strUrl + "?q=" + _strCity+ "&APPID=" + _strAppID);

                    Log.i("Tag",url.toString());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    StringBuffer json = new StringBuffer(1024);
                    String strRequestResult = "";

                    Log.v("Tag","Fetching Data.");

                    while ((strRequestResult = reader.readLine()) != null)
                        json.append(strRequestResult).append("\n");
                    reader.close();

                    _data = new JSONObject(json.toString());

                    Log.v("Tag",_data.toString());

                    if (_data.getInt("cod") != 200) {
                        Log.i("Tag","Cancelled, Unexpected Error has occurred");
                        _data = null;
                        return false;
                    }
                    else
                    {
                        parseServiceCallback(_data.toString());
                        return true;
                    }

                } catch (Exception e) {
                    Log.v("Tag", e.getMessage());
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Void Void) {}
        }.execute();

    }
  */
}