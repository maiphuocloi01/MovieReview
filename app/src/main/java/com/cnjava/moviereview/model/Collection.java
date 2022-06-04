package com.cnjava.moviereview.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Collection implements Serializable {
    @SerializedName("id")
    public int id;
    @SerializedName("name")
    public String name;
    @SerializedName("overview")
    public String overview;
    @SerializedName("poster_path")
    public String poster_path;
    @SerializedName("backdrop_path")
    public String backdrop_path;
    @SerializedName("parts")
    public List<Part> parts;

    public static class Part implements Serializable{

        @SerializedName("id")
        public int id;
        @SerializedName("title")
        public String title;
        @SerializedName("release_date")
        public String releaseDate;
        @SerializedName("poster_path")
        public String posterPath;

    }

}
