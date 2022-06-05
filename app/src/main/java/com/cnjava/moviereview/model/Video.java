package com.cnjava.moviereview.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Video implements Serializable {

    @SerializedName("id")
    public int id;
    @SerializedName("results")
    public List<VideoDetail> results;

    public static class VideoDetail implements Serializable{

        @SerializedName("id")
        public String id;
        @SerializedName("name")
        public String name;
        @SerializedName("key")
        public String key;
        @SerializedName("site")
        public String site;
        @SerializedName("type")
        public String type;

    }
}
