package com.cnjava.moviereview.util;

public interface Constants {

    String BASE_URL_GUEST = "https://api.themoviedb.org/3/";
    String BASE_URL_USER = "https://viewie-api.herokuapp.com/api/v1/";
    String BASE_URL_AI = "https://viewie-ai.herokuapp.com/";
    String BASE_URL_UPlOAD_IMAGE = "https://nhom01nt118.azurewebsites.net/";
    String IMAGE_URL = "https://image.tmdb.org/t/p/w1280/";
    String IMAGE_URL_YOUTUBE = "https://img.youtube.com/vi/";

    //share pref
    String ACCESS_TOKEN = "ACCESS_TOKEN";
    String USERNAME = "USERNAME";

    int ANIM_SLIDE = 0;
    int ANIM_FADE = 1;
    int ANIM_SCALE = 2;

    String KEY_GET_POPULAR_MOVIE = "KEY_GET_POPULAR_MOVIE";
    String KEY_GET_UPCOMING_MOVIE = "KEY_GET_UPCOMING_MOVIE";
    String KEY_GET_NOW_PLAYING_MOVIE = "KEY_GET_NOW_PLAYING_MOVIE";
    String KEY_GET_TOP_RATED_MOVIE = "KEY_GET_TOP_RATED_MOVIE";
    String KEY_GET_MOVIE_DETAIL = "KEY_GET_MOVIE_DETAIL";
    String KEY_SEARCH_MOVIE = "KEY_SEARCH_MOVIE";
    String KEY_SEARCH_MOVIE_BY_CATEGORY = "KEY_SEARCH_MOVIE_BY_CATEGORY";
    String KEY_SEARCH_KEYWORD = "KEY_SEARCH_KEYWORD";
    String KEY_GET_CAST = "KEY_GET_CAST";
    String KEY_GET_RECOMMENDATION = "KEY_GET_RECOMMENDATION";
    String KEY_GET_MOVIE_BY_KEYWORD_ID = "KEY_GET_MOVIE_BY_KEYWORD_ID";
    String KEY_GET_COLLECTION = "KEY_GET_COLLECTION";
    String KEY_GET_VIDEO = "KEY_GET_VIDEO";
    String KEY_GET_SOCIAL = "KEY_GET_SOCIAL";

    String KEY_REGISTER = "KEY_REGISTER";
    String KEY_SEND_OTP = "KEY_SEND_OTP";
    String KEY_CONFIRM_OTP = "KEY_CONFIRM_OTP";
    String KEY_FORGOT_PASSWORD = "KEY_FORGOT_PASSWORD";
    String KEY_GET_USER_BY_ID = "KEY_GET_USER_BY_ID";
    String KEY_GET_YOUR_PROFILE = "KEY_GET_YOUR_PROFILE";
    String KEY_UPDATE_YOUR_PROFILE = "KEY_UPDATE_YOUR_PROFILE";
    String KEY_LOGIN = "KEY_LOGIN";
    String KEY_UPLOAD_IMAGE = "KEY_UPLOAD_IMAGE";


    //Review
    String KEY_REVIEW_BY_MOVIE_ID = "KEY_REVIEW_BY_MOVIE_ID";
    String KEY_ADD_REVIEW = "KEY_ADD_REVIEW";
    String KEY_LIKE_REVIEW = "KEY_LIKE_REVIEW";
    String KEY_DISLIKE_REVIEW = "KEY_DISLIKE_REVIEW";


    String KEY_SUMMARIZATION = "KEY_SUMMARIZATION";

}
