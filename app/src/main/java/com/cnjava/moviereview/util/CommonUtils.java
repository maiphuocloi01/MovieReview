package com.cnjava.moviereview.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cnjava.moviereview.MyApplication;

public class CommonUtils {

    private static final String PREF_FILE = "pref_saving";
    private static CommonUtils instance;

    private CommonUtils() {
        //for singleton
    }

    public static CommonUtils getInstance() {
        if (instance == null) {
            instance = new CommonUtils();
        }
        return instance;
    }

    public void savePref(String key, String value) {
        SharedPreferences pref = MyApplication.getInstance().getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        pref.edit().putString(key, value).apply();
    }

    public String getPref(String key) {
        SharedPreferences pref = MyApplication.getInstance().getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        return pref.getString(key, null);
    }

    public void clearPref(String key) {
        SharedPreferences pref = MyApplication.getInstance().getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        pref.edit().remove(key).apply();
    }

    public static boolean isInternetOn(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static String versionToComparable( String version )
    {
        String versionNum = version;
        int at = version.indexOf( '-' );
        if ( at >= 0 )
            versionNum = version.substring( 0, at );
        String[] numAr = versionNum.split( "\\." );
        String versionFormatted = "0";
        for ( String tmp : numAr ){
            versionFormatted += String.format( "%4s", tmp ).replace(' ', '0');
        }
        while ( versionFormatted.length() < 12 )  {
            versionFormatted += "0000";
        }
        return versionFormatted;
    }

    public static String getVersionName(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return Constants.EMPTY_STRING;
    }
}
