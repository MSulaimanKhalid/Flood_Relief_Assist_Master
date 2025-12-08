package org.example.zereaab;

public class SingleEndedQueue <T> {
    private final SinglyLinkedList<T> list = new SinglyLinkedList<>();

    void enqueue(T data){
        list.addLast(data);
    }

    T dequeue (){
        return list.removeFirst();
    }

}
