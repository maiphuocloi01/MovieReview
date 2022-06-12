package com.cnjava.moviereview.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Favorite implements Serializable {
    @SerializedName("id")
    public String id;
    @SerializedName("userId")
    public String userId;
    @SerializedName("createdAt")
    public String createdAt;
    @SerializedName("movie")
    public MovieFavorite movie;

    public static class MovieFavorite implements Serializable {
        @SerializedName("poster")
        public String poster;
        @SerializedName("id")
        public String id;
        @SerializedName("name")
        public String title;
        @SerializedName("overview")
        public String overview;
        @SerializedName("releaseDate")
        public String releaseDate;

        public MovieFavorite(String poster, String id, String title, String overview, String releaseDate) {
            this.poster = poster;
            this.id = id;
            this.title = title;
            this.overview = overview;
            this.releaseDate = releaseDate;
        }
    }
}
