package com.phasmidsoftware.dsaipg.adt.threesum;

import java.util.Arrays;
import java.util.Random;

public class PerformanceAnalysis {

    public static class Stopwatch {
        private final long start;

        public Stopwatch() {
            start = System.currentTimeMillis();
        }

        public double elapsedTime() {
            long now = System.currentTimeMillis();
            return (now - start) / 1000.0;
        }
    }

    public static void main(String[] args) {
        System.out.println("3-SUM 算法性能分析 - 倍率法测试");
        System.out.println("==================================================");
        System.out.printf("%-10s %-15s %-15s %-15s%n",
                "N", "Cubic(s)", "Quadratic(s)", "Quadrithmic(s)");
        System.out.println("==================================================");

        // 使用倍率法：每次N加倍
        int baseN = 100;
        double prevCubic = 0, prevQuadratic = 0, prevQuadrithmic = 0;

        for (int i = 0; i < 6; i++) { // 6个数据点
            int n = baseN * (int)Math.pow(2, i);
            int[] data = generateRandomArray(n, n * 10);

            // 测试Cubic算法
            Stopwatch sw = new Stopwatch();
            ThreeSum cubic = new ThreeSumCubic(data);
            cubic.getTriples();
            double cubicTime = sw.elapsedTime();

            // 测试Quadratic算法
            sw = new Stopwatch();
            ThreeSum quadratic = new ThreeSumQuadratic(data);
            quadratic.getTriples();
            double quadraticTime = sw.elapsedTime();

            // 测试Quadrithmic算法
            sw = new Stopwatch();
            ThreeSum quadrithmic = new ThreeSumQuadrithmic(data);
            quadrithmic.getTriples();
            double quadrithmicTime = sw.elapsedTime();

            // 计算倍率
            String cubicRatio = (i > 0) ? String.format("(x%.2f)", cubicTime/prevCubic) : "";
            String quadraticRatio = (i > 0) ? String.format("(x%.2f)", quadraticTime/prevQuadratic) : "";
            String quadrithmicRatio = (i > 0) ? String.format("(x%.2f)", quadrithmicTime/prevQuadrithmic) : "";

            System.out.printf("%-10d %-15.3f%-10s %-15.5f%-10s %-15.5f%-10s%n",
                    n, cubicTime, cubicRatio, quadraticTime, quadraticRatio,
                    quadrithmicTime, quadrithmicRatio);

            prevCubic = cubicTime;
            prevQuadratic = quadraticTime;
            prevQuadrithmic = quadrithmicTime;
        }
    }

    private static int[] generateRandomArray(int size, int bound) {
        Random random = new Random(42); // 固定种子以确保可重复性
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(2 * bound + 1) - bound;
        }
        return array;
    }
}