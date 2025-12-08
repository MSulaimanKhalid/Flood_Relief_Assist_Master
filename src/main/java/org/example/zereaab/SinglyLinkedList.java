package org.example.zereaab;

public class SinglyLinkedList <T>{

    private Node head = null;
    private Node tail = null;
    private int size = 0;

    void addLast(T data){
        Node newNode = new Node(data);
        if (head == null){
            head = newNode;
            tail = newNode;
            size++;
            return;
        }
        tail.next = newNode;
        tail = newNode;
        size++;
    }

    T removeFirst(){
        if (head == null){
            return null;
        }
        T value = head.data;
        if (head == tail){
            head = tail = null;
            size--;
            return value;
        }
        head = head.next;
        size--;
        return value;
    }

    public int getSize() {
        return size;
    }

    private class Node{
        T data;
        Node next;
        Node(T data){
            this.data = data;
            next = null;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }
    }
}
