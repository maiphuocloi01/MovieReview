package com.cnjava.moviereview;

import com.cnjava.moviereview.model.Genres;
import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.model.Review;
import com.cnjava.moviereview.model.User;

import java.util.ArrayList;
import java.util.List;

public class Storage {
    public Movie moviePopular;
    public Movie movieNowPlaying;
    public Movie movieUpcoming;
    public Movie movieTopRated;
    public Movie movieRecommend;
    public User myUser;
    public List<Genres> genresList = new ArrayList<>();
    public List<Review> reviewList;

}
