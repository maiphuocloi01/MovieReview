package com.cnjava.moviereview;

import androidx.fragment.app.Fragment;

import com.cnjava.moviereview.model.Favorite;
import com.cnjava.moviereview.model.Genres;
import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.model.MovieDetail;
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

    public List<Favorite> favoriteList;
    public List<Review> reviewList;
    public MovieDetail movieDetail;
    public String fragmentTag;

    public float WIDTH_SCREEN = 0f;
    public float HEIGHT_SCREEN = 0f;



}
