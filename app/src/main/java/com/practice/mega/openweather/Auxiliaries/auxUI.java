package com.practice.mega.openweather.Auxiliaries;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class auxUI {
    /**
     * Display long Toast.
     *
     * @param c          Context handler
     * @param strMessage Message to view viewed
     */
    public static void showToast(Context c, String strMessage) {
        Toast.makeText(c, strMessage, Toast.LENGTH_LONG).show();
    }

    public static void makeCall(String strTele)
    {
        Uri number = Uri.parse("tel:" + strTele);
        Intent callIntent = new Intent(Intent.ACTION_DIAL,number);
    }

    public static void openURI(String strURI)
    {
        Uri webPage = Uri.parse(strURI);
        Intent callIntent = new Intent(Intent.ACTION_VIEW,webPage);
    }

    public static void openLocation(String strLocation)
    {
        Uri location = Uri.parse(strLocation);
        Intent callIntent = new Intent(Intent.ACTION_VIEW,location);
    }

    public static void playSound(Context cnn, String strFilName)
    {
        /*
        int iResoureID  =  auxFunctions.getResourceID(auxFunctions.ResourceType.RAW,strFilName);
        MediaPlayer mp = MediaPlayer.create(cnn,iResoureID);
        */
    }

    public static ArrayList<String> getPictures()
    {
        ArrayList<String> strings = new ArrayList<String>();
        File picDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        for (File f:picDir.listFiles())
            strings.add(f.getAbsolutePath());
        return strings;
    }

    public static ArrayList<String> getVideos()
    {
        ArrayList<String> strings = new ArrayList<String>();
        File picDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);

        for (File f:picDir.listFiles())
            strings.add(f.getAbsolutePath());
        return strings;
    }

    public static ArrayList<String> getMusic()
    {
        ArrayList<String> strings = new ArrayList<String>();
        File picDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);

        for (File f:picDir.listFiles())
            strings.add(f.getAbsolutePath());
        return strings;
    }
}
