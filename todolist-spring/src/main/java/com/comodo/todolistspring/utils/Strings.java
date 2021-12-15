package com.comodo.todolistspring.utils;
import java.util.Arrays;

public final class Strings {

    public static final String EMPTY_STRING = "";

    private static final int PAD_LIMIT = 8192;

    private Strings() {
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static String repeat(char c, int count) {
        if (count < 1) return EMPTY_STRING;
        char[] chars = new char[count];
        Arrays.fill(chars, c);
        return new String(chars);
    }

    public static String rightPad(String str, int size) {
        return rightPad(str, size, ' ');
    }

    public static String rightPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }
        int pads = size - str.length();
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (pads > PAD_LIMIT) {
            return rightPad(str, size, String.valueOf(padChar));
        }
        return str.concat(repeat(padChar, pads));
    }

    public static String rightPad(String str, int size, String padStr) {
        if (str == null) {
            return null;
        }
        if (isNullOrEmpty(padStr)) {
            padStr = " ";
        }
        int padLen = padStr.length();
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (padLen == 1 && pads <= PAD_LIMIT) {
            return rightPad(str, size, padStr.charAt(0));
        }

        if (pads == padLen) {
            return str.concat(padStr);
        } else if (pads < padLen) {
            return str.concat(padStr.substring(0, pads));
        } else {
            char[] padding = new char[pads];
            char[] padChars = padStr.toCharArray();
            for (int i = 0; i < pads; i++) {
                padding[i] = padChars[i % padLen];
            }
            return str.concat(new String(padding));
        }
    }

    public static String subStringAfterLast(String str, String s) {
        if (isNullOrEmpty(str) || isNullOrEmpty(s))
            return str;
        final int pos = str.lastIndexOf(s);
        if (pos < 0)
            return str;
        else return str.substring(pos + s.length());
    }

}
