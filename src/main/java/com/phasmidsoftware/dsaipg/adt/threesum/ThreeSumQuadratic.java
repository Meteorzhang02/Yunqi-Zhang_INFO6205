package com.phasmidsoftware.dsaipg.adt.threesum;

import java.util.*;

public class ThreeSumQuadratic implements ThreeSum {
    private final int[] originalNumbers;  // 保存原始数组
    private final int[] sortedNumbers;    // 保存排序后的数组

    public ThreeSumQuadratic(int[] numbers) {
        this.originalNumbers = numbers.clone();
        this.sortedNumbers = numbers.clone();
        Arrays.sort(this.sortedNumbers);
    }

    @Override
    public Triple[] getTriples() {
        List<Triple> result = new ArrayList<>();
        int n = sortedNumbers.length;

        for (int i = 0; i < n - 2; i++) {
            // 跳过重复元素
            if (i > 0 && sortedNumbers[i] == sortedNumbers[i - 1]) continue;

            int left = i + 1;
            int right = n - 1;

            while (left < right) {
                int sum = sortedNumbers[i] + sortedNumbers[left] + sortedNumbers[right];

                if (sum == 0) {
                    result.add(new Triple(sortedNumbers[i], sortedNumbers[left], sortedNumbers[right]));

                    // 跳过重复元素
                    while (left < right && sortedNumbers[left] == sortedNumbers[left + 1]) left++;
                    while (left < right && sortedNumbers[right] == sortedNumbers[right - 1]) right--;

                    left++;
                    right--;
                } else if (sum < 0) {
                    left++;
                } else {
                    right--;
                }
            }
        }

        // 转换为数组前手动去重
        Set<Triple> uniqueTriples = new LinkedHashSet<>(result);
        return uniqueTriples.toArray(new Triple[0]);
    }

    // 重载方法，返回包含特定索引元素的三元组
    public List<Triple> getTriples(int j) {
        if (j < 0 || j >= originalNumbers.length) {
            return new ArrayList<>();
        }

        // 获取原始索引j对应的值
        int targetValue = originalNumbers[j];

        // 找到所有等于targetValue的位置（在排序后的数组中）
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < sortedNumbers.length; i++) {
            if (sortedNumbers[i] == targetValue) {
                positions.add(i);
            }
        }

        Set<Triple> result = new HashSet<>();

        // 对每个位置，找到包含该位置元素的三元组
        for (int fixedPos : positions) {
            int fixedValue = sortedNumbers[fixedPos];

            // 使用双指针法寻找另外两个数
            int left = 0;
            int right = sortedNumbers.length - 1;

            while (left < right) {
                // 跳过fixedPos位置
                if (left == fixedPos) {
                    left++;
                    continue;
                }
                if (right == fixedPos) {
                    right--;
                    continue;
                }

                int sum = fixedValue + sortedNumbers[left] + sortedNumbers[right];

                if (sum == 0) {
                    // 创建三元组，确保顺序一致以便比较
                    int[] tripleValues = {fixedValue, sortedNumbers[left], sortedNumbers[right]};
                    Arrays.sort(tripleValues); // 排序以便正确创建Triple对象
                    result.add(new Triple(tripleValues[0], tripleValues[1], tripleValues[2]));
                    left++;
                    right--;
                } else if (sum < 0) {
                    left++;
                } else {
                    right--;
                }
            }
        }

        return new ArrayList<>(result);
    }
}