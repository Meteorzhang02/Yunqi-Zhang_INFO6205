package heap.benchmark;

public interface PriorityQueue<T extends Comparable<T>> {
    void add(T element);
    T removeMin();
    T peek();
    int size();
    boolean isEmpty();
}