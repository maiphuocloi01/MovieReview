package com.cnjava.moviereview;

import android.app.Application;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class MyApplication extends Application {
    private static MyApplication instance;
    private Storage storage;

    @Override
    public void onCreate() {
        super.onCreate();
        storage = new Storage();
        instance = this;
    }

    public Storage getStorage() {
        return storage;
    }

    public static MyApplication getInstance() {
        if (instance == null){
            instance = new MyApplication();
        }
        return instance;
    }
}
