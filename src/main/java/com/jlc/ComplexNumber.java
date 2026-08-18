package com.jlc;

public class ComplexNumber {
    public final double real;
    public final double imag;

    public ComplexNumber(double real, double imag) {
        this.real = real;
        this.imag = imag;
    }

    public static ComplexNumber parse(String str) {
        // e.g. "3 + 4i", "5-2i", "-4i", "6"
        String clean = str.replaceAll("\\s+", "").replace("j", "i");
        if (clean.equals("i") || clean.equals("+i")) return new ComplexNumber(0, 1);
        if (clean.equals("-i")) return new ComplexNumber(0, -1);

        if (!clean.contains("i")) {
            return new ComplexNumber(Double.parseDouble(clean), 0);
        }

        if (clean.indexOf('i') == clean.length() - 1 && !clean.substring(0, clean.length() - 1).contains("+") && !clean.substring(1, clean.length() - 1).contains("-")) {
            String coeff = clean.substring(0, clean.length() - 1);
            return new ComplexNumber(0, coeff.isEmpty() || coeff.equals("+") ? 1 : (coeff.equals("-") ? -1 : Double.parseDouble(coeff)));
        }

        int lastSign = Math.max(clean.lastIndexOf('+'), clean.lastIndexOf('-'));
        if (lastSign <= 0) {
            return new ComplexNumber(0, Double.parseDouble(clean.replace("i", "")));
        }

        double r = Double.parseDouble(clean.substring(0, lastSign));
        String iPart = clean.substring(lastSign, clean.length() - 1);
        double im = iPart.equals("+") ? 1 : (iPart.equals("-") ? -1 : Double.parseDouble(iPart));
        return new ComplexNumber(r, im);
    }

    public ComplexNumber add(ComplexNumber o) {
        return new ComplexNumber(this.real + o.real, this.imag + o.imag);
    }

    public ComplexNumber subtract(ComplexNumber o) {
        return new ComplexNumber(this.real - o.real, this.imag - o.imag);
    }

    public ComplexNumber multiply(ComplexNumber o) {
        return new ComplexNumber(this.real * o.real - this.imag * o.imag, this.real * o.imag + this.imag * o.real);
    }

    public ComplexNumber divide(ComplexNumber o) {
        double denom = o.real * o.real + o.imag * o.imag;
        if (denom == 0) throw new ArithmeticException("Division by zero in complex division");
        return new ComplexNumber((this.real * o.real + this.imag * o.imag) / denom, (this.imag * o.real - this.real * o.imag) / denom);
    }

    public double magnitude() {
        return Math.hypot(real, imag);
    }

    public double phaseRadians() {
        return Math.atan2(imag, real);
    }

    public double phaseDegrees() {
        return Math.toDegrees(phaseRadians());
    }

    public String toPolarString() {
        return String.format("%.4f ∠ %.2f° (%.4f * e^(i * %.4f rad))", magnitude(), phaseDegrees(), magnitude(), phaseRadians());
    }

    @Override
    public String toString() {
        if (imag == 0) return String.format("%.4f", real);
        if (real == 0) return String.format("%.4fi", imag);
        if (imag < 0) return String.format("%.4f - %.4fi", real, Math.abs(imag));
        return String.format("%.4f + %.4fi", real, imag);
    }
}
