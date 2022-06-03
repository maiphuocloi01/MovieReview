package com.cnjava.moviereview.view.activity;


import static com.cnjava.moviereview.util.IMEUtils.hideSoftInput;
import static com.cnjava.moviereview.util.IMEUtils.isActive;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.cnjava.moviereview.MyApplication;
import com.cnjava.moviereview.R;
import com.cnjava.moviereview.model.Genres;
import com.cnjava.moviereview.util.IMEUtils;
import com.cnjava.moviereview.view.callback.OnMainCallBack;
import com.cnjava.moviereview.view.fragment.BaseFragment;
import com.cnjava.moviereview.view.fragment.LoginFragment;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements OnMainCallBack {

    public static final String TAG = MainActivity.class.getName();
    public static final int ANIM_SLIDE = 0;
    public static final int ANIM_FADE = 1;

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
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        initViews();
        addGenres();

        //List<Genres> genres = MyApplication.getInstance().getStorage().genresList;
        //Genres genres1 = genres.stream().filter(a -> a.getId() == 12).collect(Collectors.toList()).get(0);
    }

    private void initViews() {
        LoginFragment frg = new LoginFragment();
        frg.setCallBack(this);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_main, frg, LoginFragment.class.getName())
                .commit();
    }

    @Override
    public void showFragment(String tag, Object data, boolean isBack, int anim) {
        try {

            if (isActive(this)) {
                hideSoftInput(MainActivity.this);
            }
            Class<?> clazz = Class.forName(tag);
            Constructor<?> cons = clazz.getConstructor();
            BaseFragment<?, ?> frg = (BaseFragment<?, ?>) cons.newInstance();
            frg.setData(data);
            frg.setCallBack(this);
            FragmentTransaction trans = getSupportFragmentManager()
                    .beginTransaction();
            if (isBack) {
                trans.addToBackStack(null);
            }
            if(anim == ANIM_SLIDE) {
                trans.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
            } else if(anim == ANIM_FADE){
                trans.setCustomAnimations(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit);
            }
            trans.add(R.id.layout_main, frg, tag).commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void backToPrev() {
        if (isActive(this)) {
            hideSoftInput(MainActivity.this);
        }
        onBackPressed();
    }

    private void addGenres(){
        MyApplication.getInstance().getStorage().genresList.add(new Genres(28, "Action"));
        MyApplication.getInstance().getStorage().genresList.add(new Genres(12, "Adventure"));
        MyApplication.getInstance().getStorage().genresList.add(new Genres(16, "Animation"));
        MyApplication.getInstance().getStorage().genresList.add(new Genres(35, "Comedy"));
        MyApplication.getInstance().getStorage().genresList.add(new Genres(80, "Crime"));
        MyApplication.getInstance().getStorage().genresList.add(new Genres(99, "Documentary"));
        MyApplication.getInstance().getStorage().genresList.add(new Genres(18, "Drama"));
        MyApplication.getInstance().getStorage().genresList.add(new Genres(10751, "Family"));
        MyApplication.getInstance().getStorage().genresList.add(new Genres(14, "Fantasy"));
        MyApplication.getInstance().getStorage().genresList.add(new Genres(36, "History"));
        MyApplication.getInstance().getStorage().genresList.add(new Genres(27, "Horror"));
        MyApplication.getInstance().getStorage().genresList.add(new Genres(10402, "Music"));
        MyApplication.getInstance().getStorage().genresList.add(new Genres(9648, "Mystery"));
        MyApplication.getInstance().getStorage().genresList.add(new Genres(10749, "Romance"));
        MyApplication.getInstance().getStorage().genresList.add(new Genres(878, "Science Fiction"));
        MyApplication.getInstance().getStorage().genresList.add(new Genres(10770, "TV Movie"));
        MyApplication.getInstance().getStorage().genresList.add(new Genres(53, "Thriller"));
        MyApplication.getInstance().getStorage().genresList.add(new Genres(10752, "War"));
        MyApplication.getInstance().getStorage().genresList.add(new Genres(37, "37"));
    }
}