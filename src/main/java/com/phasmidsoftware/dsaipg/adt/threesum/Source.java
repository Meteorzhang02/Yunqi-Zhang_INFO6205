package com.phasmidsoftware.dsaipg.adt.threesum;

import java.util.Random;
import java.util.function.Supplier;

public class Source {
    private final int size;
    private final int bound;
    private final long seed;

    public Source(int size, int bound) {
        this(size, bound, System.currentTimeMillis());
    }

    public Source(int size, int bound, long seed) {
        this.size = size;
        this.bound = bound;
        this.seed = seed;
    }

    public Supplier<int[]> intsSupplier(int scale) {
        return () -> {
            Random random = new Random(seed);
            int[] result = new int[size];
            for (int i = 0; i < size; i++) {
                result[i] = random.nextInt(bound * 2) - bound;
            }
            return result;
        };
    }
}