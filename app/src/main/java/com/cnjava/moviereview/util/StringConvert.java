package com.cnjava.moviereview.util;

import java.text.Normalizer;

public class StringConvert {

    public static String removeDiacriticalMarks(String string) {
        String result = Normalizer.normalize(string, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return result.replaceAll("[^A-Za-z0-9]","");
    }

    public static String CreateMD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString().toUpperCase();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }
}
