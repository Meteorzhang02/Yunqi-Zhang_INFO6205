package heap.benchmark;

import java.util.*;
import java.util.function.Supplier;

public class HeapBenchmark {
    private static final int M = 4095;          // 堆的最大容量
    private static final int INSERTS = 16000;    // 插入总数
    private static final int REMOVES = 4000;     // 移除总数
    private static final Random RAND = new Random(42);

    public static void main(String[] args) {
        System.out.println("Starting Heap Performance Benchmark...");
        System.out.printf("Max Heap Size (M): %d, Total Inserts: %d, Total Removes: %d%n%n", M, INSERTS, REMOVES);

        Map<String, Supplier<PriorityQueue<Element>>> heapFactories = new LinkedHashMap<>();
        heapFactories.put("Binary Heap", () -> new BinaryHeap<>(M));
        heapFactories.put("Binary Heap (Floyd)", () -> new FloydBinaryHeap<>(M));
        heapFactories.put("4-ary Heap", () -> new FouraryHeap<>(M));
        heapFactories.put("4-ary Heap (Floyd)", () -> new FloydFouraryHeap<>(M));
        heapFactories.put("Fibonacci Heap", () -> new FibonacciHeap<>(M));

        Map<String, Long> timeResults = new LinkedHashMap<>();
        Map<String, Element> spilledResults = new LinkedHashMap<>();

        for (Map.Entry<String, Supplier<PriorityQueue<Element>>> entry : heapFactories.entrySet()) {
            String name = entry.getKey();
            System.out.println("Testing: " + name);

            PriorityQueue<Element> heap = entry.getValue().get();
            BenchmarkResult result = runBenchmark(heap);

            timeResults.put(name, result.timeNs);
            spilledResults.put(name, result.topSpilled);

            System.out.printf("  Time: %d ns, Top Spilled: %s%n", result.timeNs, result.topSpilled);
        }

        System.out.println("\n--- Benchmark Results ---");
        timeResults.forEach((name, time) -> System.out.printf("%-25s: %12d ns%n", name, time));
        System.out.println("\nTop Spilled Elements:");
        spilledResults.forEach((name, elem) -> System.out.printf("%-25s: %s%n", name, elem));
    }

    private static BenchmarkResult runBenchmark(PriorityQueue<Element> heap) {
        RAND.setSeed(42); // 每个堆处理完全相同的操作序列

        long start = System.nanoTime();

        Element topSpilled = null;
        int removesDone = 0;

        for (int i = 0; i < INSERTS; i++) {
            Element e = Element.random(RAND);

            if (heap.size() >= M) {
                // 堆已满，处理可能的“挤出”
                if (heap.peek() != null && e.compareTo(heap.peek()) < 0) {
                    Element spilled = heap.removeMin(); // 移除当前优先级最低的
                    heap.add(e);                         // 插入更高优先级的
                    if (topSpilled == null || spilled.compareTo(topSpilled) < 0) {
                        topSpilled = spilled;
                    }
                }
                // 否则忽略该插入
            } else {
                heap.add(e);
            }

            // 穿插执行移除操作
            if (i > 0 && i % (INSERTS / REMOVES) == 0 && removesDone < REMOVES) {
                heap.removeMin();
                removesDone++;
            }
        }

        // 完成剩余的移除操作
        while (removesDone < REMOVES) {
            heap.removeMin();
            removesDone++;
        }

        long end = System.nanoTime();
        return new BenchmarkResult(end - start, topSpilled);
    }

    private static class BenchmarkResult {
        long timeNs;
        Element topSpilled;

        BenchmarkResult(long timeNs, Element topSpilled) {
            this.timeNs = timeNs;
            this.topSpilled = topSpilled;
        }
    }
}