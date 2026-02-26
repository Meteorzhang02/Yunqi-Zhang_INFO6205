package heap.benchmark;

public class BinaryHeap<T extends Comparable<T>> implements PriorityQueue<T> {
    protected static final int DEFAULT_CAPACITY = 4095;
    protected T[] heap;
    protected int size;

    @SuppressWarnings("unchecked")
    public BinaryHeap(int initialCapacity) {
        heap = (T[]) new Comparable[initialCapacity + 1]; // 索引0不用
        size = 0;
    }

    public BinaryHeap() {
        this(DEFAULT_CAPACITY);
    }

    @Override
    public void add(T element) {
        if (element == null) throw new IllegalArgumentException();
        if (size >= heap.length - 1) {
            // 堆已满，忽略插入（由调用者处理spilled元素）
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
        while (k > 1 && key.compareTo(heap[k / 2]) < 0) {
            heap[k] = heap[k / 2];
            k = k / 2;
        }
        heap[k] = key;
    }

    protected void siftDown(int k) {
        T key = heap[k];
        while (2 * k <= size) {
            int child = 2 * k;
            if (child < size && heap[child].compareTo(heap[child + 1]) > 0) {
                child++;
            }
            if (key.compareTo(heap[child]) <= 0) {
                break;
            }
            heap[k] = heap[child];
            k = child;
        }
        heap[k] = key;
    }
}