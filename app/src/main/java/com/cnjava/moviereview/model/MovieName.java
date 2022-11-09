package com.cnjava.moviereview.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MovieName implements Serializable {
    @SerializedName("results")
    public List<MovieNameResult> results;
    public static class MovieNameResult implements Serializable{
        @SerializedName("title")
        public String title;
    }
}
