package com.cnjava.moviereview.viewmodel;

import android.util.Log;

import com.cnjava.moviereview.model.Response;
import com.cnjava.moviereview.model.Review;
import com.cnjava.moviereview.model.User;
import com.cnjava.moviereview.util.Constants;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CommonViewModel extends BaseViewModel {

    private static final String TAG = CommonViewModel.class.getName();

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "onCleared: ");
    }

    public void getPopularMovie(){
        getApi().getPopularMovie().enqueue(initHandleResponse(Constants.KEY_GET_POPULAR_MOVIE));
    }

    public void getUpcomingMovie(){
        getApi().getUpcomingMovie().enqueue(initHandleResponse(Constants.KEY_GET_UPCOMING_MOVIE));
    }

    public void getTopRatedMovie(){
        getApi().getTopRatedMovie().enqueue(initHandleResponse(Constants.KEY_GET_TOP_RATED_MOVIE));
    }

    public void getNowPlayingMovie(){
        getApi().getNowPlayingMovie().enqueue(initHandleResponse(Constants.KEY_GET_NOW_PLAYING_MOVIE));
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

    public void getSocial(int id){
        getApi().getSocial(id).enqueue(initHandleResponse(Constants.KEY_GET_SOCIAL));
    }


    //User API
    public void registration(User user){
        getUserApi().registration(user).enqueue(initHandleResponse(Constants.KEY_REGISTER));
    }

    public void sendOTP(String email){
        getUserApi().sendOTP(new User(email)).enqueue(initHandleResponse(Constants.KEY_SEND_OTP));
    }

    public void confirmOTP(String email, String token){
        Response response = new Response(email, token);
        getUserApi().confirmOTP(response).enqueue(initHandleResponse(Constants.KEY_CONFIRM_OTP));
    }

    public void forgotPassword(User user){
        getUserApi().forgotPassword(user).enqueue(initHandleResponse(Constants.KEY_FORGOT_PASSWORD));
    }

    public void getUserById(String userId){
        getUserApi().getUserById(userId).enqueue(initHandleResponse(Constants.KEY_GET_USER_BY_ID));
    }

    public void getYourProfile(String token){
        getUserApi().getYourProfile("Bearer " + token).enqueue(initHandleResponse(Constants.KEY_GET_YOUR_PROFILE));
    }

    public void updateProfile(User user, String token){
        getUserApi().updateProfile(user,"Bearer " + token).enqueue(initHandleResponse(Constants.KEY_UPDATE_YOUR_PROFILE));
    }

    public void login(String email, String password) {
        getUserApi().login(email, password).enqueue(initHandleResponse(Constants.KEY_LOGIN));
    }

    public void uploadImageAccount(MultipartBody.Part parts, RequestBody someData) {
        uploadImageApi().uploadImage(parts, someData).enqueue(initHandleResponse(Constants.KEY_UPLOAD_IMAGE));
    }

    public void getReviewByMovieId(String id) {
        getUserApi().getReviewByMovieId(id).enqueue(initHandleResponse(Constants.KEY_REVIEW_BY_MOVIE_ID));
    }

    public void addReview(Review review, String token) {
        getUserApi().addReview(review,"Bearer " + token).enqueue(initHandleResponse(Constants.KEY_ADD_REVIEW));
    }

    public void likeReview(String id, String token) {
        getUserApi().likeReview(id,"Bearer " + token).enqueue(initHandleResponse(Constants.KEY_LIKE_REVIEW));
    }

    public void dislikeReview(String id, String token) {
        getUserApi().dislikeReview(id,"Bearer " + token).enqueue(initHandleResponse(Constants.KEY_DISLIKE_REVIEW));
    }

    public void summarizationReview(String text) {
        aiApi().summarizationReview(text).enqueue(initHandleResponse(Constants.KEY_SUMMARIZATION));
    }

}
