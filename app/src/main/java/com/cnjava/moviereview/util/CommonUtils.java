package com.cnjava.moviereview.util;

import android.content.Context;
import android.content.SharedPreferences;

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
}
