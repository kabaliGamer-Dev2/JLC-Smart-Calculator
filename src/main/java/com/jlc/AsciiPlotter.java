package com.jlc;

import java.util.Arrays;
import java.util.Map;

public class AsciiPlotter {

    public static String plot(String expression, double xMin, double xMax, int width, int height) {
        if (width <= 10) width = 60;
        if (height <= 5) height = 20;

        double[] xVals = new double[width];
        double[] yVals = new double[width];
        double dx = (xMax - xMin) / (width - 1);

        double yMin = Double.MAX_VALUE;
        double yMax = -Double.MAX_VALUE;

        for (int i = 0; i < width; i++) {
            double x = xMin + i * dx;
            xVals[i] = x;
            try {
                double y = MathEngine.evaluate(expression, Map.of("x", x));
                if (Double.isNaN(y) || Double.isInfinite(y)) {
                    yVals[i] = Double.NaN;
                } else {
                    yVals[i] = y;
                    yMin = Math.min(yMin, y);
                    yMax = Math.max(yMax, y);
                }
            } catch (Exception e) {
                yVals[i] = Double.NaN;
            }
        }

        if (yMin >= yMax) {
            yMin -= 1.0;
            yMax += 1.0;
        }

        char[][] canvas = new char[height][width];
        for (int r = 0; r < height; r++) {
            Arrays.fill(canvas[r], ' ');
        }

        // Draw axes if in view
        int zeroRow = -1;
        if (yMin <= 0 && yMax >= 0) {
            zeroRow = (int) Math.round((yMax - 0.0) / (yMax - yMin) * (height - 1));
            if (zeroRow >= 0 && zeroRow < height) {
                for (int c = 0; c < width; c++) canvas[zeroRow][c] = '─';
            }
        }

        int zeroCol = -1;
        if (xMin <= 0 && xMax >= 0) {
            zeroCol = (int) Math.round((0.0 - xMin) / (xMax - xMin) * (width - 1));
            if (zeroCol >= 0 && zeroCol < width) {
                for (int r = 0; r < height; r++) {
                    canvas[r][zeroCol] = (canvas[r][zeroCol] == '─') ? '┼' : '│';
                }
            }
        }

        // Plot function points
        for (int c = 0; c < width; c++) {
            if (Double.isNaN(yVals[c])) continue;
            int r = (int) Math.round((yMax - yVals[c]) / (yMax - yMin) * (height - 1));
            if (r >= 0 && r < height) {
                canvas[r][c] = '•';
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append(UI.ORANGE_BOLD).append("Plot of y = ").append(expression)
          .append(UI.ASH).append("  [x ∈ [").append(xMin).append(", ").append(xMax).append("], y ∈ [")
          .append(String.format("%.2f", yMin)).append(", ").append(String.format("%.2f", yMax)).append("]]\n")
          .append(UI.RESET);

        for (int r = 0; r < height; r++) {
            double curY = yMax - (double) r / (height - 1) * (yMax - yMin);
            sb.append(UI.ASH).append(String.format("%8.2f │ ", curY)).append(UI.ORANGE);
            for (int c = 0; c < width; c++) {
                char ch = canvas[r][c];
                if (ch == '•') {
                    sb.append(UI.ORANGE_BOLD).append('•').append(UI.ORANGE);
                } else if (ch == '┼' || ch == '─' || ch == '│') {
                    sb.append(UI.ASH_DARK).append(ch).append(UI.ORANGE);
                } else {
                    sb.append(' ');
                }
            }
            sb.append("\n");
        }
        sb.append(UI.ASH).append("         └").append("─".repeat(width)).append("\n");
        sb.append(UI.ASH).append(String.format("%10.2f", xMin)).append(" ".repeat(Math.max(1, width - 18)))
          .append(String.format("%10.2f", xMax)).append(UI.RESET).append("\n");

        return sb.toString();
    }
}
