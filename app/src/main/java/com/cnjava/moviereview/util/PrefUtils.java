package com.cnjava.moviereview.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.cnjava.moviereview.MyApplication;

public class PrefUtils {

    private static final String PREF_FILE = "pref_saving";
    private static PrefUtils instance;

    private PrefUtils() {
        //for singleton
    }

    public static PrefUtils getInstance() {
        if (instance == null) {
            instance = new PrefUtils();
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
