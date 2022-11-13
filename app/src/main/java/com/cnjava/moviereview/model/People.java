package com.cnjava.moviereview.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class People {
    @SerializedName("adult")
    public boolean adult;
    @SerializedName("also_known_as")
    public ArrayList<String> also_known_as;
    @SerializedName("biography")
    @NonNull
    public String biography = "";
    @SerializedName("birthday")
    public String birthday = "";
    @SerializedName("deathday")
    public String deathday = "";
    @SerializedName("gender")
    public int gender;
    @SerializedName("homepage")
    public String homepage = "";
    @SerializedName("id")
    public int id;
    @SerializedName("imdb_id")
    public String imdb_id = "";
    @SerializedName("known_for_department")
    public String known_for_department = "";
    @SerializedName("name")
    public String name;
    @SerializedName("place_of_birth")
    public String place_of_birth = "";
    @SerializedName("popularity")
    public double popularity;
    @SerializedName("profile_path")
    public String profile_path = "";
}