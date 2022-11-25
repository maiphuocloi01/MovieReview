package com.cnjava.moviereview.model;

import java.io.Serializable;
import java.util.ArrayList;

public class PeopleImage implements Serializable {
    public int id;
    public ArrayList<Profile> profiles;

    public static class Profile {
        public double aspect_ratio;
        public int height;
        public Object iso_639_1;
        public String file_path;
        public double vote_average;
        public int vote_count;
        public int width;
    }
}
