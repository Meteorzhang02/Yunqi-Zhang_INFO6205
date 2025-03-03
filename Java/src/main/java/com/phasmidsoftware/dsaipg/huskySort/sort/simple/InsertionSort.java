/*
  (c) Copyright 2018, 2019 Phasmid Software
 */
package com.phasmidsoftware.dsaipg.huskySort.sort.simple;

import com.phasmidsoftware.dsaipg.huskySort.sort.ComparableSortHelper;
import com.phasmidsoftware.dsaipg.huskySort.sort.ComparisonSortHelper;
import com.phasmidsoftware.dsaipg.huskySort.sort.SortWithHelper;
import com.phasmidsoftware.dsaipg.huskySort.util.Config;

/**
 * Class to implement Insertion Sort.
 * NOTE: this implementation does NOT use the insertion swap mechanism and is not therefore used in the SortBenchmark,
 * This class is the version of insertion sort appropriate for running instrumented benchmarks.
 * It extends SortWithHelper and therefore has access to the Helper functions.
 * <p>
 * For a simpler and clearer version, see InsertionSortBasic.
 * </p>
 * <p>
 *     NOTE: there is an alternative implementation of InsertionSort in the info6205/classic package.
 * There are considerable differences so, for now, we'll leave both versions as is.
 * </p>
 *
 * @param <X> the underlying type which must extend Comparable.
 */
public class InsertionSort<X extends Comparable<X>> extends SortWithHelper<X> {

    /**
     * Sort the subarray xs:from:to using insertion sort.
     *
     * @param xs   sort the array xs from "from" to "to".
     * @param from the index of the first element to sort.
     * @param to   the index of the first element not to sort.
     */
    public void sort(final X[] xs, final int from, final int to) {
        final ComparisonSortHelper<X> helper = getHelper();
        for (int i = from + 1; i < to; i++) {
            // CONSIDER implement using swapIntoSorted
            int j = i;
            while (j > from && helper.swapStableConditional(xs, j)) j--;
        }
    }

    public static <Y extends Comparable<Y>> void mutatingInsertionSort(final Y[] ys) {
        new InsertionSort<Y>().mutatingSort(ys);
    }

    public static final String DESCRIPTION = "Insertion sort";

    /**
     * Constructor for InsertionSort
     *
     * @param N      the number elements we expect to sort.
     * @param config the configuration.
     */
    public InsertionSort(final int N, final Config config) {
        super(DESCRIPTION, N, config);
    }

    public InsertionSort() {
        this(new ComparableSortHelper<>(DESCRIPTION));
    }

    /**
     * Constructor for InsertionSort
     *
     * @param helper an explicit instance of ComparisonSortHelper to be used.
     */
    public InsertionSort(final ComparisonSortHelper<X> helper) {
        super(helper);
    }

}