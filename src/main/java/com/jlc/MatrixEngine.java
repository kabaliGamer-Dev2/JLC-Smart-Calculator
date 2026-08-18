package com.jlc;

import java.util.Arrays;

public class MatrixEngine {

    public static class Matrix {
        public final int rows;
        public final int cols;
        public final double[][] data;

        public Matrix(int rows, int cols) {
            this.rows = rows;
            this.cols = cols;
            this.data = new double[rows][cols];
        }

        public Matrix(double[][] data) {
            this.rows = data.length;
            this.cols = data[0].length;
            this.data = new double[rows][cols];
            for (int i = 0; i < rows; i++) {
                System.arraycopy(data[i], 0, this.data[i], 0, cols);
            }
        }

        public static Matrix parse(String str) {
            String trimmed = str.trim();
            if (trimmed.startsWith("[[") && trimmed.endsWith("]]")) {
                String inner = trimmed.substring(2, trimmed.length() - 2);
                String[] rowTokens = inner.split("\\]\\s*,\\s*\\[");
                double[][] grid = new double[rowTokens.length][];
                for (int r = 0; r < rowTokens.length; r++) {
                    String[] nums = rowTokens[r].trim().split("[,\\s]+");
                    grid[r] = new double[nums.length];
                    for (int c = 0; c < nums.length; c++) {
                        grid[r][c] = Double.parseDouble(nums[c]);
                    }
                }
                return new Matrix(grid);
            }
            if (trimmed.contains(";")) {
                String clean = trimmed.replaceAll("[\\[\\]]", "").trim();
                String[] rowTokens = clean.split(";");
                double[][] grid = new double[rowTokens.length][];
                for (int r = 0; r < rowTokens.length; r++) {
                    String[] nums = rowTokens[r].trim().split("[,\\s]+");
                    grid[r] = new double[nums.length];
                    for (int c = 0; c < nums.length; c++) {
                        grid[r][c] = Double.parseDouble(nums[c]);
                    }
                }
                return new Matrix(grid);
            }
            String clean = trimmed.replaceAll("[\\[\\]]", " ").trim();
            String[] rowTokens = clean.split("\n");
            double[][] grid = new double[rowTokens.length][];
            for (int r = 0; r < rowTokens.length; r++) {
                String[] nums = rowTokens[r].trim().split("[,\\s]+");
                grid[r] = new double[nums.length];
                for (int c = 0; c < nums.length; c++) {
                    grid[r][c] = Double.parseDouble(nums[c]);
                }
            }
            return new Matrix(grid);
        }

        public Matrix add(Matrix other) {
            if (this.rows != other.rows || this.cols != other.cols) {
                throw new IllegalArgumentException("Matrix dimensions must match for addition");
            }
            Matrix res = new Matrix(rows, cols);
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    res.data[i][j] = this.data[i][j] + other.data[i][j];
                }
            }
            return res;
        }

        public Matrix subtract(Matrix other) {
            if (this.rows != other.rows || this.cols != other.cols) {
                throw new IllegalArgumentException("Matrix dimensions must match for subtraction");
            }
            Matrix res = new Matrix(rows, cols);
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    res.data[i][j] = this.data[i][j] - other.data[i][j];
                }
            }
            return res;
        }

        public Matrix multiply(Matrix other) {
            if (this.cols != other.rows) {
                throw new IllegalArgumentException("Cannot multiply " + rows + "x" + cols + " by " + other.rows + "x" + other.cols);
            }
            Matrix res = new Matrix(this.rows, other.cols);
            for (int i = 0; i < this.rows; i++) {
                for (int j = 0; j < other.cols; j++) {
                    double sum = 0;
                    for (int k = 0; k < this.cols; k++) {
                        sum += this.data[i][k] * other.data[k][j];
                    }
                    res.data[i][j] = sum;
                }
            }
            return res;
        }

        public Matrix transpose() {
            Matrix res = new Matrix(cols, rows);
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    res.data[j][i] = this.data[i][j];
                }
            }
            return res;
        }

        public double determinant() {
            if (rows != cols) {
                throw new IllegalArgumentException("Determinant only exists for square matrices");
            }
            return det(this.data);
        }

        private static double det(double[][] m) {
            int n = m.length;
            if (n == 1) return m[0][0];
            if (n == 2) return m[0][0] * m[1][1] - m[0][1] * m[1][0];
            double d = 0;
            for (int j = 0; j < n; j++) {
                d += (j % 2 == 0 ? 1 : -1) * m[0][j] * det(minor(m, 0, j));
            }
            return d;
        }

        private static double[][] minor(double[][] m, int r, int c) {
            int n = m.length;
            double[][] sub = new double[n - 1][n - 1];
            int sr = 0;
            for (int i = 0; i < n; i++) {
                if (i == r) continue;
                int sc = 0;
                for (int j = 0; j < n; j++) {
                    if (j == c) continue;
                    sub[sr][sc++] = m[i][j];
                }
                sr++;
            }
            return sub;
        }

        public Matrix inverse() {
            double detVal = determinant();
            if (Math.abs(detVal) < 1e-12) {
                throw new ArithmeticException("Matrix is singular (determinant = 0), inverse does not exist");
            }
            int n = rows;
            Matrix inv = new Matrix(n, n);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    double sign = ((i + j) % 2 == 0) ? 1.0 : -1.0;
                    inv.data[j][i] = (sign * det(minor(this.data, i, j))) / detVal;
                }
            }
            return inv;
        }

        public double[] eigenvalues2x2() {
            if (rows != 2 || cols != 2) {
                throw new IllegalArgumentException("Analytical eigenvalues implemented for 2x2 matrices");
            }
            double a = data[0][0], b = data[0][1], c = data[1][0], d = data[1][1];
            double trace = a + d;
            double det = a * d - b * c;
            double disc = trace * trace - 4 * det;
            if (disc < 0) {
                return new double[] { trace / 2.0, Math.sqrt(-disc) / 2.0 }; // real, imag
            }
            double lambda1 = (trace + Math.sqrt(disc)) / 2.0;
            double lambda2 = (trace - Math.sqrt(disc)) / 2.0;
            return new double[] { lambda1, lambda2 };
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("[\n");
            for (int i = 0; i < rows; i++) {
                sb.append("  [");
                for (int j = 0; j < cols; j++) {
                    sb.append(String.format("%8.4f", data[i][j]));
                    if (j < cols - 1) sb.append(", ");
                }
                sb.append("]\n");
            }
            sb.append("]");
            return sb.toString();
        }
    }

    public static double vectorDot(double[] a, double[] b) {
        if (a.length != b.length) throw new IllegalArgumentException("Vector dimensions must match");
        double sum = 0;
        for (int i = 0; i < a.length; i++) sum += a[i] * b[i];
        return sum;
    }

    public static double[] vectorCross3D(double[] a, double[] b) {
        if (a.length != 3 || b.length != 3) throw new IllegalArgumentException("3D vectors required for cross product");
        return new double[] {
            a[1] * b[2] - a[2] * b[1],
            a[2] * b[0] - a[0] * b[2],
            a[0] * b[1] - a[1] * b[0]
        };
    }
}
