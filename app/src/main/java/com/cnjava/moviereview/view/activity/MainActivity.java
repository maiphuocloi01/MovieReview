package com.cnjava.moviereview.view.activity;


import static com.cnjava.moviereview.util.IMEUtils.hideSoftInput;
import static com.cnjava.moviereview.util.IMEUtils.isActive;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.cnjava.moviereview.MyApplication;
import com.cnjava.moviereview.R;
import com.cnjava.moviereview.Storage;
import com.cnjava.moviereview.data.receiver.CheckConnectionBroadcastReceiver;
import com.cnjava.moviereview.databinding.ActivityMainBinding;
import com.cnjava.moviereview.model.Video;
import com.cnjava.moviereview.util.CommonUtils;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.util.ViewUtils;
import com.cnjava.moviereview.view.activity.main.MainViewModel;
import com.cnjava.moviereview.view.callback.OnMainCallBack;
import com.cnjava.moviereview.view.fragment.BaseFragment;
import com.cnjava.moviereview.view.fragment.home.HomeFragment;
import com.cnjava.moviereview.view.fragment.onboard.OnboardFragment;
import com.google.android.material.snackbar.Snackbar;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements OnMainCallBack, CheckConnectionBroadcastReceiver.CheckConnectionListener {

    public static final String TAG = MainActivity.class.getName();
    private ActivityMainBinding binding;
    private final Storage storage = MyApplication.getInstance().getStorage();
    private Snackbar mSnackBar;
    boolean doubleBackToExitPressedOnce = false;
    private String sessionKey;

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_MovieReview);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
        storage.WIDTH_SCREEN = displaymetrics.widthPixels / displaymetrics.density;
        storage.HEIGHT_SCREEN = displaymetrics.heightPixels / displaymetrics.density;

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        initViews();

        viewModel.getLiveDataIsLoading().observe(this, loading -> {
            loadingDataFromAPI(loading);
        });
        if(CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN) != null) {
            viewModel.getYourProfile(CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
            viewModel.yourProfileLD().observe(this, me -> {
                if(me == null){
                    Log.d(TAG, "user null: ");
                }
            });
        }
        /*new CountDownTimer(3100, 2000) {
            public void onFinish() {
                if (!CommonUtils.isInternetOn(MainActivity.this)) {
                    System.exit(0);
                } else {
                    binding.layoutActivity.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.dark));
                    ViewUtils.gone(binding.animationView);
                    ViewUtils.show(binding.layoutMain);
                }
            }

            public void onTick(long millisUntilFinished) {
                if (!CommonUtils.isInternetOn(MainActivity.this)) {
                    Toast.makeText(MainActivity.this, "No connection", Toast.LENGTH_SHORT).show();
                }
            }
        }.start();*/
    }

    private void loadingDataFromAPI(Boolean loading) {
        if (!CommonUtils.isInternetOn(MainActivity.this)) {
            System.exit(0);
        } else {
            if(!loading) {
                binding.layoutActivity.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.dark));
                ViewUtils.gone(binding.animationView);
                ViewUtils.show(binding.layoutMain);
            }
        }
    }

    private void initViews() {
        if (CommonUtils.getInstance().getPref(Constants.ONBOARD) == null) {
            OnboardFragment frg = new OnboardFragment();
            frg.setCallBack(this);
            frg.setData(this);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.layout_main, frg, OnboardFragment.class.getName())
                    .commit();
        } else {
            HomeFragment frg = new HomeFragment();
            frg.setCallBack(this);
            frg.setData(this);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.layout_main, frg, HomeFragment.class.getName())
                    .commit();
        }
    }

    @Override
    public void addFragment(String tag, Object data, boolean isBack, int anim) {
        Fragment frg = showFragment(tag, data);
        if (frg != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(tag)
                    .setCustomAnimations(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit)
                    .add(R.id.layout_main, frg, tag)
                    .commit();
        }
    }

    @Override
    public void replaceFragment(String tag, Object data, boolean isBack, int anim) {
        Fragment frg = showFragment(tag, data);
        if (frg != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(tag)
                    .setCustomAnimations(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit)
                    .replace(R.id.layout_main, frg, tag)
                    .commit();
        }
    }

    private Fragment showFragment(String tag, Object data) {
        try {
            if (isActive(this)) {
                hideSoftInput(this);
            }
            Class<?> clazz = Class.forName(tag);
            Constructor<?> cons = clazz.getConstructor();
            BaseFragment<?, ?> frg = (BaseFragment<?, ?>) cons.newInstance();
            frg.setData(data);
            frg.setCallBack(this);
            return frg;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void backToPrev() {
        if (isActive(this)) {
            hideSoftInput(MainActivity.this);
        }
        super.onBackPressed();
        //getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentByTag(HomeFragment.TAG) instanceof HomeFragment
                && getSupportFragmentManager().getBackStackEntryCount() < 1) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void reloadFragment(Fragment currentFragment) {
        if (currentFragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .detach(currentFragment)
                    .commit();
            getSupportFragmentManager()
                    .beginTransaction()
                    .attach(currentFragment)
                    .commit();
        }
    }

    @Override
    public void reloadFragmentByTag(String tag) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (currentFragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .detach(currentFragment)
                    .commit();
            getSupportFragmentManager()
                    .beginTransaction()
                    .attach(currentFragment)
                    .commit();
        }
    }

    @Override
    public void clearBackStack() {
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    @Override
    public void reloadAllBackStack() {
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            String fragmentTag = fm.getBackStackEntryAt(i).getName();
            refreshFragment(fm.findFragmentByTag(fragmentTag));
        }
    }

    @Override
    public void gotoActivity(ArrayList<String> ids, Video video) {
        Intent intent = new Intent(MainActivity.this, VideoActivity.class);
        intent.putStringArrayListExtra("ids", ids);
        intent.putExtra("video", video);
        startActivity(intent);
    }

    public void refreshFragment(Fragment currentFragment) {
        if (currentFragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .detach(currentFragment)
                    .commit();
            getSupportFragmentManager()
                    .beginTransaction()
                    .attach(currentFragment)
                    .commit();
        }
    }

    @Override
    public Fragment getBackStack() {
        if (getSupportFragmentManager().getBackStackEntryCount() < 2) {
            return null;
        }
        String fragmentTag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 2).getName();
        return getSupportFragmentManager().findFragmentByTag(fragmentTag);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sessionKey != null) {
            CommonUtils.getInstance().savePref(Constants.ACCESS_TOKEN, sessionKey);
            sessionKey = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        String saveSession = CommonUtils.getInstance().getPref(Constants.SAVE_SESSION);
        if (saveSession == null) {
            sessionKey = CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN);
            CommonUtils.getInstance().savePref(Constants.ACCESS_TOKEN, null);
            CommonUtils.getInstance().savePref(Constants.SAVE_SESSION, null);
            storage.myUser = null;
            storage.reviewList = null;
            storage.movieDetail = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void showMessage(Boolean isConnected) {

        if (!isConnected) {
            String messageToUser = "No Internet Connection";

            //Assume "rootLayout" as the root layout of every activity.
            mSnackBar = Snackbar
                    .make(findViewById(R.id.layout_activity), messageToUser, Snackbar.LENGTH_LONG)
                    .setAction("Dismiss", view -> mSnackBar.dismiss());

            mSnackBar.setDuration(Snackbar.LENGTH_INDEFINITE);
            // Changing message text color
            mSnackBar.setActionTextColor(Color.RED);
            mSnackBar.show();
        } else {
            String messageToUser = "You are online now.";
            mSnackBar = Snackbar
                    .make(findViewById(R.id.layout_activity), messageToUser, Snackbar.LENGTH_SHORT);
            mSnackBar.setDuration(Snackbar.LENGTH_SHORT);
            mSnackBar.setActionTextColor(Color.WHITE);
            mSnackBar.show();
            //mSnackBar.dismiss();
        }
    }

    @Override
    public void onNetworkConnectionChanged(Boolean isConnected) {
        showMessage(isConnected);
    }
}