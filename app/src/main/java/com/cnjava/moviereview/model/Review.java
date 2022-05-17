package com.cnjava.moviereview.model;

import java.io.Serializable;

public class Review implements Serializable {

    public int id;
    public int userId;
    public String author;
    public String content;
    public double rating;
    public String createdAt;
    public int movieId;
    public String movieTitle;
    public String avatarPath;
    public boolean isShrink = true;

    public Review(int id, int userId, String author, String content, double rating, String createdAt, int movieId, String movieTitle, String avatarPath) {
        this.id = id;
        this.userId = userId;
        this.author = author;
        this.content = content;
        this.rating = rating;
        this.createdAt = createdAt;
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.avatarPath = avatarPath;
    }
}
