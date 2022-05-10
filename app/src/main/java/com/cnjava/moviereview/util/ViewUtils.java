package com.cnjava.moviereview.util;

import android.view.View;

public class ViewUtils {

    public static boolean isShow(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    public static boolean isGone(View view) {
        return view.getVisibility() == View.GONE;
    }

    public static boolean isInvisible(View view) {
        return view.getVisibility() == View.INVISIBLE;
    }

    public static void show(View view){
        view.setVisibility(View.VISIBLE);
    }

    public static void gone(View view){
        view.setVisibility(View.GONE);
    }

    public static void inv(View view){
        view.setVisibility(View.INVISIBLE);
    }


}
