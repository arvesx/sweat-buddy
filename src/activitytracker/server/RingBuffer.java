package activitytracker.server;

import java.util.NoSuchElementException;

public class RingBuffer<T> {

    private Node<T> head;
    private Node<T> tail;
    private Node<T> temp;

    public RingBuffer() {

    }

    public void add(T obj) {

        Node<T> node = new Node<>(obj);

        if (head == null && tail == null) {
            head = node;
            tail = node;
            temp = node;
            return;
        }
        tail.setNext(node);
        node.setPrev(tail);
        tail = node;
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
        return dataToReturn;
    }

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

    class Node<T> {

        private Node<T> next;
        private Node<T> prev;
        private T data;

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
