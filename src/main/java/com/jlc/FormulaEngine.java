package com.jlc;

import java.util.ArrayList;
import java.util.List;

public class FormulaEngine {

    public static class StepExplanation {
        public final String formulaName;
        public final String equation;
        public final List<String> steps = new ArrayList<>();
        public final double result;
        public final String unit;

        public StepExplanation(String formulaName, String equation, double result, String unit) {
            this.formulaName = formulaName;
            this.equation = equation;
            this.result = result;
            this.unit = unit;
        }

        public void addStep(String desc, String expr, double val) {
            steps.add(desc + ": " + expr + " = " + ResponseFormatter.formatNumber(val));
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(UI.ORANGE_BOLD).append("📐 Formula: ").append(formulaName).append("\n").append(UI.RESET);
            sb.append(UI.ASH).append("   Equation: ").append(UI.WHITE_BOLD).append(equation).append("\n").append(UI.RESET);
            sb.append(UI.ORANGE_BOLD).append("📝 Step-by-Step Resolution:\n").append(UI.RESET);
            for (int i = 0; i < steps.size(); i++) {
                sb.append(UI.ASH).append("   Step ").append(i + 1).append(": ").append(UI.WHITE_BOLD).append(steps.get(i)).append("\n").append(UI.RESET);
            }
            sb.append(UI.ORANGE_BOLD).append("🎯 Final Result: ").append(UI.GOLD_BOLD).append(ResponseFormatter.formatNumber(result)).append(" ").append(unit).append("\n").append(UI.RESET);
            return sb.toString();
        }
    }

    // --- Astronomy & Astrophysics ---
    public static StepExplanation escapeVelocity(double massKg, double radiusM) {
        double G = 6.67430e-11;
        double inside = (2.0 * G * massKg) / radiusM;
        double vMps = Math.sqrt(inside);
        double vKmps = vMps / 1000.0;

        StepExplanation exp = new StepExplanation("Earth / Celestial Escape Velocity", "v_e = sqrt(2 * G * M / R)", vKmps, "km/s");
        exp.addStep("Multiply 2 * G * M", "2 * 6.67430e-11 * " + massKg, 2.0 * G * massKg);
        exp.addStep("Divide by Radius R", (2.0 * G * massKg) + " / " + radiusM, inside);
        exp.addStep("Take Square Root (m/s)", "sqrt(" + inside + ")", vMps);
        exp.addStep("Convert to km/s", vMps + " / 1000", vKmps);
        return exp;
    }

    public static StepExplanation schwarzschildRadius(double massKg) {
        double G = 6.67430e-11;
        double c = 299792458.0;
        double rsMeters = (2.0 * G * massKg) / (c * c);
        double rsKm = rsMeters / 1000.0;

        StepExplanation exp = new StepExplanation("Schwarzschild Black Hole Radius", "r_s = (2 * G * M) / c²", rsKm, "km");
        exp.addStep("Numerator (2 * G * M)", "2 * 6.67430e-11 * " + massKg, 2.0 * G * massKg);
        exp.addStep("Denominator (c²)", "299792458²", c * c);
        exp.addStep("Radius in meters", (2.0 * G * massKg) + " / " + (c * c), rsMeters);
        exp.addStep("Radius in km", rsMeters + " / 1000", rsKm);
        return exp;
    }

    public static StepExplanation photonEnergy(double wavelengthM) {
        double h = 6.62607015e-34;
        double c = 299792458.0;
        double energyJ = (h * c) / wavelengthM;
        double energyEV = energyJ / 1.602176634e-19;

        StepExplanation exp = new StepExplanation("Photon Energy", "E = (h * c) / λ", energyJ, "Joules (" + ResponseFormatter.formatNumber(energyEV) + " eV)");
        exp.addStep("Compute h * c", "6.62607015e-34 * 299792458", h * c);
        exp.addStep("Divide by wavelength λ", (h * c) + " / " + wavelengthM, energyJ);
        return exp;
    }

    public static StepExplanation deBroglieWavelength(double massKg, double velocityMps) {
        double h = 6.62607015e-34;
        double lambda = h / (massKg * velocityMps);

        StepExplanation exp = new StepExplanation("De Broglie Matter Wavelength", "λ = h / (m * v)", lambda, "meters");
        exp.addStep("Momentum p = m * v", massKg + " * " + velocityMps, massKg * velocityMps);
        exp.addStep("Divide h / p", "6.62607015e-34 / " + (massKg * velocityMps), lambda);
        return exp;
    }

    public static StepExplanation massEnergyEquivalence(double massKg) {
        double c = 299792458.0;
        double energyJ = massKg * (c * c);

        StepExplanation exp = new StepExplanation("Mass-Energy Equivalence", "E = m * c²", energyJ, "Joules");
        exp.addStep("Calculate c²", "299792458²", c * c);
        exp.addStep("Multiply mass by c²", massKg + " * " + (c * c), energyJ);
        return exp;
    }

    // --- Thermodynamics & Chemistry ---
    public static StepExplanation idealGasPressure(double moles, double tempK, double volumeM3) {
        double R = 8.314462618;
        double p = (moles * R * tempK) / volumeM3;

        StepExplanation exp = new StepExplanation("Ideal Gas Law (Pressure)", "P = (n * R * T) / V", p, "Pascals");
        exp.addStep("Compute n * R * T", moles + " * 8.314462618 * " + tempK, moles * R * tempK);
        exp.addStep("Divide by Volume V", (moles * R * tempK) + " / " + volumeM3, p);
        return exp;
    }

    public static StepExplanation phFromHydrogen(double hConcentration) {
        double ph = -Math.log10(hConcentration);
        StepExplanation exp = new StepExplanation("pH Calculation", "pH = -log10([H+])", ph, "");
        exp.addStep("Compute log10([H+])", "log10(" + hConcentration + ")", Math.log10(hConcentration));
        exp.addStep("Negate result", "-(" + Math.log10(hConcentration) + ")", ph);
        return exp;
    }

    // --- Finance & Investment ---
    public static StepExplanation compoundInterest(double principal, double annualRatePct, int compoundsPerYear, double years) {
        double r = annualRatePct / 100.0;
        double base = 1.0 + (r / compoundsPerYear);
        double exponent = compoundsPerYear * years;
        double amount = principal * Math.pow(base, exponent);
        double interest = amount - principal;

        StepExplanation exp = new StepExplanation("Compound Interest", "A = P * (1 + r/n)^(n*t)", amount, "(Interest: " + ResponseFormatter.formatNumber(interest) + ")");
        exp.addStep("Rate per period (r/n)", r + " / " + compoundsPerYear, r / compoundsPerYear);
        exp.addStep("Growth factor (1 + r/n)", "1 + " + (r / compoundsPerYear), base);
        exp.addStep("Total compounding periods (n*t)", compoundsPerYear + " * " + years, exponent);
        exp.addStep("Compounded amount", principal + " * " + base + "^" + exponent, amount);
        return exp;
    }

    public static StepExplanation loanEmi(double principal, double annualRatePct, int months) {
        double monthlyRate = (annualRatePct / 100.0) / 12.0;
        double factor = Math.pow(1.0 + monthlyRate, months);
        double emi = (principal * monthlyRate * factor) / (factor - 1.0);
        double totalPayment = emi * months;
        double totalInterest = totalPayment - principal;

        StepExplanation exp = new StepExplanation("Loan Equated Monthly Installment (EMI)", "EMI = [P * r * (1+r)^n] / [(1+r)^n - 1]", emi, "per month (Total Interest: " + ResponseFormatter.formatNumber(totalInterest) + ")");
        exp.addStep("Monthly interest rate (r)", annualRatePct + "% / 12", monthlyRate);
        exp.addStep("Compounding factor (1+r)^n", "(1 + " + monthlyRate + ")^" + months, factor);
        exp.addStep("Numerator [P * r * factor]", principal + " * " + monthlyRate + " * " + factor, principal * monthlyRate * factor);
        exp.addStep("Denominator [factor - 1]", factor + " - 1", factor - 1.0);
        exp.addStep("Monthly EMI", (principal * monthlyRate * factor) + " / " + (factor - 1.0), emi);
        return exp;
    }

    public static StepExplanation sipCalculator(double monthlyDeposit, double annualRatePct, int months) {
        double i = (annualRatePct / 100.0) / 12.0;
        double factor = Math.pow(1.0 + i, months);
        double futureVal = monthlyDeposit * ((factor - 1.0) / i) * (1.0 + i);
        double totalInvested = monthlyDeposit * months;
        double wealthGained = futureVal - totalInvested;

        StepExplanation exp = new StepExplanation("SIP (Systematic Investment Plan) Future Value", "FV = P * [((1+i)^n - 1) / i] * (1+i)", futureVal, "(Wealth Gain: " + ResponseFormatter.formatNumber(wealthGained) + ")");
        exp.addStep("Monthly interest rate i", annualRatePct + "% / 12", i);
        exp.addStep("Growth factor (1+i)^n", "(1 + " + i + ")^" + months, factor);
        exp.addStep("Future Value", monthlyDeposit + " * [(" + factor + " - 1) / " + i + "] * (1 + " + i + ")", futureVal);
        return exp;
    }
}
