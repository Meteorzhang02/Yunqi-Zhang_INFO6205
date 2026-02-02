package com.phasmidsoftware.dsaipg.adt.threesum;

import java.util.Arrays;

public class Triple {
    private final int x;
    private final int y;
    private final int z;
    private final int[] sortedValues;

    public Triple(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.sortedValues = new int[]{x, y, z};
        Arrays.sort(this.sortedValues);
    }

    public int sum() {
        return x + y + z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triple triple = (Triple) o;
        return Arrays.equals(sortedValues, triple.sortedValues);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(sortedValues);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getZ() { return z; }
}