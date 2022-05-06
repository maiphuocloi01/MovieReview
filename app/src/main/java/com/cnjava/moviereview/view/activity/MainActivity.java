package com.cnjava.moviereview.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.cnjava.moviereview.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_MovieReview);
        setContentView(R.layout.activity_main);
    }
}