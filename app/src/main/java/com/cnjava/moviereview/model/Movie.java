package com.cnjava.moviereview.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Movie implements Serializable {

    @SerializedName("page")
    public int page;
    @SerializedName("results")
    public List<Result> results;
    @SerializedName("total_pages")
    public int total_pages;
    @SerializedName("total_results")
    public int total_results;

    public static class Result implements Serializable{

        @SerializedName("backdrop_path")
        public String backdropPath;
        @SerializedName("id")
        public int id;
        @SerializedName("original_title")
        public String title;
        @SerializedName("overview")
        public String overview;
        @SerializedName("poster_path")
        public String posterPath;
        @SerializedName("release_date")
        public String releaseDate;
        @SerializedName("vote_average")
        public double voteAverage;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "page=" + page +
                ", results=" + results +
                ", total_pages=" + total_pages +
                ", total_results=" + total_results +
                '}';
    }
}
