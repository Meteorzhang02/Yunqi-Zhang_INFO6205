/*
  (c) Copyright 2018, 2019 Phasmid Software
 */
package com.phasmidsoftware.dsaipg.huskySort.sort.simple;

import com.phasmidsoftware.dsaipg.huskySort.sort.ComparableSortHelper;
import com.phasmidsoftware.dsaipg.huskySort.sort.ComparisonSortHelper;
import com.phasmidsoftware.dsaipg.huskySort.sort.SortWithHelper;
import com.phasmidsoftware.dsaipg.huskySort.util.Config;

import java.util.Arrays;

/**
 * Sorter which delegates to Timsort via Arrays.sort.
 *
 * @param <X>
 */
public class TimSort<X extends Comparable<X>> extends SortWithHelper<X> {

    /**
     * @param xs   sort the array xs from "from" until "to" (exclusive of to).
     * @param from the index of the first element to sort.
     * @param to   the index of the first element not to sort.
     */
    public void sort(final X[] xs, final int from, final int to) {
        Arrays.sort(xs, from, to);
    }

    public static final String DESCRIPTION = "Timsort";

    /**
     * Constructor for TimSort
     *
     * @param helper an explicit instance of ComparisonSortHelper to be used.
     */
    public TimSort(final ComparisonSortHelper<X> helper) {
        super(helper);
    }

    /**
     * Constructor for TimSort
     *
     * @param N      the number elements we expect to sort.
     * @param config the configuration.
     */
    public TimSort(final int N, final Config config) {
        super(DESCRIPTION, N, config);
    }

    public TimSort() {
        this(new ComparableSortHelper<>(DESCRIPTION));
    }
}
