package com.cnjava.moviereview.util;

import com.cnjava.moviereview.model.Genres;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Constants {

    public static final String BASE_URL_GUEST = "https://api.themoviedb.org/3/";
    public static final String BASE_URL_USER = "http://viewieapi-env.eba-jprxedaw.ap-southeast-1.elasticbeanstalk.com/api/v1/";
    //public static final String BASE_URL_USER = "http://192.168.43.54:5000/api/v1/";
    public static final String BASE_URL_AI = "https://viewie-ai.herokuapp.com/";
    public static final String BASE_URL_TRANSLATE = "https://translate.yandex.net/api/v1.5/tr.json/";
    public static final String BASE_URL_UPlOAD_IMAGE = "https://nhom01nt118.azurewebsites.net/";
    public static final String IMAGE_URL = "https://image.tmdb.org/t/p/w1280/";
    public static final String IMAGE_URL_YOUTUBE = "https://img.youtube.com/vi/";

    public static final String API_KEY = "AIzaSyBiSqVB9NuQm4dCdH5XW_yLrXnQrqP-2qE";
    public static final String API_KEY_TRANSLATE = "trnsl.1.1.20221031T035646Z.677687ed96cdbd33.d7369050a15c711d554aa46bd8da1f2db4f237d4";

    //share pref
    public static final String ACCESS_TOKEN = "ACCESS_TOKEN_VER2";
    public static final String SAVE_SESSION = "SAVE_SESSION";
    public static final String USERNAME = "USERNAME";
    public static final String ONBOARD = "ONBOARD";

    public static final int ANIM_SLIDE = 0;
    public static final int ANIM_FADE = 1;
    public static final int ANIM_SCALE = 2;


    public static final String KEY_TYPE_RESULT = "type_result";
    public static final String KEY_GET_POPULAR_MOVIE = "KEY_GET_POPULAR_MOVIE";
    public static final String KEY_GET_UPCOMING_MOVIE = "Upcoming Movie";
    public static final String KEY_GET_NOW_PLAYING_MOVIE = "Now Playing Movie";
    public static final String KEY_GET_TOP_RATED_MOVIE = "Top Rated Movie";
    public static final String KEY_GET_MOVIE_DETAIL = "KEY_GET_MOVIE_DETAIL";
    public static final String KEY_SEARCH_MOVIE = "KEY_SEARCH_MOVIE";
    public static final String KEY_SEARCH_MOVIE_BY_CATEGORY = "KEY_SEARCH_MOVIE_BY_CATEGORY";
    public static final String KEY_SEARCH_KEYWORD = "KEY_SEARCH_KEYWORD";
    public static final String KEY_GET_CAST = "KEY_GET_CAST";
    public static final String KEY_GET_RECOMMENDATION = "Recommendation Movie";
    public static final String KEY_GET_MOVIE_BY_KEYWORD_ID = "KEY_GET_MOVIE_BY_KEYWORD_ID";
    public static final String KEY_GET_COLLECTION = "KEY_GET_COLLECTION";
    public static final String KEY_GET_VIDEO = "KEY_GET_VIDEO";
    public static final String KEY_GET_SOCIAL = "KEY_GET_SOCIAL";

    public static final String KEY_REGISTER = "KEY_REGISTER";
    public static final String KEY_SEND_OTP = "KEY_SEND_OTP";
    public static final String KEY_CONFIRM_OTP = "KEY_CONFIRM_OTP";
    public static final String KEY_FORGOT_PASSWORD = "KEY_FORGOT_PASSWORD";
    public static final String KEY_GET_USER_BY_ID = "KEY_GET_USER_BY_ID";
    public static final String KEY_GET_YOUR_PROFILE = "KEY_GET_YOUR_PROFILE";
    public static final String KEY_UPDATE_YOUR_PROFILE = "KEY_UPDATE_YOUR_PROFILE";
    public static final String KEY_LOGIN = "KEY_LOGIN";
    public static final String KEY_UPLOAD_IMAGE = "KEY_UPLOAD_IMAGE";


    //Review
    public static final String KEY_REVIEW_BY_MOVIE_ID = "KEY_REVIEW_BY_MOVIE_ID";
    public static final String KEY_REVIEW_BY_USER_ID = "KEY_REVIEW_BY_USER_ID";
    public static final String KEY_ADD_REVIEW = "KEY_ADD_REVIEW";
    public static final String KEY_UPDATE_REVIEW = "KEY_UPDATE_REVIEW";
    public static final String KEY_DELETE_REVIEW = "KEY_DELETE_REVIEW";
    public static final String KEY_LIKE_REVIEW = "KEY_LIKE_REVIEW";
    public static final String KEY_DISLIKE_REVIEW = "KEY_DISLIKE_REVIEW";


    public static final String KEY_SUMMARIZATION = "KEY_SUMMARIZATION";

    public static final String KEY_GET_MY_STATISTIC = "KEY_GET_MY_STATISTIC";
    public static final String KEY_GET_STATISTIC_BY_USER_ID = "KEY_GET_STATISTIC_BY_USER_ID";


    public static final String KEY_GET_FAVORITE = "KEY_GET_FAVORITE";
    public static final String KEY_ADD_FAVORITE = "KEY_ADD_FAVORITE";
    public static final String KEY_DELETE_FAVORITE = "KEY_DELETE_FAVORITE";

    public static final String EMPTY_STRING = "";
    public static final List<Genres> genresList = Collections.unmodifiableList(
            new ArrayList<Genres>() {{
                add(new Genres(28, "Action"));
                add(new Genres(12, "Adventure"));
                add(new Genres(16, "Animation"));
                add(new Genres(35, "Comedy"));
                add(new Genres(80, "Crime"));
                add(new Genres(99, "Documentary"));
                add(new Genres(18, "Drama"));
                add(new Genres(10751, "Family"));
                add(new Genres(14, "Fantasy"));
                add(new Genres(36, "History"));
                add(new Genres(27, "Horror"));
                add(new Genres(10402, "Music"));
                add(new Genres(9648, "Mystery"));
                add(new Genres(10749, "Romance"));
                add(new Genres(878, "Science Fiction"));
                add(new Genres(10770, "TV Movie"));
                add(new Genres(53, "Thriller"));
                add(new Genres(10752, "War"));
                add(new Genres(37, "Western"));
            }});
}
