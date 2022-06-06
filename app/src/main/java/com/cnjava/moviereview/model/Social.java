package com.cnjava.moviereview.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Social implements Serializable {

    @SerializedName("id")
    public int id;
    @SerializedName("imdb_id")
    public String imdbId;
    @SerializedName("facebook_id")
    public String facebookId;
    @SerializedName("instagram_id")
    public String instagramId;
    @SerializedName("twitter_id")
    public String twitterId;
}
