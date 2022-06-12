package com.cnjava.moviereview.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Review implements Serializable {

    @SerializedName("id")
    public String id;
    @SerializedName("content")
    public String content;
    @SerializedName("stars")
    public double rating;
    @SerializedName("createdAt")
    public String createdAt;
    @SerializedName("updatedAt")
    public String updatedAt;
    @SerializedName("like")
    public List<String> like;
    @SerializedName("dislike")
    public List<String> dislike;
    public boolean isShrink = true;
    @SerializedName("author")
    public User user;
    @SerializedName("movie")
    public MovieReview movie;

    public boolean isLike = false;
    public boolean isDislike = false;

    public static class MovieReview implements Serializable{
        @SerializedName("poster")
        public String backdropPath;
        @SerializedName("id")
        public String id;
        @SerializedName("name")
        public String title;
        @SerializedName("overview")
        public String overview;
        @SerializedName("releaseDate")
        public String releaseDate;

        public MovieReview(String backdropPath, String id, String title, String overview, String releaseDate) {
            this.backdropPath = backdropPath;
            this.id = id;
            this.title = title;
            this.overview = overview;
            this.releaseDate = releaseDate;
        }
    }

    public Review(String content, double rating, MovieReview movie) {
        this.content = content;
        this.rating = rating;
        this.movie = movie;
    }

    public Review(String content, double rating) {
        this.content = content;
        this.rating = rating;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
