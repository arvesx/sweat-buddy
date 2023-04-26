package dependencies.structures;

import java.util.LinkedList;
import java.util.Queue;

public class FifoQueue<T> {
    private final Queue<T> queue;

    public FifoQueue() {
        queue = new LinkedList<>();
    }

    public void enqueue(T item) {
        queue.add(item);
    }

    public T dequeue() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int size() {
        return queue.size();
    }

}
