package org.example.zereaab;

import java.awt.geom.Point2D;

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

    Node getHead(){
        if (head == null){
            return null;
        }
        return head;
    }

    public Point2D.Double poleInList() {
        if (head.data instanceof Point2D.Double) {
            Point2D.Double pole = null;
            Node poleNode = null;
            Node temp = getHead();
            Point2D.Double curr;

            while (temp != null){
                curr = (Point2D.Double)temp.data;
                if (pole == null){
                    pole = curr;
                    poleNode = temp;
                    temp = temp.next;
                    continue;
                }
                if (curr.getY() < pole.getY()){
                    pole = curr;
                    poleNode = temp;
                } else if (curr.getY() == pole.getY()) {
                    if (curr.getX() < pole.getX()){
                        pole = curr;
                        poleNode = temp;
                    }
                }
                temp = temp.next;
            }

            if (poleNode != tail){
                poleNode.data = poleNode.next.data;
                poleNode.next = poleNode.next.next;
            }
            else{
                temp = head;
                while (temp.next.next != null){
                    temp = temp.next;
                }
                temp.next = null;
                tail = temp;
            }

            return pole;

        }
        return null;
    }

    class Node{
        private T data;
        private Node next;
        Node(T data){
            this.data = data;
            next = null;
        }

        Node (){

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
