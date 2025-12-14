package org.example.zereaab;

import java.util.ArrayList;

public class Stack<T> {
    private ArrayList<T> stack = new ArrayList<>();

    public void push(T item) {
        stack.add(item);
    }

    public T pop(){
        if (stack.isEmpty()){
            return null;
        }
        return stack.removeLast();
    }

    public T peek(){
        if (stack.isEmpty()){
            return null;
        }
        return stack.getLast();
    }

    public T get(int index){
        if (index <= 0 || index > stack.size()){
            return null;
        }
        return stack.get(index-1);
    }

    public int size(){
        return stack.size();
    }

    public boolean isEmpty(){
        return stack.isEmpty();
    }

}
