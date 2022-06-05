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

    public void searchMovie(String keyword){
        getApi().searchMovie(keyword).enqueue(initHandleResponse(Constants.KEY_SEARCH_MOVIE));
    }

    public void getMovieByCategory(String category){
        String sort = "popularity.desc";
        getApi().getMovieByCategory(sort, category).enqueue(initHandleResponse(Constants.KEY_SEARCH_MOVIE_BY_CATEGORY));
    }

    public void searchKeyword(String keyword){
        getApi().searchKeyword(keyword).enqueue(initHandleResponse(Constants.KEY_SEARCH_KEYWORD));
    }

    public void getCast(int id){
        getApi().getCast(id).enqueue(initHandleResponse(Constants.KEY_GET_CAST));
    }

    public void getRecommendation(int id){
        getApi().getRecommendation(id).enqueue(initHandleResponse(Constants.KEY_GET_RECOMMENDATION));
    }

    public void getMovieByKeywordId(int id){
        getApi().getMovieByKeywordId(id).enqueue(initHandleResponse(Constants.KEY_GET_MOVIE_BY_KEYWORD_ID));
    }

    public void getCollection(int id){
        getApi().getCollection(id).enqueue(initHandleResponse(Constants.KEY_GET_COLLECTION));
    }

    public void getVideo(int id){
        getApi().getVideo(id).enqueue(initHandleResponse(Constants.KEY_GET_VIDEO));
    }
}
