package heap.benchmark;

public class FloydFouraryHeap<T extends Comparable<T>> extends FouraryHeap<T> {

    public FloydFouraryHeap(int initialCapacity) {
        super(initialCapacity);
    }

    public FloydFouraryHeap() {
        super();
    }

    protected void buildHeap() {
        for (int i = size / D; i >= 1; i--) {
            siftDown(i);
        }
    }

    public void buildFromArray(T[] items) {
        if (items.length > heap.length - 1) {
            throw new IllegalArgumentException("Array too large");
        }
        for (int i = 0; i < items.length; i++) {
            heap[i + 1] = items[i];
        }
        size = items.length;
        buildHeap();
    }
}