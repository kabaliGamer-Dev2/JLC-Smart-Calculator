package com.jlc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FallbackParser {

    private static final Pattern CHAIN =
            Pattern.compile("([a-zA-Z0-9().,\\s+\\-*/^!%]+)");

    public static String extractWithRegex(String input) {
        if (input == null) return null;
        String trimmed = input.trim();
        if (trimmed.startsWith("\"") && trimmed.endsWith("\"")) {
            trimmed = trimmed.substring(1, trimmed.length() - 1).trim();
        }
        if (MathEngine.isSafe(trimmed)) {
            return trimmed;
        }

        String ruleApplied = MemoryStore.getInstance().applyRules(trimmed);
        if (MathEngine.isSafe(ruleApplied)) {
            return ruleApplied;
        }

        String normalized = ruleApplied.trim()
                .replaceAll("(?i)\\bwhat is the square root of\\s+", "sqrt(")
                .replaceAll("(?i)\\bwhat is square root of\\s+", "sqrt(")
                .replaceAll("(?i)\\bsquare root of\\s+([a-zA-Z0-9().]+)", "sqrt($1)")
                .replaceAll("(?i)\\bcube root of\\s+([a-zA-Z0-9().]+)", "cbrt($1)")
                .replaceAll("(?i)\\bmultiply\\s+([0-9.]+)\\s+by\\s+([0-9.]+)", "$1 * $2")
                .replaceAll("(?i)\\bdivide\\s+([0-9.]+)\\s+by\\s+([0-9.]+)", "$1 / $2")
                .replaceAll("(?i)\\bhalf of\\s+([0-9.]+)", "$1 / 2")
                .replaceAll("(?i)\\bdouble\\s+([0-9.]+)", "2 * $1")
                .replaceAll("(?i)\\btriple\\s+([0-9.]+)", "3 * $1")
                .replaceAll("(?i)\\bsubtract\\s+([0-9.]+)\\s+from\\s+([0-9.]+)", "$2 - $1")
                .replaceAll("(?i)\\b([0-9.]+)\\s*percent of\\s+([0-9.]+)", "($1 / 100) * $2")
                .replaceAll("(?i)\\bsquare\\s+([0-9.]+)", "$1^2")
                .replaceAll("(?i)\\bcube\\s+([0-9.]+)", "$1^3")
                .replaceAll("(?i)\\braise\\s+([0-9.]+)\\s+to the power of\\s+([0-9.]+)", "$1^$2")
                .replaceAll("(?i)\\bfind\\s+([0-9.]+)\\s+factorial", "$1!")
                .replaceAll("(?i)\\badd\\s+([0-9.]+),\\s*([0-9.]+),\\s*and\\s+([0-9.]+)", "$1 + $2 + $3")
                .replace("divided by", "/")
                .replace("multiplied by", "*")
                .replace("to the power of", "^")
                .replace("to the power", "^")
                .replace("power of", "^")
                .replace("factorial of", "fact")
                .replace("factorial", "!")
                .replace("divide", "/")
                .replace("multiply", "*")
                .replace("quotient", "/")
                .replace("product", "*")
                .replace("difference", "-")
                .replace("subtract", "-")
                .replace("minus", "-")
                .replace("plus", "+")
                .replace("times", "*")
                .replace("over", "/")
                .replace("add", "+")
                .replace("sum", "+")
                .replace("÷", "/")
                .replace("×", "*")
                .replaceAll("\\bless\\b", "-")
                .replaceAll("\\bx\\b", "*");

        if (normalized.contains("sqrt(") && !normalized.contains(")")) {
            normalized += ")";
        }

        if (MathEngine.isSafe(normalized)) {
            return normalized;
        }

        Matcher m = Pattern.compile("(\\d+(?:\\.\\d+)?)(?:\\s*([+\\-*/^!])\\s*(\\d+(?:\\.\\d+)?))+").matcher(normalized);
        if (m.find()) {
            String expr = m.group().trim();
            if (MathEngine.isSafe(expr)) return expr;
        }

        return null;
    }
}
