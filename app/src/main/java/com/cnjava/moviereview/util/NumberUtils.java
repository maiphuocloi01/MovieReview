package com.cnjava.moviereview.util;

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

}
