package com.jlc;

import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;
import net.objecthunter.exp4j.operator.Operator;

import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MathEngine {

    private static final Set<String> ALLOWED_IDENTIFIERS = Set.of(
            "sqrt", "cbrt", "sin", "cos", "tan", "cot", "asin", "acos", "atan",
            "sinh", "cosh", "tanh", "abs", "log", "log10", "log2", "ln", "exp",
            "floor", "ceil", "fact", "factorial", "pi", "e", "min", "max", "rad", "deg",
            "gcd", "lcm", "fib", "isprime"
    );

    private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("[a-zA-Z][a-zA-Z0-9]*");
    private static final Pattern ALLOWED_CHARACTERS = Pattern.compile("^[a-zA-Z0-9+\\-*/^!%().,\\s°eE]+$");

    private static final Operator FACTORIAL_OP = new Operator("!", 1, true, Operator.PRECEDENCE_POWER + 1) {
        @Override
        public double apply(double... args) {
            int n = (int) args[0];
            if (n < 0 || n != args[0]) {
                throw new IllegalArgumentException("Operand for factorial must be a non-negative integer");
            }
            double res = 1;
            for (int i = 1; i <= n; i++) {
                res *= i;
            }
            return res;
        }
    };

    private static final Function FACT_FUNC = new Function("fact", 1) {
        @Override
        public double apply(double... args) {
            int n = (int) args[0];
            if (n < 0 || n != args[0]) {
                throw new IllegalArgumentException("Operand for factorial must be a non-negative integer");
            }
            double res = 1;
            for (int i = 1; i <= n; i++) {
                res *= i;
            }
            return res;
        }
    };

    private static final Function FACTORIAL_FUNC = new Function("factorial", 1) {
        @Override
        public double apply(double... args) {
            int n = (int) args[0];
            if (n < 0 || n != args[0]) {
                throw new IllegalArgumentException("Operand for factorial must be a non-negative integer");
            }
            double res = 1;
            for (int i = 1; i <= n; i++) {
                res *= i;
            }
            return res;
        }
    };

    private static final Function LN_FUNC = new Function("ln", 1) {
        @Override
        public double apply(double... args) {
            if (args[0] <= 0) {
                throw new IllegalArgumentException("Logarithm undefined for non-positive numbers");
            }
            return Math.log(args[0]);
        }
    };

    private static final Function MIN_FUNC = new Function("min", 2) {
        @Override
        public double apply(double... args) {
            return Math.min(args[0], args[1]);
        }
    };

    private static final Function MAX_FUNC = new Function("max", 2) {
        @Override
        public double apply(double... args) {
            return Math.max(args[0], args[1]);
        }
    };

    private static final Function RAD_FUNC = new Function("rad", 1) {
        @Override
        public double apply(double... args) {
            return Math.toRadians(args[0]);
        }
    };

    private static final Function DEG_FUNC = new Function("deg", 1) {
        @Override
        public double apply(double... args) {
            return Math.toDegrees(args[0]);
        }
    };

    private static final Function GCD_FUNC = new Function("gcd", 2) {
        @Override
        public double apply(double... args) {
            long a = Math.round(args[0]);
            long b = Math.round(args[1]);
            while (b != 0) {
                long t = b;
                b = a % b;
                a = t;
            }
            return Math.abs(a);
        }
    };

    private static final Function LCM_FUNC = new Function("lcm", 2) {
        @Override
        public double apply(double... args) {
            long a = Math.round(args[0]);
            long b = Math.round(args[1]);
            if (a == 0 || b == 0) return 0;
            long gcdVal = Math.round(GCD_FUNC.apply(a, b));
            return Math.abs((a / gcdVal) * b);
        }
    };

    private static final Function FIB_FUNC = new Function("fib", 1) {
        @Override
        public double apply(double... args) {
            int n = (int) Math.round(args[0]);
            if (n <= 0) return 0;
            if (n == 1 || n == 2) return 1;
            long a = 1, b = 1;
            for (int i = 3; i <= n; i++) {
                long c = a + b;
                a = b;
                b = c;
            }
            return b;
        }
    };

    private static final Function ISPRIME_FUNC = new Function("isprime", 1) {
        @Override
        public double apply(double... args) {
            long n = Math.round(args[0]);
            if (n < 2) return 0;
            if (n == 2 || n == 3) return 1;
            if (n % 2 == 0 || n % 3 == 0) return 0;
            for (long i = 5; i * i <= n; i += 6) {
                if (n % i == 0 || n % (i + 2) == 0) return 0;
            }
            return 1;
        }
    };

    public static boolean isSafe(String expr, Set<String> customVariables) {
        if (expr == null || expr.isBlank()) return false;
        if (!ALLOWED_CHARACTERS.matcher(expr).matches()) {
            return false;
        }
        Matcher matcher = IDENTIFIER_PATTERN.matcher(expr);
        while (matcher.find()) {
            String id = matcher.group().toLowerCase();
            if (id.matches("^e-?\\d+$") || id.equals("e")) {
                continue; // scientific notation like 1e-11 or e constant
            }
            if (!ALLOWED_IDENTIFIERS.contains(id) && (customVariables == null || !customVariables.contains(id))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isSafe(String expr) {
        return isSafe(expr, null);
    }

    public static double evaluate(String expression) {
        return evaluate(expression, null);
    }

    public static double evaluate(String expression, Map<String, Double> variables) {
        if (expression == null || expression.isBlank()) {
            throw new IllegalArgumentException("Empty expression");
        }

        String cleaned = expression
                .replace('×', '*')
                .replace('÷', '/')
                .replaceAll("(\\d+(?:\\.\\d+)?)\\s*°", "rad($1)")
                .replaceAll("(\\d+(?:\\.\\d+)?)\\s*\\*\\s*10\\^\\(?(-?\\d+)\\)?", "$1e$2")
                .trim();

        Set<String> varKeys = variables == null ? null : variables.keySet();
        if (!isSafe(cleaned, varKeys)) {
            throw new IllegalArgumentException("Unsafe expression: " + expression);
        }

        ExpressionBuilder builder = new ExpressionBuilder(cleaned)
                .operator(FACTORIAL_OP)
                .function(FACT_FUNC)
                .function(FACTORIAL_FUNC)
                .function(LN_FUNC)
                .function(MIN_FUNC)
                .function(MAX_FUNC)
                .function(RAD_FUNC)
                .function(DEG_FUNC)
                .function(GCD_FUNC)
                .function(LCM_FUNC)
                .function(FIB_FUNC)
                .function(ISPRIME_FUNC);

        if (variables != null && !variables.isEmpty()) {
            builder.variables(variables.keySet());
        }

        net.objecthunter.exp4j.Expression exp = builder.build();

        if (variables != null && !variables.isEmpty()) {
            for (Map.Entry<String, Double> entry : variables.entrySet()) {
                exp.setVariable(entry.getKey(), entry.getValue());
            }
        }

        double result = exp.evaluate();
        if (Double.isNaN(result) || Double.isInfinite(result)) {
            throw new ArithmeticException("Division by zero or invalid operation");
        }

        // Safe precision handling without Long.MAX_VALUE overflow or zeroing small scientific floats
        if (result != 0 && (Math.abs(result) >= 1e7 || Math.abs(result) < 1e-4)) {
            return result;
        }
        if (result == 0 || Math.abs(result - Math.rint(result)) < 1e-11) {
            return Math.rint(result);
        }
        return Math.round(result * 1e12) / 1e12;
    }
}
