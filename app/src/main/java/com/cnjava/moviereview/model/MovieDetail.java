package com.cnjava.moviereview.model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MovieDetail implements Serializable {

    @SerializedName("id")
    public int id;
    @SerializedName("backdrop_path")
    public String backdropPath;
    @SerializedName("adult")
    public boolean adult;
    @SerializedName("budget")
    public long budget;
    @SerializedName("homepage")
    public String homepage;
    @Nullable
    @SerializedName("original_title")
    public String title;
    @SerializedName("overview")
    public String overview;
    @SerializedName("popularity")
    public double popularity;
    @SerializedName("poster_path")
    public String posterPath;
    @SerializedName("release_date")
    public String releaseDate;
    @SerializedName("revenue")
    public long revenue;
    @SerializedName("status")
    public String status;
    @SerializedName("vote_average")
    public float voteAverage;
    @SerializedName("vote_count")
    public int voteCount;
    @SerializedName("genres")
    public List<Genres> genres;
    @SerializedName("runtime")
    public int runtime;
    @SerializedName("belongs_to_collection")
    public Collection collection;

    public static class Genres implements Serializable{

        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;

    }

}
