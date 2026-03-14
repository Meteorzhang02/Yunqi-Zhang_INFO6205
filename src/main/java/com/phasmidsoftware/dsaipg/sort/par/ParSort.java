package com.phasmidsoftware.dsaipg.sort.par;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

/**
 * 并行归并排序实现
 * 主要使用 CompletableFuture + Fork/Join 风格的二分递归
 */
final class ParSort {

    /**
     * 当待排序区间长度 < cutoff 时，切换为顺序排序（Arrays.sort）
     * 这个值需要通过实验来调优
     */
    public static int cutoff = 8192;  // 建议从 4096～32768 之间实验

    /**
     * 原地排序入口方法（会修改传入的 array）
     */
    public static void sort(int[] array, int from, int to) {
        // 如果区间足够小，直接使用系统排序
        if (to - from <= cutoff) {
            Arrays.sort(array, from, to);
            return;
        }

        int mid = from + (to - from) / 2;

        // 左半部分异步排序
        CompletableFuture<int[]> leftFuture = asyncSort(array, from, mid);
        // 右半部分异步排序
        CompletableFuture<int[]> rightFuture = asyncSort(array, mid, to);

        // 等待两者完成，并合并结果，然后写回原数组
        CompletableFuture<int[]> combined = leftFuture.thenCombine(rightFuture, ParSort::doMerge);

        // 阻塞等待完成，并把结果拷贝回原数组的对应位置
        int[] result = combined.join();
        System.arraycopy(result, 0, array, from, result.length);
    }

    /**
     * 递归排序子数组，并返回全新的已排序数组（不修改原数组）
     */
    static int[] sortRecursive(int[] array, int from, int to) {
        int length = to - from;
        int[] result = new int[length];

        // 小区间直接用系统排序
        if (length <= cutoff) {
            System.arraycopy(array, from, result, 0, length);
            Arrays.sort(result);
            return result;
        }

        int mid = from + length / 2;

        CompletableFuture<int[]> left = asyncSort(array, from, mid);
        CompletableFuture<int[]> right = asyncSort(array, mid, to);

        return left.thenCombine(right, ParSort::doMerge).join();
    }

    /**
     * 合并两个已经有序的数组
     */
    static int[] doMerge(int[] xs1, int[] xs2) {
        int[] result = new int[xs1.length + xs2.length];
        int i = 0, j = 0, k = 0;

        while (i < xs1.length && j < xs2.length) {
            if (xs1[i] <= xs2[j]) {
                result[k++] = xs1[i++];
            } else {
                result[k++] = xs2[j++];
            }
        }

        while (i < xs1.length) result[k++] = xs1[i++];
        while (j < xs2.length) result[k++] = xs2[j++];

        return result;
    }

    /**
     * 异步执行 sortRecursive，返回一个 CompletableFuture
     */
    static CompletableFuture<int[]> asyncSort(int[] array, int from, int to) {
        return CompletableFuture.supplyAsync(() -> sortRecursive(array, from, to));
    }
}