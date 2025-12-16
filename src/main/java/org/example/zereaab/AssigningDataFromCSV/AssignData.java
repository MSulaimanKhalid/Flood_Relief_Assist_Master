package org.example.zereaab.AssigningDataFromCSV;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import org.example.zereaab.DataStructures.HashMap;

public class AssignData {

    private final String csvFilePath = "C:\\this shi right here\\uni\\sem 3\\DSA\\semProject\\assisstMasterheh\\Flood_Relief_Assist_Master\\scrape\\river_state_extracted.csv";

    // Water Nodes
    public WaterNode TarbelaDam = new WaterNode();
    public WaterNode Kabul = new WaterNode();
    public WaterNode Kalabagh = new WaterNode();
    public WaterNode Chashma = new WaterNode();
    public WaterNode Taunsa = new WaterNode();
    public WaterNode Marala = new WaterNode();
    public WaterNode Trimmu = new WaterNode();
    public WaterNode Panjnad = new WaterNode();
    public WaterNode ManglaDam = new WaterNode();
    public WaterNode Guddu = new WaterNode();
    public WaterNode Sukkur = new WaterNode();
    public WaterNode Kotri = new WaterNode();


    public void load() throws Exception {
        HashMap latestDataMap = new HashMap(20);

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            // Skip header
            br.readLine();

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                WaterNode node = parseCSVLineToAssignDataToWaterNodes(line);
                String key = node.stationName.trim().toLowerCase();

                // Check if node already exists in the map
                WaterNode existingNode = latestDataMap.get(key);
                if (existingNode != null) {
                    // Node already exists → add new outflow to its recentOutflows
                    addToRecentOutflows(existingNode, node.outflow);

                    // Update other fields if needed (like StationHeight, Latitude, etc.)
                    existingNode.StationHeight = node.StationHeight;
                    existingNode.Latitude = node.Latitude;
                    existingNode.Longitude = node.Longitude;
                    existingNode.lastReadingAt = node.lastReadingAt;
                    existingNode.MaxPeak = node.MaxPeak;
                    existingNode.inflow = node.inflow;
                    existingNode.outflow=node.outflow;
                } else {
                    // First occurrence → put new node in map and add outflow
                    latestDataMap.put(key, node);
                    addToRecentOutflows(node, node.outflow);
                }

            }

        }

        TarbelaDam = latestDataMap.get("tarbela dam");
        Kabul      = latestDataMap.get("kabul");
        Kalabagh   = latestDataMap.get("kalabagh");
        Chashma    = latestDataMap.get("chashma");
        Taunsa     = latestDataMap.get("taunsa");
        Marala     = latestDataMap.get("marala");
        Trimmu     = latestDataMap.get("trimmu");
        Panjnad    = latestDataMap.get("panjnad");
        ManglaDam  = latestDataMap.get("mangla dam");
        Guddu      = latestDataMap.get("guddu");
        Sukkur     = latestDataMap.get("sukkur");
        Kotri      = latestDataMap.get("kotri");

    }

    private WaterNode parseCSVLineToAssignDataToWaterNodes(String row) {
        // Use manual parsing for CSV with quotes
        String[] columns = parseCSV(row);

        WaterNode node = new WaterNode();

        // Check if we have enough columns
        if (columns.length >= 9) {
            node.stationName = columns[0].trim();
            node.partOfRiver = columns[1].trim();
            node.StationHeight = convertToDouble(columns[2]);
            node.Latitude = convertToDouble(columns[3]);
            node.Longitude = convertToDouble(columns[4]);
            node.outflow = extractNumber(columns[5]);
            node.lastReadingAt = columns[6].trim();
            node.MaxPeak = extractNumber(columns[7]);
            node.inflow = extractNumber(columns[8]);
        } else {
            System.out.println("WARNING: Not enough columns. Row: " + row);
        }

        return node;
    }

    // Proper CSV parser that handles quoted fields
    private String[] parseCSV(String line) {
        List<String> result = new java.util.ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                // Toggle quote mode
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                // Comma outside quotes = field separator
                result.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }

        // Add the last field
        result.add(current.toString());

        return result.toArray(new String[0]);
    }

    private double extractNumber(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0;
        }

        text = text.trim();

        // Remove quotes if present
        if (text.startsWith("\"") && text.endsWith("\"")) {
            text = text.substring(1, text.length() - 1);
        }

        // Remove everything after first parenthesis if it exists
        int parenIndex = text.indexOf('(');
        if (parenIndex != -1) {
            text = text.substring(0, parenIndex).trim();
        }

        // Remove commas from number
        text = text.replace(",", "").trim();

        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            System.out.println("Could not parse number from: '" + text + "'");
            return 0;
        }
    }

    private double convertToDouble(String text) {
        if (text == null || text.trim().isEmpty()) return 0;

        text = text.trim();

        // Remove quotes if present
        if (text.startsWith("\"") && text.endsWith("\"")) {
            text = text.substring(1, text.length() - 1);
        }

        text = text.replace("m", "")
                .replace(",", "")
                .trim();

        if (text.isEmpty()) return 0;

        try {
            return Double.parseDouble(text);
        } catch (Exception e) {
            System.out.println("Could not parse to double: '" + text + "'");
            return 0;
        }
    }

    private void addToRecentOutflows(WaterNode node, double outflow) {
        if (node == null) return;

        node.recentOutflows.add(outflow);
        if (node.recentOutflows.size() > 7) {
            node.recentOutflows.remove(0); // removing the oldest one
        }
    }


}