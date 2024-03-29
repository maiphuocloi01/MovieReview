package com.cnjava.moviereview.model;

import androidx.annotation.NonNull;
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
    @SerializedName("title")
    public String title = "";
    @SerializedName("original_title")
    public String original_title = "";
    @SerializedName("overview")
    @NonNull
    public String overview = "";
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

    @Override
    public String toString() {
        return "MovieDetail{" +
                "id=" + id +
                ", backdropPath='" + backdropPath + '\'' +
                ", adult=" + adult +
                ", budget=" + budget +
                ", homepage='" + homepage + '\'' +
                ", title='" + title + '\'' +
                ", overview='" + overview + '\'' +
                ", popularity=" + popularity +
                ", posterPath='" + posterPath + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", revenue=" + revenue +
                ", status='" + status + '\'' +
                ", voteAverage=" + voteAverage +
                ", voteCount=" + voteCount +
                ", genres=" + genres +
                ", runtime=" + runtime +
                ", collection=" + collection +
                '}';
    }
}
