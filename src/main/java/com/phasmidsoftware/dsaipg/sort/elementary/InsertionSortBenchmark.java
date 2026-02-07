package com.phasmidsoftware.dsaipg.sort.elementary;

import com.phasmidsoftware.dsaipg.util.benchmark.Benchmark_Timer;
import com.phasmidsoftware.dsaipg.util.config.Config;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class InsertionSortBenchmark {

    public static void main(String[] args) {
        Config config = loadConfigSafely();

        int[] sizes = {1000, 2000, 4000, 8000, 16000};

        System.out.println("n\tRandom\tOrdered\tPartial(80%)\tReverse");
        System.out.println("----------------------------------------------------------");

        for (int n : sizes) {
            // nRuns 参数建议用 100 或根据 config 动态获取，这里固定 100
            InsertionSortComparator<Integer> sorter = new InsertionSortComparator<>(Integer::compareTo, n, 100, config);

            Benchmark_Timer<Integer[]> benchmark = new Benchmark_Timer<>(
                    "InsertionSort n=" + n,
                    config,
                    null,  // pre
                    arr -> sorter.sort(arr, 0, arr.length),  // run
                    null   // post
            );

            // 重复次数根据场景调整，避免 reverse 太慢
            double tRandom   = benchmark.runFromSupplier(() -> randomArray(n), 20);
            double tOrdered  = benchmark.runFromSupplier(() -> orderedArray(n), 100);
            double tPartial  = benchmark.runFromSupplier(() -> partialArray(n, 0.8), 50);
            double tReverse  = benchmark.runFromSupplier(() -> reverseArray(n), 10);

            System.out.printf("%5d\t%8.2f\t%8.2f\t%10.2f\t%8.2f\n",
                    n, tRandom, tOrdered, tPartial, tReverse);
        }
    }

    /**
     * 安全加载 config.ini，失败时打印错误并退出（避免 new Config() 无效）
     */
    private static Config loadConfigSafely() {
        try {
            // 优先根据本类加载（找 resources 下的 config.ini）
            return Config.load(InsertionSortBenchmark.class);
            // 或者用 Benchmark_Timer.class 也行： Config.load(Benchmark_Timer.class);
        } catch (IOException e) {
            System.err.println("严重错误：无法加载 config.ini 文件");
            System.err.println("原因: " + e.getMessage());
            e.printStackTrace();
            System.err.println("请检查：");
            System.err.println("1. config.ini 是否在 src/main/resources/ 目录下");
            System.err.println("2. 项目是否正确配置 resources 路径");
            System.err.println("3. 是否在运行时 classpath 中包含了 config.ini");
            System.exit(1);  // 强制退出，让你先修复配置问题
            return null;     // 实际上不会到这里
        }
    }

    private static Integer[] randomArray(int n) {
        Integer[] a = new Integer[n];
        for (int i = 0; i < n; i++) a[i] = i;
        List<Integer> list = Arrays.asList(a);
        Collections.shuffle(list, new Random(42));  // 固定种子，可重复
        return a;
    }

    private static Integer[] orderedArray(int n) {
        Integer[] a = new Integer[n];
        for (int i = 0; i < n; i++) a[i] = i;
        return a;
    }

    private static Integer[] reverseArray(int n) {
        Integer[] a = new Integer[n];
        for (int i = 0; i < n; i++) a[i] = n - 1 - i;
        return a;
    }

    private static Integer[] partialArray(int n, double orderedRatio) {
        Integer[] a = orderedArray(n);
        int disturbCount = (int) (n * (1 - orderedRatio));
        Random r = new Random(42);
        for (int i = 0; i < disturbCount; i++) {
            int x = r.nextInt(n);
            int y = r.nextInt(n);
            Integer temp = a[x];
            a[x] = a[y];
            a[y] = temp;
        }
        return a;
    }
}