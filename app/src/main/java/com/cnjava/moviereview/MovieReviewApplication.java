package com.cnjava.moviereview;

import android.app.Application;

public class MovieReviewApplication extends Application {
    private static MovieReviewApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static MovieReviewApplication getInstance() {
        return instance;
    }
}
