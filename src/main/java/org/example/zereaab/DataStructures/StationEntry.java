package org.example.zereaab.DataStructures;

import org.example.zereaab.AssigningDataFromCSV.WaterNode;

class StationEntry {
    String stationKey;
    WaterNode stationData;
    StationEntry next;   // for collision handling

    StationEntry(String stationKey, WaterNode stationData) {
        this.stationKey = stationKey;
        this.stationData = stationData;
        this.next = null;
    }
}
