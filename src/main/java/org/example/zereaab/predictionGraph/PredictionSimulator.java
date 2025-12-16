package org.example.zereaab.predictionGraph;

import org.example.zereaab.AssigningDataFromCSV.WaterNode;
import java.util.HashSet;
import java.util.Set;

public class PredictionSimulator {

    private RiverGraph graph;

    public PredictionSimulator(RiverGraph graph) {
        this.graph = graph;
    }

    // Weighted average of last 7 days
    private double weightedTrend(WaterNode node) {
        double[] w = {0.25, 025., 0.15,0.15, 0.1, 0.05, 0.05};
        double sum = 0;

        for (int i = 0; i < 7; i++) {
            sum += node.recentOutflows.get(i) * w[i];
        }
        return sum;
    }

    public void run() {

        // Collect all nodes
        Set<WaterNode> nodes = new HashSet<>();
        for (RiverEdge e : graph.edges) {
            nodes.add(e.source);
            nodes.add(e.destination);
        }

        // 🔹 Preload edge buffers with historical data
        for (RiverEdge edge : graph.edges) {
            int d = edge.delayDaysForWaterToReach;
            WaterNode src = edge.source;

            for (int i = 7 - d; i < 7; i++) {
                edge.waterReleasedFromSource(src.recentOutflows.get(i));
            }
        }

        // 🔹 Predict ONLY next 7 days
        // 🔹 Predict ONLY next 7 days
        for (int day = 1; day <= 7; day++) {

            System.out.println("=== Prediction Day " + day + " ===");

            for (WaterNode node : nodes) {

                // Water reaching today from upstream
                double inflow = 0;
                for (RiverEdge e : graph.edges) {
                    if (e.destination == node) {
                        inflow += e.advanceDay();
                    }
                }
                if (node.isJunction) {
                    if (inflow > node.junctionCapacity) {
                        inflow= node.junctionCapacity-inflow;
                    }
                }


                        // Trend from last 7 days
                double trend = weightedTrend(node);

                // Final predicted outflow
               /*before adding in constraint one
                double predicted =
                        Math.min(node.MaxPeak, trend + inflow);*/

                double rawPrediction = Math.min(node.MaxPeak, trend + inflow);
                //C1
                double predictedAfterC1 = applyRateLimit(rawPrediction, node,day);
                //C2
                double predictedAfterC2=applyMeanReversion(predictedAfterC1, node);



                node.predictedOutflows.add(predictedAfterC2);

                // 🔹 Update sliding window
                node.recentOutflows.add(predictedAfterC2);
                node.recentOutflows.remove(0);

                int ratio = (int)((predictedAfterC2 / node.MaxPeak) * 100);
                node.overflowRatio[day - 1] = ratio;

                if(node.stationName.equals("Chashma")) {
                    System.out.println("\nChashma: " + node.stationName);
                    System.out.println("  Inflow   → " + inflow);
                    System.out.println("  Trend    → " + trend);
                    System.out.println("  Predicted→ " + predictedAfterC2);
                    System.out.println("  Window   → " + node.recentOutflows);
                    System.out.println("  Overflow %→ " + ratio + "%");
                }



                // Push downstream
                for (RiverEdge e : graph.edges) {
                    if (e.source == node) {
                        e.waterReleasedFromSource(predictedAfterC2);
                    }
                }
            }

            System.out.println("---------------------------");
        }


        //tester
//        private void printWindow(WaterNode node) {
//            System.out.println(
//                    node.stationName + " window → " + node.recentOutflows
//            );
//        }

    }

    //limiters
    // Rate-of-Change limiter (Node constraint #1)
    private double applyRateLimit(double predicted, WaterNode node,int day) {

        double yesterday = node.recentOutflows.get(day-1); // last known day
        double maxIncrease = yesterday * 1.15; // +15%
        double maxDecrease = yesterday * 0.85; // -15%

        if (predicted > maxIncrease) return maxIncrease;
        if (predicted < maxDecrease) return maxDecrease;

        return predicted;
    }

    //constraint 2
    private double equilibrium(WaterNode node) {
        double sum = 0;
        for (double v : node.recentOutflows)
            sum += v;
        return sum / node.recentOutflows.size();
    }
    private double applyMeanReversion(double predicted, WaterNode node) {

        double eq = equilibrium(node);
        double strength = 0.20; // 20% pull (tunable)

        return predicted + strength * (eq - predicted);
    }



}
