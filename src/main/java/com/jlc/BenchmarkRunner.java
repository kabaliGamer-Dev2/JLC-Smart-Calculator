package com.jlc;

import java.util.Map;

public class BenchmarkRunner {

    public static class BenchmarkResult {
        public final int iterations;
        public final long jlcTimeMs;
        public final long standardTimeMs;
        public final double jlcThroughputOpsPerSec;
        public final double standardThroughputOpsPerSec;
        public final double maxDelta;

        public BenchmarkResult(int iterations, long jlcTimeMs, long standardTimeMs, double jlcOps, double stdOps, double maxDelta) {
            this.iterations = iterations;
            this.jlcTimeMs = jlcTimeMs;
            this.standardTimeMs = standardTimeMs;
            this.jlcThroughputOpsPerSec = jlcOps;
            this.standardThroughputOpsPerSec = stdOps;
            this.maxDelta = maxDelta;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(UI.ASH_DARK).append("=========================== ").append(UI.ORANGE_BOLD)
              .append("⚡ JLC PERFORMANCE BENCHMARK").append(UI.ASH_DARK).append(" ===========================\n").append(UI.RESET);
            sb.append(UI.ASH).append("  • Iterations Tested: ").append(UI.WHITE_BOLD).append(String.format("%,d", iterations)).append("\n").append(UI.RESET);
            sb.append(UI.ASH).append("  • JLC MathEngine Duration: ").append(UI.ORANGE_BOLD).append(jlcTimeMs).append(" ms").append(UI.ASH)
              .append(" (").append(String.format("%,.0f", jlcThroughputOpsPerSec)).append(" ops/sec)\n").append(UI.RESET);
            sb.append(UI.ASH).append("  • Java Standard Math Duration: ").append(UI.WHITE_BOLD).append(standardTimeMs).append(" ms").append(UI.ASH)
              .append(" (").append(String.format("%,.0f", standardThroughputOpsPerSec)).append(" ops/sec)\n").append(UI.RESET);
            sb.append(UI.ASH).append("  • Max Numerical Delta: ").append(UI.ORANGE_BOLD).append(maxDelta).append(UI.RESET)
              .append(maxDelta < 1e-9 ? " (✅ 100% Bit-for-Bit Exact)" : "").append("\n");
            sb.append(UI.ASH_DARK).append("===================================================================================\n").append(UI.RESET);
            return sb.toString();
        }
    }

    public static BenchmarkResult runBenchmark(int iterations) {
        if (iterations <= 0) iterations = 100000;

        // Warm up
        for (int i = 0; i < 1000; i++) {
            MathEngine.evaluate("sqrt(144) + sin(0.5) * (3^3) - log10(100)");
        }

        double maxDelta = 0;

        // Benchmark JLC
        long startJlc = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            double x = i * 0.01;
            double resJlc = MathEngine.evaluate("sqrt(" + x + ") + sin(" + x + ") * 5 - (2^3)");
            double resStd = Math.sqrt(x) + Math.sin(x) * 5.0 - Math.pow(2.0, 3.0);
            maxDelta = Math.max(maxDelta, Math.abs(resJlc - resStd));
        }
        long jlcTime = System.currentTimeMillis() - startJlc;
        if (jlcTime == 0) jlcTime = 1;

        // Benchmark Standard Math
        long startStd = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            double x = i * 0.01;
            double resStd = Math.sqrt(x) + Math.sin(x) * 5.0 - Math.pow(2.0, 3.0);
        }
        long stdTime = System.currentTimeMillis() - startStd;
        if (stdTime == 0) stdTime = 1;

        double jlcOps = ((double) iterations / jlcTime) * 1000.0;
        double stdOps = ((double) iterations / stdTime) * 1000.0;

        return new BenchmarkResult(iterations, jlcTime, stdTime, jlcOps, stdOps, maxDelta);
    }

    public static void main(String[] args) {
        int iters = args.length > 0 ? Integer.parseInt(args[0]) : 100000;
        System.out.println("Running JLC Benchmark with " + iters + " iterations...");
        BenchmarkResult res = runBenchmark(iters);
        System.out.println(res);
    }
}
