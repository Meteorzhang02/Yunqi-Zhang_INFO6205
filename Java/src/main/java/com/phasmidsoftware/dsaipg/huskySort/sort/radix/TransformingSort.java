package com.phasmidsoftware.dsaipg.huskySort.sort.radix;

import com.phasmidsoftware.dsaipg.huskySort.sort.GenericSort;
import com.phasmidsoftware.dsaipg.huskySort.sort.Sorter;

/**
 * Interface to define a sort method which sorts an array of Xs by temporarily transforming them into an array of Ys.
 *
 * @param <X> the element type of the input/output arrays (must implement Comparable of X).
 * @param <T> the element type of the temporary array.
 */
public interface TransformingSort<X extends Comparable<X>, T> extends GenericSort<T>, Sorter<X> {

    /**
     * Method to get a suitable Helper for this TransformingSort.
     *
     * @return the Helper.
     */
    TransformingHelper<X, T> getHelper();

}