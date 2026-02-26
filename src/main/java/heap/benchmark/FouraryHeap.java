package heap.benchmark;

public class FouraryHeap<T extends Comparable<T>> implements PriorityQueue<T> {
    protected static final int DEFAULT_CAPACITY = 4095;
    protected static final int D = 4;
    protected T[] heap;
    protected int size;

    @SuppressWarnings("unchecked")
    public FouraryHeap(int initialCapacity) {
        heap = (T[]) new Comparable[initialCapacity + 1];
        size = 0;
    }

    public FouraryHeap() {
        this(DEFAULT_CAPACITY);
    }

    @Override
    public void add(T element) {
        if (element == null) throw new IllegalArgumentException();
        if (size >= heap.length - 1) {
            return;
        }
        size++;
        heap[size] = element;
        siftUp(size);
    }

    @Override
    public T removeMin() {
        if (isEmpty()) return null;
        T min = heap[1];
        heap[1] = heap[size];
        heap[size] = null;
        size--;
        siftDown(1);
        return min;
    }

    @Override
    public T peek() {
        return isEmpty() ? null : heap[1];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    protected void siftUp(int k) {
        T key = heap[k];
        while (k > 1) {
            int parent = (k - 2) / D + 1;
            if (key.compareTo(heap[parent]) >= 0) break;
            heap[k] = heap[parent];
            k = parent;
        }
        heap[k] = key;
    }

    protected void siftDown(int k) {
        T key = heap[k];
        while (D * (k - 1) + 2 <= size) {
            int firstChild = D * (k - 1) + 2;
            int minChild = firstChild;
            int lastChild = Math.min(firstChild + D - 1, size);
            for (int i = firstChild + 1; i <= lastChild; i++) {
                if (heap[i].compareTo(heap[minChild]) < 0) {
                    minChild = i;
                }
            }
            if (key.compareTo(heap[minChild]) <= 0) {
                break;
            }
            heap[k] = heap[minChild];
            k = minChild;
        }
        heap[k] = key;
    }
}