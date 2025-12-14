package org.example.zereaab;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class minHeapofanglesOLD {
    ArrayList<Point2D.Double> heap = new ArrayList<>();
    Point2D.Double pole;

    minHeapofanglesOLD(Point2D.Double  pole){
        this.pole = pole;
    }

    void enqueue(Point2D.Double point){
        if (heap.isEmpty()){
            heap.addFirst(point);
            return;
        }
        heap.add(point);
        heapifyUp(heap.size()-1);
    }

    Point2D.Double dequeue(){
        if (heap.isEmpty()){
            return null;
        }
        Point2D.Double value = heap.getFirst();
        heap.set(0,heap.removeLast());
        heapifyDown(0);
        return value;
    }

    void heapifyDown(int index){
        Point2D.Double smallerChild;
        Point2D.Double parent;
        Point2D.Double rightChild;
        double leftAngle;
        double rightAngle;
        double parentAngle;
        double smallerChildAngle;
        int childIndex;
        while (index < heap.size()) {
            parent = heap.get(index);
            if (2*index+1 < heap.size()){
                smallerChild = heap.get(2*index+1);
                childIndex = 2*index+1;
                if (2*index + 2 < heap.size()){
                    rightChild = heap.get(2*index + 2 );
                    leftAngle = Math.atan2(smallerChild.getY() - pole.getY(), smallerChild.getX() - pole.getX());
                    rightAngle = Math.atan2(rightChild.getY() - pole.getY(), rightChild.getX() - pole.getX());
                    Point2D.Double temp;
                    if (rightAngle < leftAngle){
                        temp = smallerChild;
                        smallerChild = rightChild;
                        rightChild = temp;
                        childIndex = 2*index + 2;
                    }
                    else if(rightAngle == leftAngle){
                        if (dist(pole,rightChild) < dist(pole,smallerChild)){
                            temp = smallerChild;
                            smallerChild = rightChild;
                            rightChild = temp;
                            childIndex = 2*index + 2;
                        }
                    }
                }
            }
            else{
                break;
            }

            parentAngle = Math.atan2(parent.getY() - pole.getY(), parent.getX() - pole.getX());
            smallerChildAngle = Math.atan2(smallerChild.getY() - pole.getY(), smallerChild.getX() - pole.getX());
            Point2D.Double temp;
            if (smallerChildAngle < parentAngle){
                temp = parent;
                parent = smallerChild;
                smallerChild = temp;
            } else if (smallerChildAngle == parentAngle) {
                if (dist(pole, smallerChild) < dist(pole, parent)){
                    temp = parent;
                    parent = smallerChild;
                    smallerChild = temp;
                }
            }
            index = childIndex;
        }
    }

    void heapifyUp(int index){
        Point2D.Double child;
        Point2D.Double parent;
        double childAngle;
        double parentAngle;
        while (index >= 0){
            child = heap.get(index);
            parent = heap.get((index-1)/2);
            childAngle = Math.atan2(child.getY() - pole.getY(),child.getX() - pole.getX());
            parentAngle = Math.atan2(parent.getY() - pole.getY(), parent.getX() - pole.getX());
            Point2D.Double temp;
            if (childAngle < parentAngle){
                temp = parent;
                parent = child;
                child = temp;
            } else if ((childAngle == parentAngle)) {
                if (dist(pole,child) < dist(pole,parent)){
                    temp = parent;
                    parent = child;
                    child = temp;
                }
            }
            index = (index - 1)/2;
        }
    }

    double dist(Point2D.Double pole, Point2D.Double point){
        return (Math.pow(point.getX() - pole.getX(), 2)) + (Math.pow(point.getY() - pole.getY(), 2));
    }

}
