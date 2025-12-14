package org.example.zereaab;

import java.awt.geom.Point2D;

public class SingleEndedQueue <T> {
    private final SinglyLinkedList<T> list = new SinglyLinkedList<>();

    void enqueue(T data){
        list.addLast(data);
    }

    T dequeue (){
        return list.removeFirst();
    }

    public int getSize() {
        return list.getSize();
    }

    public Point2D.Double poleInQueue (){
        if (list.getHead().getData() instanceof Point2D.Double) {
            return list.poleInList();
        }
        System.out.println("queue is not of Point2D.Double");
        return null;
    }

    public SinglyLinkedList<T>.Node getFirst(){
        return list.getHead();
    }

}
