package org.example.zereaab.DataStructures;

import org.example.zereaab.AssigningDataFromCSV.WaterNode;

public class SinglyLinkedListForHashMap {
    private StationEntry head;

    public void insert(String key, WaterNode data) {

        // empty list
        if (head == null) {
            head = new StationEntry(key, data);
            return;
        }

        StationEntry current = head;

        while (current != null) {

            // overwrite if key exists
            if (current.stationKey.equals(key)) {
                current.stationData = data;
                return;
            }

           //add new
            if (current.next == null) {
                current.next = new StationEntry(key, data);
                return;
            }

            current = current.next;
        }
    }

    public WaterNode search(String key) {
        StationEntry current = head;

        while (current != null) {
            if (current.stationKey.equals(key)) {
                return current.stationData;
            }
            current = current.next;
        }
        return null;
    }
}

