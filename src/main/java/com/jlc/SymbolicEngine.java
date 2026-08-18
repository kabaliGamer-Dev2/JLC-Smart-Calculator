package com.jlc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SymbolicEngine {

    public static class QuadraticSolution {
        public final double root1Real;
        public final double root1Imag;
        public final double root2Real;
        public final double root2Imag;
        public final boolean isComplex;

        public QuadraticSolution(double r1r, double r1i, double r2r, double r2i, boolean isComplex) {
            this.root1Real = r1r;
            this.root1Imag = r1i;
            this.root2Real = r2r;
            this.root2Imag = r2i;
            this.isComplex = isComplex;
        }

        @Override
        public String toString() {
            if (!isComplex) {
                return "x₁ = " + root1Real + ", x₂ = " + root2Real;
            } else {
                return "x₁ = " + root1Real + " + " + root1Imag + "i, x₂ = " + root2Real + " - " + Math.abs(root2Imag) + "i";
            }
        }
    }

    public static QuadraticSolution solveQuadratic(double a, double b, double c) {
        if (Math.abs(a) < 1e-12) {
            if (Math.abs(b) < 1e-12) throw new IllegalArgumentException("Degenerate linear equation");
            double r = -c / b;
            return new QuadraticSolution(r, 0, r, 0, false);
        }
        double disc = b * b - 4 * a * c;
        if (disc >= 0) {
            double r1 = (-b + Math.sqrt(disc)) / (2 * a);
            double r2 = (-b - Math.sqrt(disc)) / (2 * a);
            return new QuadraticSolution(r1, 0, r2, 0, false);
        } else {
            double real = -b / (2 * a);
            double imag = Math.sqrt(-disc) / (2 * a);
            return new QuadraticSolution(real, imag, real, -imag, true);
        }
    }

    public static double[] solve2x2System(double a1, double b1, double c1, double a2, double b2, double c2) {
        double det = a1 * b2 - a2 * b1;
        if (Math.abs(det) < 1e-12) {
            throw new ArithmeticException("System of equations has no unique solution (det = 0)");
        }
        double x = (c1 * b2 - c2 * b1) / det;
        double y = (a1 * c2 - a2 * c1) / det;
        return new double[] { x, y };
    }

    public static String differentiatePolynomial(String poly) {
        // e.g. "3x^3 + 5x^2 - 4x + 7" -> "9x^2 + 10x - 4"
        String clean = poly.replaceAll("\\s+", "").replace("-", "+-");
        String[] terms = clean.split("\\+");
        StringBuilder sb = new StringBuilder();

        for (String term : terms) {
            if (term.isEmpty()) continue;
            double coeff = 1;
            int power = 0;

            if (!term.contains("x")) {
                continue; // constant derivative is 0
            }

            if (term.contains("x^")) {
                String[] parts = term.split("x\\^");
                coeff = parts[0].isEmpty() || parts[0].equals("+") ? 1 : (parts[0].equals("-") ? -1 : Double.parseDouble(parts[0]));
                power = Integer.parseInt(parts[1]);
            } else if (term.contains("x")) {
                String cStr = term.replace("x", "");
                coeff = cStr.isEmpty() || cStr.equals("+") ? 1 : (cStr.equals("-") ? -1 : Double.parseDouble(cStr));
                power = 1;
            }

            double newCoeff = coeff * power;
            int newPower = power - 1;

            if (sb.length() > 0 && newCoeff > 0) sb.append(" + ");
            else if (newCoeff < 0 && sb.length() > 0) sb.append(" - ");
            else if (newCoeff < 0) sb.append("-");

            double absCoeff = Math.abs(newCoeff);
            if (newPower == 0) {
                sb.append(absCoeff == (long) absCoeff ? String.format("%.0f", absCoeff) : absCoeff);
            } else if (newPower == 1) {
                if (absCoeff != 1) sb.append(absCoeff == (long) absCoeff ? String.format("%.0f", absCoeff) : absCoeff);
                sb.append("x");
            } else {
                if (absCoeff != 1) sb.append(absCoeff == (long) absCoeff ? String.format("%.0f", absCoeff) : absCoeff);
                sb.append("x^").append(newPower);
            }
        }
        return sb.length() == 0 ? "0" : sb.toString();
    }
}
