package com.cnjava.moviereview.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {

    private static final int YEAR = 365 * 24 * 60 * 60;

    private static final int MONTH = 30 * 24 * 60 * 60;

    private static final int DAY = 24 * 60 * 60;

    private static final int HOUR = 60 * 60;

    private static final int MINUTE = 60;

    public static String getDescriptionTimeFromTimestamp(String strDate) {

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH);
        try {
            Date date = format.parse(strDate);
            SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd MMM", Locale.ENGLISH);
            SimpleDateFormat sdf3 = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
            String timeHour = sdf1.format(date);
            String timeMonth = sdf2.format(date);
            String timeYear = sdf2.format(date);

            long timestamp = date.getTime();
            long currentTime = System.currentTimeMillis();
            long timeGap = (currentTime - timestamp) / 1000;
            System.out.println("timeGap: " + timeGap);
            String timeStr;
            if (timeGap > YEAR) {
                /*if (timeGap / YEAR == 1){
                    timeStr = timeGap / YEAR + " year ago";
                } else {
                    timeStr = timeGap / YEAR + " years ago";
                }*/
                timeStr = timeYear;
            } else if (timeGap > MONTH) {
                /*if (timeGap / MONTH == 1){
                    timeStr = timeGap / MONTH + " month ago";
                } else {
                    timeStr = timeGap / MONTH + " months ago";
                }*/
                timeStr = timeMonth + " at " + timeHour;
            } else if (timeGap > DAY) {
                if (timeGap / DAY == 1){
                    timeStr = "Yesterday at " + timeHour;
                } else {
                    timeStr = timeGap / DAY + " days ago at " + timeHour;
                }
            } else if (timeGap > HOUR) {
                if (timeGap / HOUR == 1){
                    timeStr = timeGap / HOUR + " hour ago";
                } else {
                    timeStr = timeGap / HOUR + " hours ago";
                }
            } else if (timeGap > MINUTE) {
                if (timeGap / MINUTE == 1){
                    timeStr = timeGap / MINUTE + " minute ago";
                } else {
                    timeStr = timeGap / MINUTE + " minutes ago";
                }
            } else {
                timeStr = "Now";
            }
            return timeStr;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strDate;
    }

}
