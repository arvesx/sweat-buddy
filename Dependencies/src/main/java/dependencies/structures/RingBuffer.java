package dependencies.structures;

import java.io.PrintStream;
import java.util.NoSuchElementException;

public class RingBuffer<T> {

    private Node<T> head;
    private Node<T> tail;
    private Node<T> temp;

    private int size;

    public RingBuffer() {
        this.size = 0;
    }

    public void add(T obj) {

        Node<T> node = new Node<>(obj);

        if (head == null && tail == null) {
            head = node;
            tail = node;
            temp = node;
            this.size++;
            return;
        }
        tail.setNext(node);
        node.setPrev(tail);
        tail = node;
        this.size++;
    }

    public T pop() {
        if (head == null) {
            throw new NoSuchElementException("List is empty");
        }


        T dataToReturn = tail.getData();
        if (temp == tail) {
            temp = head;
        }
        tail = tail.getPrev();
        tail.setNext(null);
        this.size--;
        return dataToReturn;
    }

    public void remove(T obj) {
        if (head == null) {
            throw new NoSuchElementException("List is empty");
        }

        if (head.getData() == obj) {

            if (temp == head)
                temp = temp.getNext();

            head = head.getNext();
            if (head != null)
                head.setPrev(null);

            this.size--;
            return;
        }

        if (tail.getData() == obj) {
            if (temp == tail)
                temp = head;

            tail = tail.getPrev();
            tail.setNext(null);

            this.size--;
            return;
        }



        Node<T> n = head;
        while (n != null) {
            if (n.getData() == obj) {
                Node<T> prevNode = n.getPrev();
                Node<T> nextNode = n.getNext();
                if (temp == n)
                    temp = nextNode;

                prevNode.setNext(nextNode);
                nextNode.setPrev(prevNode);
            }
            n = n.getNext();
        }
        this.size--;


    }

    /**
     * Get the next object in a round-robin fashion.
     */
    public T next() {
        if (head == null) {
            throw new NoSuchElementException("List is empty");
        }

        T dataToReturn = temp.getData();
        if (temp == tail) {
            temp = head;
        } else {
            temp = temp.getNext();
        }

        return dataToReturn;

    }

    public boolean isEmpty() {
        return (head == null && tail == null);
    }

    public int size()
    {
        return this.size;
    }

    public void printBuffer(PrintStream stream) {
        if (head == null) {
            System.out.println("Ring Buffer is empty");
        }

        Node<T> t = head;
        while (t != null) {
            stream.println(t.getData());
            t = t.getNext();
        }
    }

    static class Node<T> {

        private Node<T> next;
        private Node<T> prev;
        private final T data;

        public Node(T data) {
            this.data = data;
        }

        public void setNext(Node<T> next) {
            this.next = next;
        }

        public void setPrev(Node<T> prev) {
            this.prev = prev;
        }

        public Node<T> getNext() {
            return next;
        }

        public Node<T> getPrev() {
            return prev;
        }

        public T getData() {
            return data;
        }
    }
}
