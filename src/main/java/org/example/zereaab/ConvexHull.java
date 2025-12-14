package org.example.zereaab;

import org.mapsforge.core.model.LatLong;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class ConvexHull {

    final static double radiusOfEarth = 6371000;

    public static ArrayList<Point2D.Double> generate(SingleEndedQueue<Point2D.Double> queue){

        Point2D.Double pole = queue.poleInQueue();
        System.out.println("Pole in hull list: "+"X: "+pole.getX()+" Y: "+pole.getY());

        MinHeapOfAngles heap = new MinHeapOfAngles(pole);
        heap.makeHeapFromQueue(queue);

        Stack<Point2D.Double> st = new Stack<>();
        st.push(pole);
        System.out.println("pole in stack: "+" X: "+st.get(1).getX()+" Y: "+st.get(1).getY());

        int x = heap.size();
        for (int i=0; i< x; i++){
            while (st.size() > 1 && !orientationCCW(st.get(st.size()-1),st.get(st.size()),heap.peek())){
                System.out.println("popping from stack: "+" X: "+st.peek().getX()+" Y: "+st.peek().getY());
                st.pop();
            }
            System.out.println("pushing to stack: X: "+heap.peek().getX()+" Y: "+heap.peek().getY());
            st.push(heap.dequeue());
        }

        if (st.size()<2){
            return null;
        }

        ArrayList<Point2D.Double> hull = new ArrayList<>();
        while (!st.isEmpty()){
            hull.add(st.pop());
        }
        return hull;
    }

    private static boolean orientationCCW (Point2D.Double a, Point2D.Double b, Point2D.Double c){
        double v = a.getX() * (b.getY() - c.getY()) + b.getX() * (c.getY() - a.getY()) + c.getX() * (a.getY() - b.getY());

        return v > 0;
    }

    public static SingleEndedQueue<Point2D.Double> generateHullList(SingleEndedQueue<LatLong> input, double latRef, double longRef){
        SinglyLinkedList<LatLong>.Node tempNode = input.getFirst();
        SingleEndedQueue<Point2D.Double> output = new SingleEndedQueue<>();
        double deltaLat = 0;
        double deltaLong = 0;
        double latRefRadians = Math.toRadians(latRef);
        while(tempNode != null){

            deltaLat = (tempNode.getData()).getLatitude() - latRef;
            deltaLong = (tempNode.getData()).getLongitude() - longRef;

            deltaLat = Math.toRadians(deltaLat);
            deltaLong = Math.toRadians(deltaLong);

            deltaLat = deltaLat * radiusOfEarth;
            deltaLong = deltaLong * Math.cos(latRefRadians) * radiusOfEarth;

            System.out.println("hull list element: "+ "X: "+ deltaLong+" Y: "+deltaLat);
            output.enqueue(new Point2D.Double(deltaLong,deltaLat));

            tempNode = tempNode.getNext();
        }
        System.out.println("num elements in hull list"+output.getSize());
        return output;
    }
}
