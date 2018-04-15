package com.practice.mega.openweather.Auxiliaries;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

/*
Auxiliary Functions.
Developed By: Mohamed Abdelaziz
Email: mohamedsaleh1984@hotmail.com
*/
public class auxFunctions {

    //ResourceType Enum.
    public static enum ResourceType {
        STRINGS, IMAGE, RESOURCE,RAW
    }

    /**
     * Check if the Phone is connected to the Internet or NOT.
     * @param c Context handler
     * @return True, otherwise false.
     */
    public static boolean isNetworkAvailable(Context c) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    /**
     * Get the resource id.
     * @param cnn Context handler
     * @param r Resource Type [String,Image..]
     * @param rsname The resource name.
     * @throws Exception if the parameters are not filled properly.
     * @return Resource ID
     */
    public static int getResourceID(Context cnn, ResourceType r, String rsname) throws Exception {
        if (cnn == null || r == null || rsname.length() == 0)
            throw new Exception("Arguments are not assiged properly");

        int resID = 0;
        switch (r) {
            case IMAGE:
                resID = cnn.getResources().getIdentifier(rsname, "drawable", cnn.getPackageName());
                break;
            case RESOURCE:
                resID = cnn.getResources().getIdentifier(rsname, "id", cnn.getPackageName());
                break;
            case STRINGS:
                resID = cnn.getResources().getIdentifier(rsname, "string", cnn.getPackageName());
            case RAW:
                resID = cnn.getResources().getIdentifier(rsname, "raw", cnn.getPackageName());
                break;
        }
        return resID;
    }

    /**
     * Read the content of raw file.
     * @param   resourceId Raw file id
     * @param   cnn Activity
     * @throws IOException in case of
     * @return File Content as String.
     */
    public static String readRawFileContent(int resourceId, Context cnn) throws IOException {
        StringBuilder strbuilder = new StringBuilder();
        //Fetch Raw Resource File
        InputStream in = cnn.getResources().openRawResource(resourceId);
        int iCharReadCode = 0;
        while ((iCharReadCode = in.read()) > 0) {
            strbuilder.append((char) (iCharReadCode));
        }
        in.close();
        return strbuilder.toString();
    }

    /**
     * Check if the string contains special characters or digits.
     * @param   strInput Given input string.
     * @return True, otherwise false
     */
    public static boolean isContainsNumberSpecialChars(String strInput)
    {
        Boolean bResult  =  false;
        //RegEx can be used but for Time saving, I implemented it this way.
        String strSpecialChars = ",.0123456789-~[]!-/^%$#@)(*&{}|\\/<>\"";
        for(int i=  0 ; i < strInput.length() ; i++) {
            for (int j = 0; j < strSpecialChars.length(); j++) {
                if (strInput.charAt(i) == strSpecialChars.charAt(j)) {
                    bResult = true;
                    break;
                }
            }
            if (bResult)
                break;
        }
        return bResult;
    }



    //Fahrenheit to Celsius conversion.
    public static double fahrenheitToCelsius(double f) {
        double celsiusTemp = 0;
        celsiusTemp = (f - 32) * (5 / 9);
        return celsiusTemp;
    }

    //Celsius to Fahrenheit conversion.
    public static double celsiusToFahrenheit(double c) {
        double fahrenheitTemp = 0;
        fahrenheitTemp = (c * (9 / 5)) + 32;
        return fahrenheitTemp;
    }
}
