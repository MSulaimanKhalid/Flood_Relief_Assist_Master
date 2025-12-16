package org.example.zereaab.AssigningDataFromCSV;

import java.util.ArrayList;
import java.util.List;

public class WaterNode {


    public boolean isJunction;
    public double junctionCapacity; // max safe throughput per day
    public int[] overflowRatio = new int[7]; // 7 days


    public String stationName, partOfRiver, lastReadingAt;
    public double StationHeight, Latitude, Longitude;
    public double outflow, inflow, MaxPeak;

    // graph structure
    public List<WaterNode> downstream = new ArrayList<>();

    // real historical data (from CSV)
    public List<Double> recentOutflows = new ArrayList<>();

    // future predicted data (simulation output)
    public List<Double> predictedOutflows = new ArrayList<>();
}
