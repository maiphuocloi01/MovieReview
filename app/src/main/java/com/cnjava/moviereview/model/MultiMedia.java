package com.cnjava.moviereview.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MultiMedia implements Serializable {

    @SerializedName("page")
    public int page;
    @SerializedName("results")
    public List<Result> results;
    @SerializedName("total_pages")
    public int total_pages;
    @SerializedName("total_results")
    public int total_results;

    public static class Result implements Serializable {
        public boolean adult;
        public String backdrop_path;
        public ArrayList<Integer> genre_ids;
        public int id;
        public String media_type;
        public String original_language;
        public String original_title;
        public String overview;
        public double popularity;
        public String poster_path;
        public String release_date;
        public String title;
        public boolean video;
        public double vote_average;
        public int vote_count;
        public String first_air_date;
        public String name;
        public ArrayList<String> origin_country;
        public String original_name;
    }
}
