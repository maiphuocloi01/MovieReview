package com.cnjava.moviereview.view.fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.cnjava.moviereview.databinding.FragmentHomeBinding;
import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.view.adapter.MovieAdapter;
import com.cnjava.moviereview.view.adapter.PopularAdapter;
import com.cnjava.moviereview.viewmodel.CommonViewModel;


public class HomeFragment extends BaseFragment<FragmentHomeBinding, CommonViewModel> {

    public static final String TAG = HomeFragment.class.getName();
    private Movie moviePopular;
    private Movie movieNowPlaying;
    private Movie movieUpcoming;
    private Movie movieTopRated;

    private Handler handler = new Handler(Looper.getMainLooper());

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int currentPosition = binding.vpPopular.getCurrentItem();
            if (currentPosition == moviePopular.results.size() - 1){
                binding.vpPopular.setCurrentItem(0);
            } else {
                binding.vpPopular.setCurrentItem(currentPosition + 1);
            }
        }
    };

    @Override
    protected Class<CommonViewModel> getClassVM() {
        return CommonViewModel.class;
    }

    @Override
    protected void initViews() {

        viewModel.getPopularMovie();
        viewModel.getNowPlayingMovie();
        viewModel.getTopRatedMovie();
        viewModel.getUpcomingMovie();

        binding.vpPopular.setOffscreenPageLimit(3);
        binding.vpPopular.setClipToPadding(false);
        binding.vpPopular.setClipChildren(false);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(30));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });
        binding.vpPopular.setPageTransformer(compositePageTransformer);
        //Log.d(TAG, "initViews: " + moviePopular);
    }

    @Override
    protected FragmentHomeBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentHomeBinding.inflate(inflater, container, false);
    }

    @Override
    public void apiSuccess(String key, Object data) {
        if (key.equals(Constants.KEY_GET_POPULAR_MOVIE)) {
            moviePopular = (Movie) data;
            Log.d(TAG, "apiSuccess: " + moviePopular);
            PopularAdapter popularAdapter = new PopularAdapter(context, moviePopular);
            binding.vpPopular.setAdapter(popularAdapter);
            //binding.indicatorHome.setViewPager(binding.vpPopular);

            binding.vpPopular.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    handler.removeCallbacks(runnable);
                    handler.postDelayed(runnable, 3000);
                }
            });
        } else if (key.equals(Constants.KEY_GET_NOW_PLAYING_MOVIE)) {
            movieNowPlaying = (Movie) data;
            MovieAdapter adapter = new MovieAdapter(context, movieNowPlaying);
            binding.rvPlaying.setAdapter(adapter);

        } else if (key.equals(Constants.KEY_GET_TOP_RATED_MOVIE)) {
            movieTopRated = (Movie) data;
            MovieAdapter adapter = new MovieAdapter(context, movieTopRated);
            binding.rvTopRated.setAdapter(adapter);

        } else if (key.equals(Constants.KEY_GET_UPCOMING_MOVIE)) {
            movieUpcoming = (Movie) data;
            MovieAdapter adapter = new MovieAdapter(context, movieUpcoming);
            binding.rvUpcoming.setAdapter(adapter);

        }
    }

    @Override
    public void apiError(String key, int code, Object data) {
        Log.d(TAG, "apiError: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 3000);
    }
}
