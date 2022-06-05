package com.cnjava.moviereview.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Keyword implements Serializable {

    @SerializedName("page")
    public int page;
    @SerializedName("total_pages")
    public int totalPages;
    @SerializedName("total_results")
    public int totalResults;
    @SerializedName("results")
    public List<KeywordDetail> results;

    public static class KeywordDetail implements Serializable{

        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;

    }
}
