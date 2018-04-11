package com.soenkek.popularmoviesstage2.models;

/**
 * Created by Sönke on 30.03.2018.
 */

public class ReviewObject {

    private final String author;
    private final String content;

    public ReviewObject(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public String getAuthor() {return author;}

    public String getContent() {return content;}
}
