package com.cnjava.moviereview.view.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.cnjava.moviereview.MyApplication;
import com.cnjava.moviereview.R;
import com.cnjava.moviereview.databinding.FragmentHomeBinding;
import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.model.User;
import com.cnjava.moviereview.util.CommonUtils;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.util.ViewUtils;
import com.cnjava.moviereview.view.adapter.MovieAdapter;
import com.cnjava.moviereview.view.adapter.PopularAdapter;
import com.cnjava.moviereview.viewmodel.CommonViewModel;


public class HomeFragment extends BaseFragment<FragmentHomeBinding, CommonViewModel> implements PopularAdapter.MovieCallBack {

    public static final String TAG = HomeFragment.class.getName();
    private Movie moviePopular;
    private Movie movieNowPlaying;
    private Movie movieUpcoming;
    private Movie movieTopRated;

    private Handler handler = new Handler(Looper.getMainLooper());

    private Runnable runnable = new Runnable() {
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

    @Override
    protected Class<CommonViewModel> getClassVM() {
        return CommonViewModel.class;
    }

    @Override
    protected void initViews() {

        if (MyApplication.getInstance().getStorage().moviePopular == null) {
            viewModel.getPopularMovie();
            ViewUtils.show(binding.progressCircular);
            ViewUtils.gone(binding.layoutHome);
        } else {
            moviePopular = MyApplication.getInstance().getStorage().moviePopular;
            initPopularView();
        }

        binding.etSearch.setCursorVisible(false);
        binding.etSearch.setShowSoftInputOnFocus(false);
        binding.etSearch.setFocusableInTouchMode(false);
        binding.etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.showFragment(SearchFragment.TAG, null, true, Constants.ANIM_FADE);
                //actionShowFragment(SearchFragment.TAG, null, true, Constants.ANIM_FADE);
            }
        });

        if (MyApplication.getInstance().getStorage().myUser == null) {
            //DialogUtils.showLoadDataDialog(context);
            Log.d(TAG, "myUser null");
            if (CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN) != null) {
                Log.d(TAG, "getYourProfile: ");
                viewModel.getYourProfile(CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
            }
        } else {
            Log.d(TAG, "myUser not null: ");
            if(MyApplication.getInstance().getStorage().myUser.getAvatar() != null){
                Glide.with(context)
                        .load(String.format(MyApplication.getInstance().getStorage().myUser.getAvatar()))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .placeholder(R.drawable.img_default_avt)
                        .into(binding.ivAvt);
            }
        }



        if (MyApplication.getInstance().getStorage().movieNowPlaying == null) {
            viewModel.getNowPlayingMovie();
        } else {
            movieNowPlaying = MyApplication.getInstance().getStorage().movieNowPlaying;
            initNowPlayingView();
        }

        if (MyApplication.getInstance().getStorage().movieTopRated == null) {
            viewModel.getTopRatedMovie();
        } else {
            movieTopRated = MyApplication.getInstance().getStorage().movieTopRated;
            initTopRatedView();
        }

        if (MyApplication.getInstance().getStorage().movieUpcoming == null) {
            viewModel.getUpcomingMovie();
        } else {
            movieUpcoming = MyApplication.getInstance().getStorage().movieUpcoming;
            initUpcomingView();
        }

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
        binding.ivAvt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.ivAvt.startAnimation(AnimationUtils.loadAnimation(context, androidx.appcompat.R.anim.abc_fade_in));
                if(CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN) == null){
                    callBack.replaceFragment(LoginFragment.TAG, null, true, Constants.ANIM_SLIDE);
                } else {
                    callBack.showFragment(ProfileFragment.TAG, null, true, Constants.ANIM_SLIDE);
                }
            }
        });

        binding.tvViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.tvViewAll.startAnimation(AnimationUtils.loadAnimation(context, androidx.appcompat.R.anim.abc_fade_in));
                callBack.showFragment(CategoryFragment.TAG, null, true, Constants.ANIM_SLIDE);
            }
        });

        binding.btCategory1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("category", "28");
                binding.btCategory1.startAnimation(AnimationUtils.loadAnimation(context, androidx.appcompat.R.anim.abc_fade_in));
                callBack.showFragment(SearchResultFragment.TAG, bundle, true, Constants.ANIM_SLIDE);
            }
        });

        binding.btCategory2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("category", "14");
                binding.btCategory2.startAnimation(AnimationUtils.loadAnimation(context, androidx.appcompat.R.anim.abc_fade_in));
                callBack.showFragment(SearchResultFragment.TAG, bundle, true, Constants.ANIM_SLIDE);
            }
        });

        binding.btCategory3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("category", "18");
                binding.btCategory3.startAnimation(AnimationUtils.loadAnimation(context, androidx.appcompat.R.anim.abc_fade_in));
                callBack.showFragment(SearchResultFragment.TAG, bundle, true, Constants.ANIM_SLIDE);
            }
        });

        binding.btCategory4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("category", "12");
                binding.btCategory4.startAnimation(AnimationUtils.loadAnimation(context, androidx.appcompat.R.anim.abc_fade_in));
                callBack.showFragment(SearchResultFragment.TAG, bundle, true, Constants.ANIM_SLIDE);
            }
        });
    }

    @Override
    protected FragmentHomeBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentHomeBinding.inflate(inflater, container, false);
    }

    @Override
    public void apiSuccess(String key, Object data) {

        ViewUtils.gone(binding.progressCircular);
        ViewUtils.show(binding.layoutHome);

        if (key.equals(Constants.KEY_GET_POPULAR_MOVIE)) {
            moviePopular = (Movie) data;
            MyApplication.getInstance().getStorage().moviePopular = moviePopular;
            Log.d(TAG, "apiSuccess: " + moviePopular);
            initPopularView();

        } else if (key.equals(Constants.KEY_GET_NOW_PLAYING_MOVIE)) {
            movieNowPlaying = (Movie) data;
            MyApplication.getInstance().getStorage().movieNowPlaying = movieNowPlaying;
            initNowPlayingView();

        } else if (key.equals(Constants.KEY_GET_TOP_RATED_MOVIE)) {
            movieTopRated = (Movie) data;
            MyApplication.getInstance().getStorage().movieTopRated = movieTopRated;
            initTopRatedView();

        } else if (key.equals(Constants.KEY_GET_UPCOMING_MOVIE)) {
            movieUpcoming = (Movie) data;
            MyApplication.getInstance().getStorage().movieUpcoming = movieUpcoming;
            initUpcomingView();

        } else if (key.equals(Constants.KEY_GET_YOUR_PROFILE)) {
            User user = (User) data;
            MyApplication.getInstance().getStorage().myUser = user;
            if(user.getAvatar() != null){
                Glide.with(context)
                        .load(String.format(user.getAvatar()))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .placeholder(R.drawable.img_default_avt)
                        .into(binding.ivAvt);
            }

        }
    }

    private void initPopularView() {
        PopularAdapter popularAdapter = new PopularAdapter(context, moviePopular, this);
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
    }

    private void initNowPlayingView() {
        MovieAdapter adapter = new MovieAdapter(context, movieNowPlaying, this);
        binding.rvPlaying.setAdapter(adapter);
    }

    private void initTopRatedView() {
        MovieAdapter adapter = new MovieAdapter(context, movieTopRated, this);
        binding.rvTopRated.setAdapter(adapter);
    }

    private void initUpcomingView() {
        MovieAdapter adapter = new MovieAdapter(context, movieUpcoming, this);
        binding.rvUpcoming.setAdapter(adapter);
    }

    @Override
    public void apiError(String key, int code, Object data) {
        if (key.equals(Constants.KEY_GET_POPULAR_MOVIE)) {
            if (code == 999) {
                Log.d(TAG, "apiError: " + data.toString());
                Toast.makeText(context, "Unable connect to server", Toast.LENGTH_SHORT).show();
            }
        }
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
        //actionShowFragment(DetailFragment.TAG, id, true, Constants.ANIM_SLIDE);
    }

    /*private void actionShowFragment(String tag, Object data, boolean isBack, int anim) {
        NavigateFragment parentFrag = ((NavigateFragment) HomeFragment.this.getParentFragment());
        if (parentFrag != null) {
            parentFrag.setActionShowFragment(tag, data, isBack, anim);
        }
    }*/
}
