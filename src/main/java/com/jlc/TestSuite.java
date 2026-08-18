package com.jlc;

import java.util.ArrayList;
import java.util.List;

public class TestSuite {

    public static class TestResult {
        public final String category;
        public final String name;
        public final boolean passed;
        public final String details;

        public TestResult(String category, String name, boolean passed, String details) {
            this.category = category;
            this.name = name;
            this.passed = passed;
            this.details = details;
        }
    }

    public static List<TestResult> runAll() {
        List<TestResult> results = new ArrayList<>();

        // 1. Arithmetic & Precedence
        assertEq(results, "Arithmetic", "Basic Operations", MathEngine.evaluate("25 - 4 * 3"), 13.0);
        assertEq(results, "Arithmetic", "Nested Parentheses", MathEngine.evaluate("((15 * 20) - (50 / 10)) + (3^3 * 7) - sqrt(625) + (9! / 8!)"), 468.0);

        // 2. Scientific & Exponent
        assertEq(results, "Scientific", "Power & Roots", MathEngine.evaluate("2^10 + sqrt(81)"), 1033.0);
        assertEq(results, "Scientific", "Factorials", MathEngine.evaluate("5! + 4!"), 144.0);

        // 3. Discrete Math
        assertEq(results, "Discrete", "GCD", MathEngine.evaluate("gcd(123456, 789012)"), 12.0);
        assertEq(results, "Discrete", "LCM", MathEngine.evaluate("lcm(123456, 789012)"), 8117355456.0);
        assertEq(results, "Discrete", "Fibonacci", MathEngine.evaluate("fib(50)"), 12586269025.0);
        assertEq(results, "Discrete", "Prime Check", MathEngine.evaluate("isprime(982451653)"), 1.0);

        // 4. Physics & Astronomy Formulas
        FormulaEngine.StepExplanation esc = FormulaEngine.escapeVelocity(5.9722e24, 6.371e6);
        assertApprox(results, "Astrophysics", "Earth Escape Velocity", esc.result, 11.186, 0.01);

        FormulaEngine.StepExplanation schw = FormulaEngine.schwarzschildRadius(10.0 * 1.98847e30);
        assertApprox(results, "Astrophysics", "Schwarzschild Radius 10 Msun", schw.result, 29.53, 0.05);

        FormulaEngine.StepExplanation phot = FormulaEngine.photonEnergy(532e-9);
        assertApprox(results, "Quantum", "Photon Energy 532nm", phot.result, 3.733e-19, 1e-21);

        FormulaEngine.StepExplanation emc2 = FormulaEngine.massEnergyEquivalence(0.002);
        assertApprox(results, "Relativity", "E = mc2 for 2 grams", emc2.result, 1.7975e14, 1e11);

        // 5. Unit Conversions
        UnitConverter.ConversionResult len = UnitConverter.convert(5.0, "km", "m");
        assertEq(results, "Units", "5 km to meters", len.toValue, 5000.0);

        UnitConverter.ConversionResult temp = UnitConverter.convert(100.0, "C", "F");
        assertEq(results, "Units", "100 C to Fahrenheit", temp.toValue, 212.0);

        // 6. Matrix Operations
        MatrixEngine.Matrix m1 = MatrixEngine.Matrix.parse("[[1, 2], [3, 4]]");
        assertEq(results, "Matrix", "2x2 Determinant", m1.determinant(), -2.0);

        MatrixEngine.Matrix m2 = MatrixEngine.Matrix.parse("[[2, 0], [1, 2]]");
        MatrixEngine.Matrix prod = m1.multiply(m2);
        assertEq(results, "Matrix", "Matrix Multiplication", prod.data[0][0], 4.0);

        // 7. Symbolic Solver
        SymbolicEngine.QuadraticSolution quad = SymbolicEngine.solveQuadratic(1, -5, 6);
        assertEq(results, "Symbolic", "Quadratic Roots x^2 - 5x + 6", quad.root1Real, 3.0);
        assertEq(results, "Symbolic", "Quadratic Roots x^2 - 5x + 6 (r2)", quad.root2Real, 2.0);

        // 8. Base Conversions
        assertStrEq(results, "BaseConverter", "Decimal to Hex", BaseConverter.toHex("4294967295"), "FFFFFFFF");
        assertStrEq(results, "BaseConverter", "Hex to Decimal", BaseConverter.fromHexToDecimal("FFFFFFFF"), "4294967295");
        assertStrEq(results, "BaseConverter", "Roman Numeral 2026", BaseConverter.toRoman(2026), "MMXXVI");

        // 9. Complex Numbers
        ComplexNumber c1 = ComplexNumber.parse("3 + 4i");
        assertEq(results, "Complex", "Complex Magnitude |3+4i|", c1.magnitude(), 5.0);

        // 10. Financial Engine
        FormulaEngine.StepExplanation ci = FormulaEngine.compoundInterest(10000, 8.0, 4, 5);
        assertApprox(results, "Finance", "Compound Interest 10k @ 8% 5yr", ci.result, 14859.47, 0.1);

        return results;
    }

    private static void assertEq(List<TestResult> list, String cat, String name, double actual, double expected) {
        boolean pass = Math.abs(actual - expected) < 1e-6;
        list.add(new TestResult(cat, name, pass, "Expected: " + expected + ", Actual: " + actual));
    }

    private static void assertApprox(List<TestResult> list, String cat, String name, double actual, double expected, double tol) {
        boolean pass = Math.abs(actual - expected) <= tol;
        list.add(new TestResult(cat, name, pass, "Expected: ~" + expected + ", Actual: " + actual));
    }

    private static void assertStrEq(List<TestResult> list, String cat, String name, String actual, String expected) {
        boolean pass = actual.equals(expected);
        list.add(new TestResult(cat, name, pass, "Expected: \"" + expected + "\", Actual: \"" + actual + "\""));
    }

    public static void main(String[] args) {
        List<TestResult> results = runAll();
        int passed = 0;
        System.out.println("==================== JLC TEST SUITE ====================");
        for (TestResult r : results) {
            if (r.passed) {
                passed++;
                System.out.printf("  ✅ [%-12s] %-35s %s\n", r.category, r.name, r.details);
            } else {
                System.out.printf("  ❌ [%-12s] %-35s %s\n", r.category, r.name, r.details);
            }
        }
        System.out.println("---------------------------------------------------------");
        System.out.printf("  Summary: %d / %d Passed (%.1f%%)\n", passed, results.size(), (double) passed / results.size() * 100.0);
        System.out.println("=========================================================");
    }
}
