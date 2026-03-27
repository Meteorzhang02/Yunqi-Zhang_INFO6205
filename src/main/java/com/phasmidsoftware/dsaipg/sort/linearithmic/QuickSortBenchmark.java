package com.phasmidsoftware.dsaipg.sort.linearithmic;

import com.phasmidsoftware.dsaipg.sort.helper.Helper;
import com.phasmidsoftware.dsaipg.sort.helper.HelperFactory;
import com.phasmidsoftware.dsaipg.util.config.Config;

import static com.phasmidsoftware.dsaipg.util.config.Config_Benchmark.setupConfig;

/**
 * 单独对 QuickSort_DualPivot 做 Integer 数组的 benchmark。
 * inversions=0 避免触发未实现的 InsertionSortComparator。
 */
public class QuickSortBenchmark {

    private static final int[] SIZES = {10000, 20000, 40000, 80000, 160000, 256000};

    public static void main(String[] args) throws Exception {
        System.out.println("n, compares, swaps, hits, copies, time_ms");

        for (int n : SIZES) {
            // ---- 1. 统计数据（instrumented=true, inversions=0, cutoff=10） ----
            // setupConfig(instrumenting, fixes, seed, inversions, cutoff, interimInversions)
            final Config cfgInstr = setupConfig("true", "false", "0", "0", "10", "");
            Helper<Integer> h = HelperFactory.create("QuickSort dual pivot", n, cfgInstr);
            h.init(n);

            // 多跑几次取平均，统计值会累积后由 Helper 内部平均
            int statRuns = 5;
            long sumCompares = 0, sumSwaps = 0, sumHits = 0;
            for (int r = 0; r < statRuns; r++) {
                // 每次重新 init 清零统计
                h.init(n);
                Integer[] xs = h.random(Integer.class, rnd -> rnd.nextInt(1_000_000));
                QuickSort_DualPivot<Integer> sorter = new QuickSort_DualPivot<>(h);
                // 用 sort(xs, true) 保留原数组，makeCopy=true
                Integer[] sorted = sorter.sort(xs, true);
                sumCompares += h.getCompares();
                sumSwaps    += h.getSwaps();
                sumHits     += h.getHits();
            }
            long compares = sumCompares / statRuns;
            long swaps    = sumSwaps    / statRuns;
            long hits     = sumHits     / statRuns;

            // ---- 2. 纯计时（instrumented=false） ----
            final Config cfgTime = setupConfig("false", "false", "0", "0", "10", "");
            Helper<Integer> hT = HelperFactory.create("QuickSort dual pivot", n, cfgTime);
            hT.init(n);

            // 预热
            for (int w = 0; w < 3; w++) {
                Integer[] warm = hT.random(Integer.class, rnd -> rnd.nextInt(1_000_000));
                new QuickSort_DualPivot<>(hT).sort(warm, true);
            }

            // 计时
            int runs = Math.max(5, (int)(1_000_000_000L / ((long) n * 20)));
            long total = 0;
            for (int r = 0; r < runs; r++) {
                Integer[] data = hT.random(Integer.class, rnd -> rnd.nextInt(1_000_000));
                long t0 = System.nanoTime();
                new QuickSort_DualPivot<>(hT).sort(data, true);
                total += System.nanoTime() - t0;
            }
            double timeMs = total / 1_000_000.0 / runs;

            System.out.printf("%d, %d, %d, %d, %s, %.4f%n",
                    n, compares, swaps, hits, "N/A", timeMs);

            h.close();
            hT.close();
        }
    }
}