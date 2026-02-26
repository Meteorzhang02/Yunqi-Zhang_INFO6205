package heap.benchmark;

public class FloydBinaryHeap<T extends Comparable<T>> extends BinaryHeap<T> {

    public FloydBinaryHeap(int initialCapacity) {
        super(initialCapacity);
    }

    public FloydBinaryHeap() {
        super();
    }

    // Floyd's trick: O(n)建堆
    protected void buildHeap() {
        for (int i = size / 2; i >= 1; i--) {
            siftDown(i);
        }
    }

    // 从数组构建堆（用于测试Floyd建堆）
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