package com.phasmidsoftware.dsaipg.sort.par;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

/**
 *
 */
public class Main {

    private static final int ARRAY_SIZE = 2_000_000;
    private static final int REPEATS = 7;           // 每组并行测几次取平均

    public static void main(String[] args) {
        System.out.println("可用并行度 (ForkJoinPool common parallelism): "
                + ForkJoinPool.getCommonPoolParallelism());

        List<String> results = new ArrayList<>();
        // CSV 表头（新增 sequential_time 和 speedup 列）
        results.add("cutoff_ratio,cutoff_value,avg_parallel_time_ms,sequential_time_ms,speedup\n");

        Random random = new Random();

        // ==================== 新增：先测一次顺序排序作为 baseline ====================
        System.out.println("The reference time for sequential sorting (Arrays.sort) is being measured...");
        int[] seqArray = random.ints(ARRAY_SIZE, 0, 10_000_000).toArray();
        long seqStart = System.nanoTime();
        Arrays.sort(seqArray);
        long seqEnd = System.nanoTime();
        double sequentialTimeMs = (seqEnd - seqStart) / 1_000_000.0;
        System.out.printf("Sequential Arrays.sort time: %.2f ms%n%n", sequentialTimeMs);
        // ========================================================================

        // 测试不同的 cutoff
        int[] cutoffCandidates = {512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072};

        for (int cut : cutoffCandidates) {
            ParSort.cutoff = cut;

            long totalTimeNs = 0;

            for (int rep = 0; rep < REPEATS; rep++) {
                int[] array = random.ints(ARRAY_SIZE, 0, 10_000_000).toArray();

                long start = System.nanoTime();
                ParSort.sort(array, 0, array.length);
                long end = System.nanoTime();

                totalTimeNs += (end - start);
            }

            double avgParallelMs = totalTimeNs / (double) REPEATS / 1_000_000.0;
            double ratio = (double) cut / ARRAY_SIZE;
            double speedup = sequentialTimeMs / avgParallelMs;

            String line = String.format("%.6f,%d,%.3f,%.3f,%.2f\n",
                    ratio, cut, avgParallelMs, sequentialTimeMs, speedup);
            results.add(line);

            System.out.printf("cutoff = %6d   ratio = %.5f   avg parallel = %8.2f ms   " +
                    "speedup = %.2fx%n", cut, ratio, avgParallelMs, speedup);
        }

        // 写入 CSV 文件
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream("parallel_sort_result.csv", false),
                        StandardCharsets.UTF_8))) {

            for (String line : results) {
                bw.write(line);
            }
            System.out.println("\nThe result has been written in parallel_sort_result.csv");
            System.out.println("（include sequential baseline and speedup）");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}