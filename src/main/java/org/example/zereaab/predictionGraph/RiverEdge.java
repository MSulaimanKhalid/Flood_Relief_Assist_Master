package org.example.zereaab.predictionGraph;

import org.example.zereaab.DataStructures.CircularQueue;
import org.example.zereaab.AssigningDataFromCSV.WaterNode;

public class RiverEdge {

    public WaterNode source,destination;
    public int delayDaysForWaterToReach;          // how many days it takes to reach destination
    public double attenuation;     // fraction of water that survives

    // water in transit
    CircularQueue<Double> bufferToHoldWaterDelay;

    // Constructor
    public RiverEdge(WaterNode source, WaterNode destination, int delayDaysForWaterToReach, double attenuation) {
        this.source = source;
        this.destination = destination;
        this.delayDaysForWaterToReach = delayDaysForWaterToReach;
        this.attenuation = attenuation;

        // initialize buffer with zeros
        bufferToHoldWaterDelay = new CircularQueue<>(delayDaysForWaterToReach);
        for (int i = 0; i < delayDaysForWaterToReach; i++) bufferToHoldWaterDelay.enqueue(0.0);
    }

    // push water from source node
    public void waterReleasedFromSource(double waterAmount) {
        // water attenuates immediately
        double waterThatActuallyReachesAfterLoss = waterAmount * attenuation;
        bufferToHoldWaterDelay.enqueue(waterThatActuallyReachesAfterLoss);
    }

    // advance the edge by one day
    public double advanceDay() {
        // deliver water that reached the end
        double delivered = bufferToHoldWaterDelay.dequeue();  // first in = ready to deliver
        return delivered;
    }

    //test purpose
    public String getBufferState() {
        return bufferToHoldWaterDelay.toString();
    }



}
