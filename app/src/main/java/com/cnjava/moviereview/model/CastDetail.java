package com.cnjava.moviereview.model;


import java.io.Serializable;
import java.util.ArrayList;

public class CastDetail implements Serializable {
    public String credit_type;
    public String department;
    public String job;
    public Media media;
    public String media_type;
    public String id;
    public Person person;

    public static class Media implements Serializable {
        public boolean adult;
        public String backdrop_path;
        public int id;
        public String title;
        public String original_language;
        public String original_title;
        public String overview;
        public String poster_path;
        public String media_type;
        public ArrayList<Integer> genre_ids;
        public double popularity;
        public String release_date;
        public boolean video;
        public double vote_average;
        public int vote_count;
        public String character;
    }

    public static class Person implements Serializable {
        public boolean adult;
        public int id;
        public String name;
        public String original_name;
        public String media_type;
        public double popularity;
        public int gender;
        public String known_for_department;
        public String profile_path;
    }
}
