package com.cnjava.moviereview.viewmodel;

import com.cnjava.moviereview.util.Constants;

public class CommonViewModel extends BaseViewModel {

    private static final String TAG = CommonViewModel.class.getName();


    public void getPopularMovie(){
        getApi().getPopularMovie().enqueue(initHandleResponse(Constants.KEY_GET_POPULAR_MOVIE));
    }

    public void getUpcomingMovie(){
        getApi().getUpcomingMovie().enqueue(initHandleResponse(Constants.KEY_GET_UPCOMING_MOVIE));
    }

    public void getTopRatedMovie(){
        getApi().getTopRatedMovie().enqueue(initHandleResponse(Constants.KEY_GET_NOW_PLAYING_MOVIE));
    }

    public void getNowPlayingMovie(){
        getApi().getNowPlayingMovie().enqueue(initHandleResponse(Constants.KEY_GET_TOP_RATED_MOVIE));
    }

    public void getMovieDetail(int id){
        getApi().getMovieDetail(id).enqueue(initHandleResponse(Constants.KEY_GET_MOVIE_DETAIL));
    }
}
