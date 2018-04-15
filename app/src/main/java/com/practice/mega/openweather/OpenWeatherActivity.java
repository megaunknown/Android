package com.practice.mega.openweather;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.practice.mega.openweather.Auxiliaries.auxFunctions;
import com.practice.mega.openweather.Auxiliaries.auxUI;
import com.practice.mega.openweather.OpenWeatherService.OpenWeatherService;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/*
Main Activity
Developed By: Mohamed Abdelaziz
E-mail : Mohamedsaleh1984@hotmail.com
*/
public class OpenWeatherActivity extends AppCompatActivity {
    final  String _strAppID = "7a59ab38677b2b24aba6f6dab13a2ac4";
    final   String _strUrl = "https://api.openweathermap.org/data/2.5/weather";
    final String _strCity="";
    //Dictionary for Countries Codes-=>Names.
    private Map<String, String> _dCountries;

    //Radio Button.
    private RadioButton _radioButtonF, _radioButtonC;
    private RadioGroup _radioGroup;

    private Button _btnGetWeatherInfo;
    private EditText _location;
    private TextView _country, _temperature, _humidity, _pressure, _minTemp, _maxTemp;
    private String _strLocation,_strCountry,_strTemp, _strHumidity, _strPressure, _strMinTemp, _strMaxTemp;
    private OpenWeatherService obj;
    private char _currentMeasure = 'f';
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_weather);

        //Check Internet Connectivity.
        if (!auxFunctions.isNetworkAvailable(this)) {
            auxUI.showToast(this, "You should be connected to the internet.");
        }

        if (savedInstanceState != null) {
            // Restore value of members from saved state
            /*
            _strLocation = _location.getText().toString();
            _strCountry = _country.getText().toString();
            _strTemp  = _temperature.getText().toString();
            _strHumidity =  _humidity.getText().toString();
            _strPressure =  _pressure.getText().toString();
            _strMinTemp =  _minTemp.getText().toString();
            _strMaxTemp = _maxTemp.getText().toString();
            */
        }
        linkUIWidgets();
    }

    public void linkUIWidgets()
    {
        //Radio Group.
        _radioGroup = findViewById(R.id.radioGroupTemp);
        _radioGroup.setVisibility(View.GONE);

        //Spinner Settings
        progress=new ProgressDialog(OpenWeatherActivity.this);
        progress.setMessage("Fetching Weather Info...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);


        //Button.
        _btnGetWeatherInfo = findViewById(R.id.btnGetWeather);

        //Main Edit Text Boxes.
        _location = findViewById(R.id.editTextLocation);
        _country = findViewById(R.id.editTextCountry);
        _temperature = findViewById(R.id.editTextTemp);
        _humidity = findViewById(R.id.editTextHumidty);
        _pressure = findViewById(R.id.editTextPressure);
        _minTemp = findViewById(R.id.edittextMinTemp);
        _maxTemp = findViewById(R.id.edittextMaxTemp);

        //Temp Type.
        _radioButtonF = findViewById(R.id.radioButtonFer);
        _radioButtonC = findViewById(R.id.radioButtonDeg);

        //Load Dictionary Data.
        try {
            populateDictionary();
        } catch (Exception ex) {

        }
    }
    //Get City Temp Info
    public void GetWeatherInfo(View v) {
        String strCity = _location.getText().toString();
        //auxUI.showToast(OpenWeatherActivity.this, strCity);

        if (auxFunctions.isNetworkAvailable(OpenWeatherActivity.this)) {
            if (!auxFunctions.isContainsNumberSpecialChars(strCity)) {
                if (strCity.length() > 3) {
                    fetchJSON(strCity);

                } else {
                    auxUI.showToast(this, "Location is so short.");
                }
            } else {
                auxUI.showToast(this, "Please check city name.");
            }
        } else {
            auxUI.showToast(this, "You should be connected to the internet.");
        }
    }
    /**
     *Fetch OpenWeather Result.
     *returns cod "200" if the result is OK [Successful]
     *returns cod "404" if the result is NOT OK [Failed]
     */
    public  void fetchJSON(String strCity) {
        //Fetch Service Data
        obj = new OpenWeatherService(strCity);
        final String c = strCity;
        new AsyncTask<Void, Void, Void>() {

            @Override protected void onPreExecute() {
                super.onPreExecute();
                //auxUI.showToast(OpenWeatherActivity.this, "Please wait to fetch Weather info.");
                _btnGetWeatherInfo.setEnabled(false);
                //Show the progress Dialog.
                progress.show();
            }


            @Override protected Void doInBackground(Void... params) {
                try {

                    Log.v("Tag","Process Started.");

                    URL url = new URL(_strUrl + "?q=" + c + "&APPID=" + _strAppID);

                    Log.i("Tag",url.toString());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    StringBuffer stringBuffer = new StringBuffer(1024);
                    String strRequestResult = "";

                    Log.v("Tag","Fetching Data.");

                    while ((strRequestResult = reader.readLine()) != null)
                        stringBuffer.append(strRequestResult).append("\n");
                    reader.close();

                    JSONObject jsonObject = new JSONObject(stringBuffer.toString());
                    obj.setData(jsonObject);
                } catch (Exception e) {
                    Log.v("Tag", e.getMessage());
                    return null;
                }

                return null;
            }


            @Override protected void onPostExecute(Void Void) {
                if (obj.get_data() != null && obj.isOk()) {
                    //Hide the Progress Dialog
                    progress.dismiss();
                    _btnGetWeatherInfo.setEnabled(true);

                    //Set the info to the Controls.
                    _location.setText(obj.getCity());
                    _country.setText(getCountryName(obj.getCountry()));// rising and error
                    _temperature.setText(String.valueOf(obj.getTemperature()));
                    _humidity.setText(String.valueOf(obj.getHumidity()));
                    _pressure.setText(String.valueOf(obj.getPressure()));
                    _minTemp.setText(String.valueOf(obj.getMinTemperature()));
                    _maxTemp.setText(String.valueOf(obj.getMaxTemperature()));
                }
                else
                {
                    Log.v("Tag", "Unexpected Error.");
                }
            }
        }.execute();
    }

    //Switch Temp
    public void switchFC(View v) {
        int ID = v.getId();
        if (ID == R.id.radioButtonDeg) {
            if (_currentMeasure == 'f') {
                _temperature.setText(String.valueOf(auxFunctions.fahrenheitToCelsius(Double.valueOf(_temperature.getText().toString()))));
                _minTemp.setText(String.valueOf(auxFunctions.fahrenheitToCelsius(Double.valueOf(_minTemp.getText().toString()))));
                _maxTemp.setText(String.valueOf(auxFunctions.fahrenheitToCelsius(Double.valueOf(_maxTemp.getText().toString()))));
                _currentMeasure = 'c';
            }

        } else if (ID == R.id.radioButtonFer) {
            if (_currentMeasure == 'c') {
                _temperature.setText(String.valueOf(auxFunctions.celsiusToFahrenheit(Double.valueOf(_temperature.getText().toString()))));
                _minTemp.setText(String.valueOf(auxFunctions.celsiusToFahrenheit(Double.valueOf(_minTemp.getText().toString()))));
                _maxTemp.setText(String.valueOf(auxFunctions.celsiusToFahrenheit(Double.valueOf(_maxTemp.getText().toString()))));
                _currentMeasure = 'f';
            }
        }
    }

    /**
     * Populate the dictionary from the raw file.
     * @throws  IOException if the raw file is not exisit
     */
    public void populateDictionary() throws IOException {
        String[] strLines = auxFunctions.readRawFileContent(R.raw.countries, this).split("\n");
        _dCountries = new HashMap<String, String>();
        for (int i = 0; i < strLines.length; i++) {
            String[] KeyVal = strLines[i].split("        ");
            _dCountries.put(KeyVal[0], KeyVal[1]);
        }
    }

    /**
     * Get the country name.
     * @param   strCountryCode The Country ISO Code like US, EG..etc
     */
    public String getCountryName(String strCountryCode) {
        String strResult = "nil";
        if(_dCountries.containsKey(strCountryCode))
            strResult = _dCountries.get(strCountryCode);
        return strResult;
    }

    @Override protected void onStart() {
        super.onStart();
    }

    @Override protected void onStop() {super.onStop(); }

    @Override protected void onDestroy() {
        super.onDestroy();
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("_country", _strCountry);
        outState.putString("_strTemp", _strTemp);
        outState.putString("_strHumidity", _strHumidity);
        outState.putString("_strPressure", _strPressure);
        outState.putString("_strMinTemp", _strMinTemp);
        outState.putString("_strMaxTemp", _strMaxTemp);
    }

    @Override protected void onRestoreInstanceState(Bundle savedInstanceState) { super.onRestoreInstanceState(savedInstanceState); }

    @Override protected void onRestart() {
        super.onRestart();
        //Load Dictionary Data.
        try {
            populateDictionary();
        } catch (Exception ex) {

        }
    }

    public void Info(View view)
    {
        AboutBox.Show(this);
    }
}

/*For Rotation */
                    /*
                    _strCountry = _country.getText().toString();
                    _strTemp  = _temperature.getText().toString();
                     _strHumidity =  _humidity.getText().toString();
                     _strPressure =  _pressure.getText().toString();
                     _strMinTemp =  _minTemp.getText().toString();
                     _strMaxTemp = _maxTemp.getText().toString();
                     */