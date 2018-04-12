package com.practice.mega.openweather;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*
Main Activity
Developed By: Mohamed Abdelaziz
E-mail : Mohamedsaleh1984@hotmail.com
*/
public class OpenWeatherActivity extends AppCompatActivity {

    //Dictionary for Countries Codes-=>Names.
    private Map<String, String> _dCountries;

    //Radio Button.
    private RadioButton _radioButtonF, _radioButtonC;
    private RadioGroup _radioGroup;

    private Button _btnGetWeatherInfo;
    private EditText _location, _country, _temperature, _humidity, _pressure, _minTemp, _maxTemp;
    private HandleJSON obj;
    private char _currentMeasure = 'f';
    private ProgressBar _spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_weather);

        //Check Internet Connectivity.
        if (!auxFunctions.isNetworkAvailable(this)) {
            auxFunctions.showToast(this, "You should be connected to the internet.");
        }

        //Radio Group.
        _radioGroup = (RadioGroup) findViewById(R.id.radioGroupTemp);
        _radioGroup.setVisibility(View.GONE);

        //Spinner Settings
        _spinner = (ProgressBar) findViewById(R.id.progressBarSpinner);
        _spinner.setVisibility(View.GONE);
        _spinner.setMax(100);
        //
        _btnGetWeatherInfo = (Button) findViewById(R.id.btnGetWeather);

        //Main Edit Text Boxses.
        _location = (EditText) findViewById(R.id.editTextLocation);
        _country = (EditText) findViewById(R.id.editTextCountry);
        _temperature = (EditText) findViewById(R.id.editTextTemp);
        _humidity = (EditText) findViewById(R.id.editTextHumidty);
        _pressure = (EditText) findViewById(R.id.editTextPressure);
        _minTemp = (EditText) findViewById(R.id.edittextMinTemp);
        _maxTemp = (EditText) findViewById(R.id.edittextMaxTemp);

        //Temp Type.
        _radioButtonF = (RadioButton) findViewById(R.id.radioButtonFer);
        _radioButtonC = (RadioButton) findViewById(R.id.radioButtonDeg);

        //Load Dictionary Data.
        try {
            populateDictionary();
        } catch (Exception ex) {

        }
    }

    //Get City Temp Info
    public void GetWeatherInfo(View v) {
        String strCity = _location.getText().toString();
        auxFunctions.showToast(this, strCity);

        if (auxFunctions.isNetworkAvailable(this)) {
            if (!auxFunctions.isContainsNumberSpecialChars(strCity)) {
                if (strCity.length() > 3) {
                    auxFunctions.showToast(this, "Please wait to fetch Weather info.");
                    _btnGetWeatherInfo.setEnabled(false);

                    //Set Waiting Dialog
                    _spinner.setVisibility(View.VISIBLE);
                    //Fetch Service Data
                    obj = new HandleJSON(_location.getText().toString());
                    obj.fetchJSON();
                    //While Parsing the data.
                    while (obj.parsingComplete) ;
                    //  _spinner.setMax(100);
                    _spinner.setVisibility(View.GONE);
                    _btnGetWeatherInfo.setEnabled(true);

                    //Set the info to the Controls.
                    _country.setText(getCountryName(obj.getCountry()));
                    _temperature.setText(String.valueOf(obj.getTemperature()));
                    _humidity.setText(String.valueOf(obj.getHumidity()));
                    _pressure.setText(String.valueOf(obj.getPressure()));
                    _minTemp.setText(String.valueOf(obj.getMinTemperature()));
                    _maxTemp.setText(String.valueOf(obj.getMaxTemperature()));
                } else {
                    auxFunctions.showToast(this, "Location is so short.");
                }
            } else {
                auxFunctions.showToast(this, "Please check city name.");
            }
        } else {
            auxFunctions.showToast(this, "You should be connected to the internet.");
        }
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

    //Populate the dictionary from the raw file.
    public void populateDictionary() throws IOException {
        String[] strLines = auxFunctions.readRawFileContent(R.raw.countries, this).split("\n");
        _dCountries = new HashMap<String, String>();
        for (int i = 0; i < strLines.length; i++) {
            String[] KeyVal = strLines[i].split("        ");
            _dCountries.put(KeyVal[0], KeyVal[1]);
        }
    }

    //Get the country name.
    public String getCountryName(String strCountryCode) {
        String strResult = _dCountries.get(strCountryCode);
        if( strResult.length() > 0 )
            return strResult;
        return "nil";
    }
}

