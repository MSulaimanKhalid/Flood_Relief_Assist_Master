package org.example.zereaab.DataStructures;

public class CircularQueue<T> {

    private T[] queue;
    private int front = 0;
    private int rear = 0;
    private int size = 0;
    private int capacity;

    public CircularQueue(int capacity) {
        this.capacity = capacity;
        queue = (T[]) new Object[capacity];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return size == capacity;
    }

    public void enqueue(T value) {

        if (isFull()) {
            // overwrite oldest → move front forward
            front = (front + 1) % capacity;
            size--;
        }

        queue[rear] = value;
        rear = (rear + 1) % capacity;
        size++;
    }

    public T dequeue() {
        if (isEmpty()) {
            return null;
        }

        T value = queue[front];
        front = (front + 1) % capacity;
        size--;
        return value;
    }

    //test
    @Override
    public String toString() {

        if (size == 0) return "[]";

        StringBuilder sb = new StringBuilder("[");
        int index = front;

        for (int i = 0; i < size; i++) {
            sb.append(queue[index]);
            if (i < size - 1) sb.append(", ");
            index = (index + 1) % capacity;
        }

        sb.append("]");
        return sb.toString();
    }


}
