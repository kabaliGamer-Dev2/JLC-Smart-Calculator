package com.jlc;

public class ResponseFormatter {

    public static String formatNumber(double val) {
        if (Double.isNaN(val) || Double.isInfinite(val)) return String.valueOf(val);
        if (Math.abs(val) >= 1e12 || (Math.abs(val) < 1e-4 && Math.abs(val) > 0)) {
            return String.valueOf(val);
        }
        if (val == Math.floor(val) && !Double.isInfinite(val) && Math.abs(val) < 1e12) {
            return String.format(java.util.Locale.US, "%.0f", val);
        }
        String s = String.format(java.util.Locale.US, "%.10f", val);
        s = s.replaceAll("0+$", "").replaceAll("\\.$", "");
        return s;
    }

    public static String formatResult(String expression, double result) {
        return "Expression: " + expression + " | Result: " + formatNumber(result);
    }

    public static String formatError() {
        return "I couldn't understand that calculation. Try: 5 + 3";
    }

    public static String formatDivByZero() {
        return "Cannot divide by zero.";
    }

    public static String formatInvalid() {
        return "I couldn't understand that calculation. Try: 5 + 3";
    }

    public static String formatNoMath() {
        return "That doesn't look like a math problem. Try asking a calculation!";
    }
}
