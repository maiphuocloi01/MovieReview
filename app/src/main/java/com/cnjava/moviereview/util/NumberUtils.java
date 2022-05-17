package com.cnjava.moviereview.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

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
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }

}
