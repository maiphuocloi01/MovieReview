package com.cnjava.moviereview.data.datasource;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingState;
import androidx.paging.rxjava3.RxPagingSource;

import com.cnjava.moviereview.data.MovieService;
import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.util.Constants;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MovieDataSource extends RxPagingSource<Integer, Movie.Result> {

    private MovieService movieService;
    private Integer nextPageNumber;
    private String type;
    private String query;

    public MovieDataSource(@NonNull MovieService movieService, String key, String query) {
        this.movieService = movieService;
        this.type = key;
        this.query = query;
    }

    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, Movie.Result> pagingState) {
        Integer anchorPosition = pagingState.getAnchorPosition();
        if (anchorPosition == null) {
            return null;
        }

        LoadResult.Page<Integer, Movie.Result> anchorPage = pagingState.closestPageToPosition(anchorPosition);
        if (anchorPage == null) {
            return null;
        }

        Integer prevKey = anchorPage.getPrevKey();
        if (prevKey != null) {
            return prevKey + 1;
        }

        Integer nextKey = anchorPage.getNextKey();
        if (nextKey != null) {
            return nextKey - 1;
        }
        return null;
    }

    @NonNull
    @Override
    public Single<LoadResult<Integer, Movie.Result>> loadSingle(@NonNull LoadParams<Integer> loadParams) {
        nextPageNumber = loadParams.getKey();
        if (nextPageNumber == null) {
            nextPageNumber = 1;
        }
        if (type.equals(Constants.KEY_GET_UPCOMING_MOVIE)) {
            return movieService.getUpcomingMoviePaging(nextPageNumber)
                    .subscribeOn(Schedulers.io())
                    .map(obj -> toLoadResult(nextPageNumber, obj))
                    .onErrorReturn(LoadResult.Error::new);
        } else if (type.equals(Constants.KEY_GET_NOW_PLAYING_MOVIE)) {
            return movieService.getNowPlayingMoviePaging(nextPageNumber)
                    .subscribeOn(Schedulers.io())
                    .map(obj -> toLoadResult(nextPageNumber, obj))
                    .onErrorReturn(LoadResult.Error::new);
        } else if (type.equals(Constants.KEY_GET_TOP_RATED_MOVIE)) {
            return movieService.getTopRatedMoviePaging(nextPageNumber)
                    .subscribeOn(Schedulers.io())
                    .map(obj -> toLoadResult(nextPageNumber, obj))
                    .onErrorReturn(LoadResult.Error::new);
        } else if(type.equals(Constants.KEY_SEARCH_MOVIE_BY_CATEGORY)){
            return movieService.getMovieByCategory(query, nextPageNumber)
                    .subscribeOn(Schedulers.io())
                    .map(obj -> toLoadResult(nextPageNumber, obj))
                    .onErrorReturn(LoadResult.Error::new);
        } else if(type.equals(Constants.KEY_GET_RECOMMENDATION)){
            return movieService.getRecommendationPaging(query, nextPageNumber)
                    .subscribeOn(Schedulers.io())
                    .map(obj -> toLoadResult(nextPageNumber, obj))
                    .onErrorReturn(LoadResult.Error::new);
        }
        return null;
    }

    private LoadResult<Integer, Movie.Result> toLoadResult(Integer page,
                                                           @NonNull Movie response) {
        return new LoadResult.Page<>(
                response.results,
                null,
                page == response.total_pages ? null : response.page + 1);
    }

}
