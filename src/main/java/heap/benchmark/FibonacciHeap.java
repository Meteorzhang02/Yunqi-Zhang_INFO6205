package heap.benchmark;

import java.util.*;

public class FibonacciHeap<T extends Comparable<T>> implements PriorityQueue<T> {
    private Node<T> minNode;
    private int size;
    private final int capacity;

    private static class Node<T> {
        T element;
        Node<T> parent;
        Node<T> left;
        Node<T> right;
        Node<T> child;
        int degree;
        boolean marked;

        Node(T element) {
            this.element = element;
            left = this;
            right = this;
        }
    }

    public FibonacciHeap(int capacity) {
        this.capacity = capacity;
    }

    public FibonacciHeap() {
        this(4095);
    }

    @Override
    public void add(T element) {
        if (size >= capacity) {
            return; // 堆满，忽略插入
        }
        Node<T> node = new Node<>(element);
        if (minNode == null) {
            minNode = node;
        } else {
            // 将node插入根链表
            node.left = minNode;
            node.right = minNode.right;
            minNode.right = node;
            node.right.left = node;
            if (node.element.compareTo(minNode.element) < 0) {
                minNode = node;
            }
        }
        size++;
    }

    @Override
    public T removeMin() {
        Node<T> z = minNode;
        if (z == null) return null;

        // 将z的孩子加入根链表
        if (z.child != null) {
            Node<T> x = z.child;
            do {
                Node<T> next = x.right;
                // 将x加入根链表
                x.left = minNode;
                x.right = minNode.right;
                minNode.right = x;
                x.right.left = x;
                x.parent = null;
                x = next;
            } while (x != z.child);
        }

        // 从根链表中移除z
        z.left.right = z.right;
        z.right.left = z.left;

        if (z == z.right) {
            minNode = null;
        } else {
            minNode = z.right;
            consolidate();
        }
        size--;
        return z.element;
    }

    private void consolidate() {
        int maxDegree = (int) Math.floor(Math.log(size) / Math.log(2)) + 1;
        List<Node<T>> degreeTable = new ArrayList<>(Collections.nCopies(maxDegree, null));

        List<Node<T>> rootList = new ArrayList<>();
        Node<T> start = minNode;
        Node<T> x = start;
        do {
            rootList.add(x);
            x = x.right;
        } while (x != start);

        for (Node<T> w : rootList) {
            x = w;
            int d = x.degree;
            while (d < degreeTable.size() && degreeTable.get(d) != null) {
                Node<T> y = degreeTable.get(d);
                if (x.element.compareTo(y.element) > 0) {
                    Node<T> temp = x;
                    x = y;
                    y = temp;
                }
                // 使y成为x的孩子
                if (y == x) {
                    degreeTable.set(d, null);
                    continue;
                }
                // 将y从根链表中移除
                y.left.right = y.right;
                y.right.left = y.left;
                y.parent = x;
                y.marked = false;
                // 将y加入x的孩子链表
                if (x.child == null) {
                    x.child = y;
                    y.left = y;
                    y.right = y;
                } else {
                    y.left = x.child;
                    y.right = x.child.right;
                    x.child.right = y;
                    y.right.left = y;
                }
                x.degree++;
                degreeTable.set(d, null);
                d++;
            }
            if (d >= degreeTable.size()) {
                for (int i = degreeTable.size(); i <= d; i++) {
                    degreeTable.add(null);
                }
            }
            degreeTable.set(d, x);
        }

        minNode = null;
        for (Node<T> node : degreeTable) {
            if (node != null) {
                if (minNode == null || node.element.compareTo(minNode.element) < 0) {
                    minNode = node;
                }
            }
        }
    }

    @Override
    public T peek() {
        return minNode == null ? null : minNode.element;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }
}