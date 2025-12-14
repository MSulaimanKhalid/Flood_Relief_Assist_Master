package org.example.zereaab;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class MinHeapOfAngles {
    ArrayList<Point2D.Double> heap = new ArrayList<>();
    Point2D.Double pole;

    MinHeapOfAngles(Point2D.Double pole){
        this.pole = pole;
    }

    void enqueue(Point2D.Double point){
        heap.add(point);
        heapifyUp(heap.size() - 1);
    }

    Point2D.Double dequeue(){
        if (heap.isEmpty()){
            return null;
        }
        if (heap.size() == 1){
            return heap.removeFirst();
        }

        Point2D.Double value = heap.getFirst();
        heap.set(0, heap.removeLast());
        heapifyDown(0);

        return value;
    }

    Point2D.Double peek(){
        if (heap.isEmpty()){
            return null;
        }
        return heap.getFirst();
    }

    int compare(Point2D.Double a, Point2D.Double b){
        double angleA = Math.atan2(a.getY() - pole.getY(), a.getX() - pole.getX());
        double angleB = Math.atan2(b.getY() - pole.getY(), b.getX() - pole.getX());

        if (angleA < angleB) {
            return -1;
        }
        if (angleA > angleB){
            return 1;
        }

        double distA = dist(pole, a);
        double distB = dist(pole, b);

        return Double.compare(distA, distB);
    }

    void heapifyDown(int index){
        int size = heap.size();

        int left;
        int right;
        int smallest;
        while (true){
            left = 2 * index + 1;
            right = 2 * index + 2;
            smallest = index;

            if (left < size && compare(heap.get(left), heap.get(smallest)) < 0){
                smallest = left;
            }
            if (right < size && compare(heap.get(right), heap.get(smallest)) < 0){
                smallest = right;
            }

            if (smallest != index){
                swap(index, smallest);
                index = smallest;
            }
            else{
                break;
            }
        }
    }

    void heapifyUp(int index){
        while (index > 0) {
            int parent = (index - 1) / 2;

            if (compare(heap.get(index), heap.get(parent)) < 0) {
                swap(index, parent);
                index = parent;
            } else {
                break;
            }
        }
    }

    void swap(int a, int b){
        Point2D.Double temp = heap.get(a);
        heap.set(a, heap.get(b));
        heap.set(b, temp);
    }

    double dist(Point2D.Double pole, Point2D.Double point){
        return (Math.pow(point.getX() - pole.getX(), 2)) +
                (Math.pow(point.getY() - pole.getY(), 2));
    }

    void makeHeapFromQueue(SingleEndedQueue<Point2D.Double> queue){
        for (int i=0; i< queue.getSize(); i++){
            System.out.println("adding to heap: "+" X: "+queue.getFirst().getData().getX()+" Y: "+queue.getFirst().getData().getY());
            enqueue(queue.dequeue());
        }
        System.out.println("elements in heap: "+heap.size());
    }

    int size(){
        return heap.size();
    }


}
