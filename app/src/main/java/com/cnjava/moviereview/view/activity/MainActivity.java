package com.cnjava.moviereview.view.activity;


import static com.cnjava.moviereview.util.IMEUtils.hideSoftInput;
import static com.cnjava.moviereview.util.IMEUtils.isActive;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import com.cnjava.moviereview.MyApplication;
import com.cnjava.moviereview.R;
import com.cnjava.moviereview.Storage;
import com.cnjava.moviereview.databinding.ActivityMainBinding;
import com.cnjava.moviereview.model.Video;
import com.cnjava.moviereview.util.CommonUtils;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.util.ViewUtils;
import com.cnjava.moviereview.view.callback.OnMainCallBack;
import com.cnjava.moviereview.view.fragment.BaseFragment;
import com.cnjava.moviereview.view.fragment.home.HomeFragment;
import com.cnjava.moviereview.view.fragment.onboard.OnboardFragment;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements OnMainCallBack {

    public static final String TAG = MainActivity.class.getName();
    private ActivityMainBinding binding;
    private final Storage storage = MyApplication.getInstance().getStorage();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_MovieReview);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
        storage.WIDTH_SCREEN = displaymetrics.widthPixels / displaymetrics.density;
        storage.HEIGHT_SCREEN = displaymetrics.heightPixels / displaymetrics.density;

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        initViews();
        new CountDownTimer(3100, 2000) {
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
        }.start();
    }

    private void initViews() {
        if (CommonUtils.getInstance().getPref(Constants.ONBOARD) == null) {
            OnboardFragment frg = new OnboardFragment();
            frg.setCallBack(this);
            frg.setData(this);
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(OnboardFragment.TAG)
                    .add(R.id.layout_main, frg, OnboardFragment.class.getName())
                    .commit();
        } else {
            HomeFragment frg = new HomeFragment();
            frg.setCallBack(this);
            frg.setData(this);
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(HomeFragment.TAG)
                    .add(R.id.layout_main, frg, HomeFragment.class.getName())
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
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentByTag(HomeFragment.TAG) instanceof HomeFragment
                && getSupportFragmentManager().getBackStackEntryCount() < 2) {
            System.exit(0);
        }
        getSupportFragmentManager().popBackStack();
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
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        String saveSession = CommonUtils.getInstance().getPref(Constants.SAVE_SESSION);
        Log.d(TAG, "onDestroy: " + saveSession);
        if (saveSession == null) {
            CommonUtils.getInstance().clearPref(Constants.ACCESS_TOKEN);
            CommonUtils.getInstance().clearPref(Constants.SAVE_SESSION);
            storage.myUser = null;
            storage.reviewList = null;
            storage.movieDetail = null;
            String token = CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN);
            Log.d(TAG, "onSaveSession null \n" + token);
        } else {
            String token = CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN);
            Log.d(TAG, "onDestroy: no clear \n" + token);
        }
        super.onDestroy();
    }

}