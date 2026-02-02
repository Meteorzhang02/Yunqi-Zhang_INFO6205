package com.phasmidsoftware.dsaipg.adt.threesum;

import java.util.HashSet;
import java.util.Set;

public class ThreeSumCubic implements ThreeSum {
    private final int[] numbers;

    public ThreeSumCubic(int[] numbers) {
        this.numbers = numbers;
    }

    @Override
    public Triple[] getTriples() {
        Set<Triple> result = new HashSet<>();
        int n = numbers.length;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                for (int k = j + 1; k < n; k++) {
                    if (numbers[i] + numbers[j] + numbers[k] == 0) {
                        result.add(new Triple(numbers[i], numbers[j], numbers[k]));
                    }
                }
            }
        }

        return result.toArray(new Triple[0]);
    }
}