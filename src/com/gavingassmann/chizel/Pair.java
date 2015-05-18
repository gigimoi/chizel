package com.gavingassmann.chizel;

/**
 * Created by Gassmann844 on 5/18/2015.
 */
public class Pair<T, T1> {
    T key;
    T1 value;

    public Pair(T key, T1 value) {
        this.key = key;
        this.value = value;
    }

    T getKey() {
        return key;
    }
    T1 getValue() {
        return value;
    }
}
