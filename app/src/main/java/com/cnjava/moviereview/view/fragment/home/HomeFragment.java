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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.cnjava.moviereview.MyApplication;
import com.cnjava.moviereview.R;
import com.cnjava.moviereview.Storage;
import com.cnjava.moviereview.databinding.FragmentHomeBinding;
import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.model.User;
import com.cnjava.moviereview.util.CommonUtils;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.view.activity.main.MainViewModel;
import com.cnjava.moviereview.view.adapter.MovieAdapter;
import com.cnjava.moviereview.view.adapter.PopularAdapter;
import com.cnjava.moviereview.view.callback.SignInGoogleCallBack;
import com.cnjava.moviereview.view.fragment.BaseFragment;
import com.cnjava.moviereview.view.fragment.category.CategoryFragment;
import com.cnjava.moviereview.view.fragment.detailmovie.DetailMovieFragment;
import com.cnjava.moviereview.view.fragment.login.LoginFragment;
import com.cnjava.moviereview.view.fragment.notification.NotificationFragment;
import com.cnjava.moviereview.view.fragment.profile.ProfileFragment;
import com.cnjava.moviereview.view.fragment.register.RegisterFragment;
import com.cnjava.moviereview.view.fragment.result.ResultFragment;
import com.cnjava.moviereview.view.fragment.search.SearchFragment;
import com.cnjava.moviereview.view.fragment.searchresult.SearchResultFragment;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomeViewModel> implements PopularAdapter.MovieCallBack {

    public static final String TAG = HomeFragment.class.getName();
    private static final int POPULAR = 3;
    private static final int NOW_PLAYING = 0;
    private static final int UP_COMING = 1;
    private static final int TOP_RATED = 2;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable;
    private final Storage storage = MyApplication.getInstance().getStorage();
    private MainViewModel mainViewModel;

    @Override
    protected Class<HomeViewModel> getClassVM() {
        return HomeViewModel.class;
    }

    @Override
    protected void initViews() {

        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.height = (int) storage.HEIGHT_SCREEN;
        binding.vpPopular.setLayoutParams(params);
        if (CommonUtils.getInstance().getPref(Constants.ONBOARD) == null) {
            CommonUtils.getInstance().savePref(Constants.ONBOARD, "1");
        }

        if (CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN) != null) {
            if (mainViewModel.yourProfileLD().getValue() != null) {
                User user = mainViewModel.yourProfileLD().getValue();
                getYourProfileAndSaveToStorage(user);
            } else {
                mainViewModel.getYourProfile(CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
                mainViewModel.yourProfileLD().observe(this, this::getYourProfileAndSaveToStorage);
            }
        }

        if (mainViewModel.popularMovieLD().getValue() != null) {
            initMovieRecyclerView(mainViewModel.popularMovieLD().getValue(), POPULAR);
        } else {
            mainViewModel.getPopularMovie();
            mainViewModel.popularMovieLD().observe(this, _popularMovie -> {
                initMovieRecyclerView(_popularMovie, POPULAR);
            });
        }

        if (mainViewModel.upcomingMovieLD().getValue() != null) {
            initMovieRecyclerView(mainViewModel.upcomingMovieLD().getValue(), UP_COMING);
        } else {
            mainViewModel.getUpComingMovie();
            mainViewModel.upcomingMovieLD().observe(this, _upcomingMovie -> {
                initMovieRecyclerView(_upcomingMovie, UP_COMING);
            });
        }

        if (mainViewModel.topRatedMovieLD().getValue() != null) {
            initMovieRecyclerView(mainViewModel.topRatedMovieLD().getValue(), TOP_RATED);
        } else {
            mainViewModel.getTopRatedMovie();
            mainViewModel.topRatedMovieLD().observe(this, _topRatedMovie -> {
                initMovieRecyclerView(_topRatedMovie, TOP_RATED);
            });
        }

        if (mainViewModel.nowPlayingMovieLD().getValue() != null) {
            initMovieRecyclerView(mainViewModel.nowPlayingMovieLD().getValue(), NOW_PLAYING);
        } else {
            mainViewModel.getNowPlayingMovie();
            mainViewModel.nowPlayingMovieLD().observe(this, _nowPlayingMovie -> {
                initMovieRecyclerView(_nowPlayingMovie, NOW_PLAYING);
            });
        }

        binding.textUpcoming.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.KEY_TYPE_RESULT, Constants.KEY_GET_UPCOMING_MOVIE);
            callBack.replaceFragment(ResultFragment.TAG, bundle, true, Constants.ANIM_FADE);
        });

        binding.textNowPlaying.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.KEY_TYPE_RESULT, Constants.KEY_GET_NOW_PLAYING_MOVIE);
            callBack.replaceFragment(ResultFragment.TAG, bundle, true, Constants.ANIM_FADE);
        });

        binding.textTopRated.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.KEY_TYPE_RESULT, Constants.KEY_GET_TOP_RATED_MOVIE);
            callBack.replaceFragment(ResultFragment.TAG, bundle, true, Constants.ANIM_FADE);
        });

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(0));
        compositePageTransformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f);
        });
        binding.appBarLayout.setOutlineProvider(null);
        binding.vpPopular.setPageTransformer(compositePageTransformer);

        binding.ivAvt.setOnClickListener(view -> {
            if (CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN) == null
                    || mainViewModel.yourProfileLD().getValue() == null) {
                showAlertDialog();
            } else {
                callBack.replaceFragment(ProfileFragment.TAG, null, true, Constants.ANIM_SLIDE);
            }
        });

        binding.textTrending.setOnClickListener(view -> callBack.replaceFragment(CategoryFragment.TAG, null, true, Constants.ANIM_SLIDE));

        binding.btCategory123.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.KEY_TYPE_RESULT, Constants.KEY_SEARCH_MOVIE_BY_CATEGORY);
            bundle.putString("category", "28");
            bundle.putString("name", "Action");
            callBack.replaceFragment(ResultFragment.TAG, bundle, true, Constants.ANIM_FADE);
        });

        binding.btCategory2.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.KEY_TYPE_RESULT, Constants.KEY_SEARCH_MOVIE_BY_CATEGORY);
            bundle.putString("category", "14");
            bundle.putString("name", "Fantasy");
            callBack.replaceFragment(ResultFragment.TAG, bundle, true, Constants.ANIM_FADE);
        });

        binding.btCategory3.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.KEY_TYPE_RESULT, Constants.KEY_SEARCH_MOVIE_BY_CATEGORY);
            bundle.putString("category", "18");
            bundle.putString("name", "Drama");
            callBack.replaceFragment(ResultFragment.TAG, bundle, true, Constants.ANIM_FADE);
        });

        binding.btCategory4.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.KEY_TYPE_RESULT, Constants.KEY_SEARCH_MOVIE_BY_CATEGORY);
            bundle.putString("category", "12");
            bundle.putString("name", "Adventure");
            callBack.replaceFragment(ResultFragment.TAG, bundle, true, Constants.ANIM_FADE);
        });

        binding.ivNotification.setOnClickListener(view -> callBack.replaceFragment(NotificationFragment.TAG, null, true, Constants.ANIM_FADE));
        binding.ivSearch.setOnClickListener(view -> callBack.replaceFragment(SearchFragment.TAG, null, true, Constants.ANIM_FADE));

    }

    /*private void setScreenLoading(boolean isLoading) {
        if (isLoading) {
            ViewUtils.show(binding.progressCircular);
            ViewUtils.gone(binding.layoutHomeScreen);
        } else {
            ViewUtils.gone(binding.progressCircular);
            ViewUtils.show(binding.layoutHomeScreen);
        }
    }*/

    @Override
    protected FragmentHomeBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentHomeBinding.inflate(inflater, container, false);
    }

    @Override
    public void apiSuccess(String key, Object data) {
    }

    private void getYourProfileAndSaveToStorage(User user) {
        if (CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN) != null && user != null) {
            if (user.getAvatar() != null) {
                Picasso.get().load(user.getAvatar())
                        .resize(256, 256)
                        .placeholder(R.drawable.progress_animation)
                        .error(R.drawable.ic_profile2)
                        .centerCrop().into(binding.ivAvt);
            }
        }
    }

    private void initMovieRecyclerView(Movie movie, int type) {
        if (type == POPULAR) {
            runnable = () -> {
                if (movie != null) {
                    int currentPosition = binding.vpPopular.getCurrentItem();
                    if (currentPosition == movie.results.size() - 1) {
                        binding.vpPopular.setCurrentItem(0);
                    } else {
                        binding.vpPopular.setCurrentItem(currentPosition + 1);
                    }
                }
            };
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
            MovieAdapter adapter = new MovieAdapter(context, movie, this);
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
        Log.d(TAG, "onPause: ");
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        handler.postDelayed(runnable, 3000);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    public void gotoMovieDetail(int id) {
        callBack.replaceFragment(DetailMovieFragment.TAG, id, true, Constants.ANIM_SLIDE);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: " + outState);
        super.onSaveInstanceState(outState);
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
            callBack.replaceFragment(RegisterFragment.TAG, null, true, Constants.ANIM_SCALE);
            dialog.dismiss();
        });

        btnConfirm.setOnClickListener(view -> {
            callBack.replaceFragment(LoginFragment.TAG, null, true, Constants.ANIM_SCALE);
            dialog.dismiss();
        });
        dialog.show();

    }


    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView: ");
        super.onDestroyView();
    }

}
