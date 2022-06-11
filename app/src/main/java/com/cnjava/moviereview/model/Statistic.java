package com.cnjava.moviereview.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Statistic implements Serializable {
    @SerializedName("avgStars")
    public double avgStars;
    @SerializedName("reviews")
    public double reviews;
    @SerializedName("dislikes")
    public double dislikes;
    @SerializedName("likes")
    public double likes;
}
