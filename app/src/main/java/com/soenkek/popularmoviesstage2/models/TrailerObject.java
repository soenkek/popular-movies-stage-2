package com.soenkek.popularmoviesstage2.models;

/**
 * Created by Sönke on 29.03.2018.
 */

public class TrailerObject {

    private final String name;
    private final String type;
    private final String key;

    public TrailerObject(String name, String type, String key) {
        this.name = name;
        this.type = type;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getKey() {
        return key;
    }
}
