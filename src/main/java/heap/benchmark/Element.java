package heap.benchmark;

import java.util.Random;

public class Element implements Comparable<Element> {
    private static int idCounter = 0;
    public final int id;
    public final int priority;

    public Element(int priority) {
        this.id = idCounter++;
        this.priority = priority;
    }

    @Override
    public int compareTo(Element other) {
        // 最小堆：值越小优先级越高
        return Integer.compare(this.priority, other.priority);
    }

    @Override
    public String toString() {
        return "E(id=" + id + ", p=" + priority + ")";
    }

    public static Element random(Random rand) {
        return new Element(rand.nextInt(10_000));
    }
}