package com.cnjava.moviereview.util;

import java.text.DecimalFormat;

public class NumberUtils {

    public static String convertCurrency(long currency) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(currency) + " $";
    }

}
