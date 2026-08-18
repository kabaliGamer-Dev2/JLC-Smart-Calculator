package com.jlc;

import java.math.BigInteger;

public class BaseConverter {

    private static final int[] ROMAN_VALUES = { 1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1 };
    private static final String[] ROMAN_SYMBOLS = { "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I" };

    public static String toBinary(String decimalStr) {
        BigInteger bi = new BigInteger(decimalStr.trim());
        return bi.toString(2);
    }

    public static String toHex(String decimalStr) {
        BigInteger bi = new BigInteger(decimalStr.trim());
        return bi.toString(16).toUpperCase();
    }

    public static String toOctal(String decimalStr) {
        BigInteger bi = new BigInteger(decimalStr.trim());
        return bi.toString(8);
    }

    public static String fromHexToDecimal(String hexStr) {
        String clean = hexStr.trim().replaceFirst("^(0x|0X)", "");
        BigInteger bi = new BigInteger(clean, 16);
        return bi.toString(10);
    }

    public static String fromBinToDecimal(String binStr) {
        String clean = binStr.trim().replaceFirst("^(0b|0B)", "");
        BigInteger bi = new BigInteger(clean, 2);
        return bi.toString(10);
    }

    public static String fromBinToHex(String binStr) {
        String clean = binStr.trim().replaceFirst("^(0b|0B)", "");
        BigInteger bi = new BigInteger(clean, 2);
        return bi.toString(16).toUpperCase();
    }

    public static String toRoman(int num) {
        if (num <= 0 || num > 3999) {
            throw new IllegalArgumentException("Roman numerals only supported for numbers between 1 and 3999");
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ROMAN_VALUES.length; i++) {
            while (num >= ROMAN_VALUES[i]) {
                num -= ROMAN_VALUES[i];
                sb.append(ROMAN_SYMBOLS[i]);
            }
        }
        return sb.toString();
    }

    public static int fromRoman(String roman) {
        String s = roman.toUpperCase().trim();
        int sum = 0;
        int prev = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            int val = romanCharVal(s.charAt(i));
            if (val < prev) {
                sum -= val;
            } else {
                sum += val;
                prev = val;
            }
        }
        return sum;
    }

    private static int romanCharVal(char c) {
        switch (c) {
            case 'I': return 1;
            case 'V': return 5;
            case 'X': return 10;
            case 'L': return 50;
            case 'C': return 100;
            case 'D': return 500;
            case 'M': return 1000;
            default: throw new IllegalArgumentException("Invalid Roman numeral character: " + c);
        }
    }
}
