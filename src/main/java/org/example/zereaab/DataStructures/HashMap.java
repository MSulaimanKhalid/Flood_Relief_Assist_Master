package org.example.zereaab.DataStructures;

import org.example.zereaab.AssigningDataFromCSV.WaterNode;

public class HashMap {
    private SinglyLinkedListForHashMap[] buckets;
    private int bucketCount;

    public HashMap(int bucketCount) {
        this.bucketCount = bucketCount;
        buckets = new SinglyLinkedListForHashMap[bucketCount];

        for (int i = 0; i < bucketCount; i++) {
            buckets[i] = new SinglyLinkedListForHashMap();
        }
    }

    private int HashFunctionToGetBucketIndex(String key) {
        int hash = 0;
        for (int i = 0; i < key.length(); i++) {
            hash += key.charAt(i);
        }
        return hash % bucketCount;
    }

    public void put(String key, WaterNode data) {
        int index = HashFunctionToGetBucketIndex(key);
        buckets[index].insert(key, data);
    }

    public WaterNode get(String key) {
        int index = HashFunctionToGetBucketIndex(key);
        return buckets[index].search(key);
    }

}
