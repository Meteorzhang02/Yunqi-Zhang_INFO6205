package com.phasmidsoftware.dsaipg.adt.threesum;

import java.util.*;

public class ThreeSumQuadrithmic implements ThreeSum {
    private final int[] numbers;

    public ThreeSumQuadrithmic(int[] numbers) {
        this.numbers = numbers;
        Arrays.sort(this.numbers);
    }

    @Override
    public Triple[] getTriples() {
        Set<Triple> result = new HashSet<>();
        int n = numbers.length;

        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                int target = -(numbers[i] + numbers[j]);
                int index = Arrays.binarySearch(numbers, j + 1, n, target);
                if (index >= 0) {
                    result.add(new Triple(numbers[i], numbers[j], numbers[index]));
                }
            }
        }

        return result.toArray(new Triple[0]);
    }

    // 获取特定的三元组（用于测试）
    public Triple getTriple(int i, int j) {
        if (i >= numbers.length || j >= numbers.length || i >= j) {
            return null;
        }

        int target = -(numbers[i] + numbers[j]);
        int index = Arrays.binarySearch(numbers, j + 1, numbers.length, target);

        if (index >= 0) {
            return new Triple(numbers[i], numbers[j], numbers[index]);
        }

        return null;
    }
}