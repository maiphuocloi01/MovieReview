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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getSite() {
            return site;
        }

        public void setSite(String site) {
            this.site = site;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
