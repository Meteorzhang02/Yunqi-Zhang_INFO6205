package com.phasmidsoftware.dsaipg.adt.threesum;

import java.util.Arrays;

public class DebugUtils {

    public static void printArrayInfo(int[] arr) {
        System.out.println("Array length: " + arr.length);
        System.out.println("Array: " + Arrays.toString(arr));
        System.out.println("Sorted: " + Arrays.toString(Arrays.copyOf(arr, arr.length)));
    }

    public static void compareAlgorithms(int[] arr) {
        ThreeSum cubic = new ThreeSumCubic(arr);
        ThreeSum quadratic = new ThreeSumQuadratic(arr);

        Triple[] cubicTriples = cubic.getTriples();
        Triple[] quadraticTriples = quadratic.getTriples();

        System.out.println("Cubic found: " + cubicTriples.length);
        System.out.println("Quadratic found: " + quadraticTriples.length);

        if (cubicTriples.length != quadraticTriples.length) {
            System.out.println("MISMATCH DETECTED!");
            System.out.println("Cubic triples: " + Arrays.toString(cubicTriples));
            System.out.println("Quadratic triples: " + Arrays.toString(quadraticTriples));
        }
    }
}