package com.cnjava.moviereview.view.fragment.home;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.cnjava.moviereview.MyApplication;
import com.cnjava.moviereview.R;
import com.cnjava.moviereview.Storage;
import com.cnjava.moviereview.databinding.FragmentHomeBinding;
import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.util.CommonUtils;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.util.ViewUtils;
import com.cnjava.moviereview.view.adapter.MovieAdapter;
import com.cnjava.moviereview.view.adapter.PopularAdapter;
import com.cnjava.moviereview.view.fragment.BaseFragment;
import com.cnjava.moviereview.view.fragment.CategoryFragment;
import com.cnjava.moviereview.view.fragment.movie.DetailFragment;
import com.cnjava.moviereview.view.fragment.LoginFragment;
import com.cnjava.moviereview.view.fragment.ProfileFragment;
import com.cnjava.moviereview.view.fragment.RegisterFragment;
import com.cnjava.moviereview.view.fragment.SearchFragment;
import com.cnjava.moviereview.view.fragment.SearchResultFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomeViewModel> implements PopularAdapter.MovieCallBack {

    public static final String TAG = HomeFragment.class.getName();

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (moviePopular != null) {
                int currentPosition = binding.vpPopular.getCurrentItem();
                if (currentPosition == moviePopular.results.size() - 1) {
                    binding.vpPopular.setCurrentItem(0);
                } else {
                    binding.vpPopular.setCurrentItem(currentPosition + 1);
                }
            }
        }
    };

    private Movie moviePopular;
    private static final int POPULAR = 3;
    private Movie movieNowPlaying;
    private static final int NOW_PLAYING = 0;
    private Movie movieUpcoming;
    private static final int UP_COMING = 1;
    private Movie movieTopRated;
    private static final int TOP_RATED = 2;

    private Storage storage = MyApplication.getInstance().getStorage();

    @Override
    protected Class<HomeViewModel> getClassVM() {
        return HomeViewModel.class;
    }

    @Override
    protected void initViews() {

        //homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        viewModel.getLiveDataIsLoading().observe(getViewLifecycleOwner(), this::setScreenLoading);

        storage.fragmentTag = TAG;
        if (CommonUtils.getInstance().getPref(Constants.ONBOARD) == null) {
            CommonUtils.getInstance().savePref(Constants.ONBOARD, "1");
        }

        if (storage.myUser == null) {
            if (CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN) != null) {
                getYourProfileAndSaveToStorage();
            }
        } else {
            if (storage.myUser.getAvatar() != null) {
                Glide.with(context)
                        .load(storage.myUser.getAvatar())
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .placeholder(R.drawable.img_default_avt)
                        .into(binding.ivAvt);
            }
        }

        binding.etSearch.setShowSoftInputOnFocus(false);
        binding.etSearch.setOnClickListener(view -> {
            callBack.showFragment(SearchFragment.TAG, null, true, Constants.ANIM_FADE);
        });


        if (storage.moviePopular == null) {
            viewModel.getPopularMovie();
            viewModel.popularMovieLD().observe(this, _popularMovie -> {
                moviePopular = _popularMovie;
                storage.moviePopular = _popularMovie;
                initMovieRecyclerView(_popularMovie, POPULAR);
            });
        } else {
            moviePopular = storage.moviePopular;
            initMovieRecyclerView(moviePopular, POPULAR);
        }

        if (storage.movieNowPlaying == null) {
            viewModel.getNowPlayingMovie();
            viewModel.nowPlayingMovieLD().observe(this, _nowPlayingMovie -> {
                movieNowPlaying = _nowPlayingMovie;
                storage.movieNowPlaying = _nowPlayingMovie;
                initMovieRecyclerView(_nowPlayingMovie, NOW_PLAYING);
            });
        } else {
            movieNowPlaying = storage.movieNowPlaying;
            initMovieRecyclerView(movieNowPlaying, NOW_PLAYING);
        }

        if (storage.movieTopRated == null) {
            viewModel.getTopRatedMovie();
            viewModel.topRatedMovieLD().observe(this, _topRatedMovie -> {
                movieTopRated = _topRatedMovie;
                storage.movieTopRated = _topRatedMovie;
                initMovieRecyclerView(_topRatedMovie, TOP_RATED);
            });
        } else {
            movieTopRated = storage.movieTopRated;
            initMovieRecyclerView(movieTopRated, TOP_RATED);
        }

        if (storage.movieUpcoming == null) {
            viewModel.getUpComingMovie();
            viewModel.upcomingMovieLD().observe(this, _upcomingMovie -> {
                movieUpcoming = _upcomingMovie;
                storage.movieUpcoming = _upcomingMovie;
                initMovieRecyclerView(_upcomingMovie, UP_COMING);
            });
        } else {
            movieUpcoming = storage.movieUpcoming;
            initMovieRecyclerView(movieUpcoming, UP_COMING);
        }

        binding.vpPopular.setOffscreenPageLimit(3);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(30));
        compositePageTransformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f);
        });
        binding.vpPopular.setPageTransformer(compositePageTransformer);

        binding.ivAvt.setOnClickListener(view -> {
            if (CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN) == null) {
                showAlertDialog();
            } else {
                if (storage.myUser != null) {
                    callBack.showFragment(ProfileFragment.TAG, null, true, Constants.ANIM_SLIDE);
                } else {
                    getYourProfileAndSaveToStorage();
                }
            }
        });

        binding.tvViewAll.setOnClickListener(view -> {
            callBack.showFragment(CategoryFragment.TAG, null, true, Constants.ANIM_SLIDE);
        });

        binding.btCategory123.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("category", "28");
            callBack.showFragment(SearchResultFragment.TAG, bundle, true, Constants.ANIM_SLIDE);
        });

        binding.btCategory2.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("category", "14");
            callBack.showFragment(SearchResultFragment.TAG, bundle, true, Constants.ANIM_SLIDE);
        });

        binding.btCategory3.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("category", "18");
            callBack.showFragment(SearchResultFragment.TAG, bundle, true, Constants.ANIM_SLIDE);
        });

        binding.btCategory4.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("category", "12");
            callBack.showFragment(SearchResultFragment.TAG, bundle, true, Constants.ANIM_SLIDE);
        });


    }

    private void setScreenLoading(boolean isLoading) {
        if (isLoading) {
            ViewUtils.show(binding.progressCircular);
            ViewUtils.gone(binding.layoutHome);
        } else {
            ViewUtils.gone(binding.progressCircular);
            ViewUtils.show(binding.layoutHome);
        }
    }

    @Override
    protected FragmentHomeBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentHomeBinding.inflate(inflater, container, false);
    }

    @Override
    public void apiSuccess(String key, Object data) {
    }

    private void getYourProfileAndSaveToStorage() {
        viewModel.getYourProfile(CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
        viewModel.yourProfileLD().observe(this, _user -> {
            storage.myUser = _user;
            if (_user.getAvatar() != null) {
                Glide.with(context)
                        .load(_user.getAvatar())
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .placeholder(R.drawable.img_default_avt)
                        .into(binding.ivAvt);
            }
        });
    }

    private void initMovieRecyclerView(Movie movie, int type) {
        if (type == POPULAR) {
            PopularAdapter popularAdapter = new PopularAdapter(context, movie, this);
            binding.vpPopular.setAdapter(popularAdapter);
            binding.vpPopular.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    handler.removeCallbacks(runnable);
                    handler.postDelayed(runnable, 3000);
                }
            });
        } else if (type == NOW_PLAYING) {
            MovieAdapter adapter = new MovieAdapter(context, movie, this);
            binding.rvPlaying.setAdapter(adapter);
        } else if (type == UP_COMING) {
            MovieAdapter adapter = new MovieAdapter(context, movieUpcoming, this);
            binding.rvUpcoming.setAdapter(adapter);
        } else if (type == TOP_RATED) {
            MovieAdapter adapter = new MovieAdapter(context, movie, this);
            binding.rvTopRated.setAdapter(adapter);
        }
    }

    @Override
    public void apiError(String key, int code, Object data) {
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

    @Override
    public void gotoMovieDetail(int id) {
        callBack.showFragment(DetailFragment.TAG, id, true, Constants.ANIM_SLIDE);
    }


    private void showAlertDialog() {
        Log.d(TAG, "showAlertDialog: ");
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_login_dialog);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        dialog.setCancelable(true);

        TextView btnCancel = dialog.findViewById(R.id.bt_signup);
        Button btnConfirm = dialog.findViewById(R.id.bt_signin);

        btnCancel.setOnClickListener(view -> {
            callBack.showFragment(RegisterFragment.TAG, null, true, Constants.ANIM_SCALE);
            dialog.dismiss();
        });

        btnConfirm.setOnClickListener(view -> {
            callBack.showFragment(LoginFragment.TAG, null, true, Constants.ANIM_SCALE);
            dialog.dismiss();
        });
        dialog.show();

    }
}
