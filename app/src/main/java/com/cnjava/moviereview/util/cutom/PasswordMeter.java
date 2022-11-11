package com.cnjava.moviereview.util.cutom;

public class PasswordMeter {

    public static int passwordRanking(StringBuilder textToVerify) throws IllegalArgumentException {

        if (textToVerify == null) {
            throw new IllegalArgumentException();
        }
        int strength = -1;

        boolean containsUppercase = !textToVerify.equals(textToVerify.toString().toLowerCase());
        boolean containsLowercase = !textToVerify.equals(textToVerify.toString().toUpperCase());

        if (containsUppercase && containsLowercase) {
            strength += 2;
        } else {
            strength--;
        }

        int numDigits = getNumberDigits(textToVerify);
        if (numDigits > 0 && numDigits != textToVerify.length()) {
            strength++;
        }

        int numSymbols = getSymbols(textToVerify);
        if (numSymbols > 0 && numSymbols != textToVerify.length()) {
            strength++;
        }

        if (textToVerify.length() >= 8) {
            strength += 2;
        }
        return strength;
    }

    public static int getNumberDigits(StringBuilder inString) {
        if (isEmpty(inString)) {
            return 0;
        }
        int numDigits = 0;
        int length = inString.length();
        for (int i = 0; i < length; i++) {
            if (Character.isDigit(inString.charAt(i))) {
                numDigits++;
            }
        }
        return numDigits;
    }

    public static int getSymbols(StringBuilder inString) {
        if (isEmpty(inString)) {
            return 0;
        }
        int numDigits = 0;
        int length = inString.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isLetterOrDigit(inString.charAt(i))) {
                numDigits++;
            }
        }
        return numDigits;
    }

    public static boolean isEmpty(StringBuilder inString) {
        return inString == null || inString.length() == 0;
    }

}
