package com.phasmidsoftware.dsaipg.sort.linearithmic;

import com.phasmidsoftware.dsaipg.sort.elementary.InsertionSort;
import com.phasmidsoftware.dsaipg.sort.generic.SortException;
import com.phasmidsoftware.dsaipg.sort.generic.SortWithComparableHelper;
import com.phasmidsoftware.dsaipg.sort.helper.Helper;
import com.phasmidsoftware.dsaipg.util.config.Config;

import java.util.Arrays;

import static com.phasmidsoftware.dsaipg.util.config.Config_Benchmark.*;

/**
 * A generic implementation of the MergeSort algorithm.
 *
 * @param <X> The type of elements to be sorted, which must implement Comparable.
 */
public class MergeSort<X extends Comparable<X>> extends SortWithComparableHelper<X> {

    public static final String DESCRIPTION = "MergeSort";

    public MergeSort(Helper<X> helper) {
        super(helper);
        insertionSort = setupInsertionSort(helper);
    }

    public MergeSort(int N, int nRuns, Config config) {
        super(DESCRIPTION + getConfigString(config), N, nRuns, config);
        insertionSort = setupInsertionSort(getHelper());
    }

    public X[] sort(X[] xs, boolean makeCopy) {
        getHelper().init(xs.length);
        additionalMemory(xs.length);
        X[] result = makeCopy ? Arrays.copyOf(xs, xs.length) : xs;
        sort(result, 0, result.length);
        additionalMemory(-xs.length);
        return result;
    }

    public void sort(X[] a, int from, int to) {
        Config config = helper.getConfig();
        boolean noCopy = config.getBoolean(MERGESORT, NOCOPY);
        @SuppressWarnings("unchecked")
        X[] aux = noCopy ? helper.copyArray(a) : (X[]) new Comparable[a.length];
        sort(a, aux, from, to);
    }

    public void setArrayMemory(int n) {
        if (arrayMemory == -1) {
            arrayMemory = n;
            additionalMemory(n);
        }
    }

    public void additionalMemory(int n) {
        additionalMemory += n;
        if (maxMemory < additionalMemory) maxMemory = additionalMemory;
    }

    public Double getMemoryFactor() {
        if (arrayMemory == -1)
            throw new SortException("Array memory has not been set");
        return 1.0 * maxMemory / arrayMemory;
    }

    private InsertionSort<X> setupInsertionSort(final Helper<X> helper) {
        return new InsertionSort<>(helper.clone("MergeSort: insertion sort"));
    }

    /**
     * 核心递归排序方法，支持 no-copy 和 insurance 两种优化。
     *
     * <p><b>no-copy 模式：</b>primary 与 secondary 在 [from,to) 范围内内容相同。
     * 递归调用时交换 primary/secondary 角色，从而省去每次 merge 后的回写复制。</p>
     *
     * <p><b>insurance 模式：</b>merge 前先检查 primary[mid-1] <= primary[mid]，
     * 若已有序则直接跳过 merge（节省比较次数）。</p>
     */
    private void sort(X[] primary, X[] secondary, int from, int to) {
        Config config = helper.getConfig();
        boolean noCopy   = config.getBoolean(MERGESORT, NOCOPY);
        boolean insurance = config.getBoolean(MERGESORT, INSURANCE);

        assert !noCopy || Arrays.compare(primary, from, to, secondary, from, to) == 0
                : "MergeSort::sort: partitions are not the same";

        // 小数组交给 InsertionSort（cutoff 机制）
        if (to <= from + helper.cutoff()) {
            insertionSort.sort(primary, from, to);
            // noCopy 模式下需要把结果同步回 secondary，保证不变量
            if (noCopy)
                helper.copyBlock(primary, from, secondary, from, to - from);
            return;
        }

        int mid = from + (to - from) / 2;

        if (noCopy) {
            // 交换角色递归：让子区间结果落在 secondary 中，
            // 这样 merge 时从 secondary 读、写入 primary，无需额外复制。
            sort(secondary, primary, from, mid);
            sort(secondary, primary, mid,  to);

            // insurance：若 secondary[mid-1] <= secondary[mid]，已有序，直接复制即可
            if (insurance && helper.compare(secondary, mid - 1, mid) <= 0) {
                helper.copyBlock(secondary, from, primary, from, to - from);
                return;
            }
            // 从 secondary 合并写入 primary
            merge(secondary, primary, from, mid, to);
        } else {
            // 普通模式：递归排 primary 的左右子区间
            sort(primary, secondary, from, mid);
            sort(primary, secondary, mid,  to);

            // insurance：已有序则跳过 merge
            if (insurance && helper.compare(primary, mid - 1, mid) <= 0)
                return;

            // 把 primary 复制到 secondary，再从 secondary 合并写回 primary
            helper.copyBlock(primary, from, secondary, from, to - from);
            merge(secondary, primary, from, mid, to);
        }
    }

    /**
     * 将 sorted[from..mid) 与 sorted[mid..to) 归并后写入 result[from..to)。
     */
    private void merge(X[] sorted, X[] result, int from, int mid, int to) {
        int i = from;
        int j = mid;
        X v = helper.get(sorted, i);
        X w = helper.get(sorted, j);
        for (int k = from; k < to; k++) {
            if (i >= mid) {
                helper.copy(w, result, k);
                if (++j < to) w = helper.get(sorted, j);
            } else if (j >= to) {
                helper.copy(v, result, k);
                if (++i < mid) v = helper.get(sorted, i);
            } else if (helper.notInverted(w, v)) {
                // w <= v，取右边元素（修复了一个逆序对）
                helper.incrementFixes(mid - i);
                helper.copy(w, result, k);
                if (++j < to) w = helper.get(sorted, j);
            } else {
                helper.copy(v, result, k);
                if (++i < mid) v = helper.get(sorted, i);
            }
        }
    }

    public static final String MERGESORT  = "mergesort";
    public static final String NOCOPY     = "nocopy";
    public static final String INSURANCE  = "insurance";

    private static String getConfigString(Config config) {
        StringBuilder sb = new StringBuilder();
        if (config.getBoolean(MERGESORT, INSURANCE)) sb.append(" with insurance comparison");
        if (config.getBoolean(MERGESORT, NOCOPY))    sb.append(" with no copy");
        int cutoff = config.getInt(HELPER, CUTOFF, CUTOFF_DEFAULT);
        if (cutoff != CUTOFF_DEFAULT) {
            if (cutoff == 1) sb.append(" with no cutoff");
            else             sb.append(" with cutoff ").append(cutoff);
        }
        return sb.toString();
    }

    private final InsertionSort<X> insertionSort;
    private int arrayMemory    = -1;
    private int additionalMemory;
    private int maxMemory;
}