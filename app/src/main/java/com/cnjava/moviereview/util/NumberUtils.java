package com.cnjava.moviereview.util;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class NumberUtils {

    public static String convertCurrency(long currency) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(currency) + " $";
    }


    public static String convertDateType1(int dayOfMonth, int month, int year){
        return dayOfMonth + "/" + month + "/" + year;
    }

    public static String convertDateType2(int dayOfMonth, int month, int year){
        Date date = new GregorianCalendar(year, month, dayOfMonth).getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        return formatter.format(date);
    }

    public static String convertDateType3(String strDate){

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {
            Date date = format.parse(strDate);
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
            return sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strDate;

    }

    public static String convertDateType4(String strDate){

        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        try {
            Date date = format.parse(strDate);
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
            return sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strDate;

    }

    public static String convertDateType5(String strDate){

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {
            Date date = format.parse(strDate);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            return sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strDate;

    }

    public static String convertDateType6(String strDate){

        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        try {
            Date date = format.parse(strDate);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            return sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strDate;

    }

    public static String convertDateType7(String strDate){

        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH);
        try {
            Date date = format.parse(strDate);
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.ENGLISH);
            return sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strDate;

    }

    public static String getJsonFromAssets(Context context, String fileName) {
        String jsonString;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return jsonString;
    }

}
