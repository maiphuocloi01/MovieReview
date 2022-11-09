package com.cnjava.moviereview.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Actor implements Serializable {

    @SerializedName("id")
    public int id;
    @SerializedName("cast")
    public List<Cast> cast;

    public static class Cast implements Serializable {
        @SerializedName("id")
        public int id;
        @SerializedName("credit_id")
        public String creditId;
        @SerializedName("gender")
        public int gender;
        @SerializedName("name")
        public String name;
        @SerializedName("character")
        public String character;
        @SerializedName("profile_path")
        public String profilePath;
    }

}
