package com.cnjava.moviereview.view.fragment.favorite;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cnjava.moviereview.MyApplication;
import com.cnjava.moviereview.databinding.FragmentFavoriteBinding;
import com.cnjava.moviereview.model.Favorite;
import com.cnjava.moviereview.util.CommonUtils;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.view.adapter.FavoriteAdapter;
import com.cnjava.moviereview.view.fragment.BaseFragment;
import com.cnjava.moviereview.view.fragment.movie.DetailFragment;
import com.cnjava.moviereview.viewmodel.CommonViewModel;

import java.util.List;

public class FavoriteFragment extends BaseFragment<FragmentFavoriteBinding, CommonViewModel> implements FavoriteAdapter.FavoriteCallBack {

    public static final String TAG = FavoriteFragment.class.getName();
    private FavoriteAdapter favoriteAdapter;

    @Override
    public void apiSuccess(String key, Object data) {
        if (key.equals(Constants.KEY_GET_FAVORITE)) {
            List<Favorite> listFavorite = (List<Favorite>) data;
            //MyApplication.getInstance().getStorage().favoriteList = listFavorite;
            initFavoriteView(listFavorite);
        }
    }

    private void initFavoriteView(List<Favorite> listFavorite) {
        favoriteAdapter = new FavoriteAdapter(context, listFavorite, this);
        binding.rvResult.setAdapter(favoriteAdapter);
        binding.progressCircular.setVisibility(View.GONE);
        binding.layoutTitle.setVisibility(View.VISIBLE);
    }

    @Override
    public void apiError(String key, int code, Object data) {
        if (code == 999) {
            if (key.equals(Constants.KEY_GET_FAVORITE)) {
                Log.d(TAG, "apiError: " + data.toString());
                binding.progressCircular.setVisibility(View.GONE);
                binding.layoutTitle.setVisibility(View.VISIBLE);
                Toast.makeText(context, "Unable connect to heroku", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected Class<CommonViewModel> getClassVM() {
        return CommonViewModel.class;
    }

    @Override
    protected void initViews() {

        MyApplication.getInstance().getStorage().fragmentTag = TAG;
        binding.progressCircular.setVisibility(View.VISIBLE);
        binding.layoutTitle.setVisibility(View.GONE);

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.backToPrev();
            }
        });

        if (CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN) != null) {
            Log.d(TAG, "getYourProfile: ");
            viewModel.getMyFavorite(CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
        }
    }

    @Override
    protected FragmentFavoriteBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentFavoriteBinding.inflate(inflater, container, false);
    }

    @Override
    public void gotoMovieDetail(Favorite favorite) {
        callBack.showFragment(DetailFragment.TAG, Integer.parseInt(favorite.movie.id), true, Constants.ANIM_SLIDE);
    }

    @Override
    public void deleteMovieFavorite(Favorite favorite) {
        favoriteAdapter.deleteItem(favorite);
        viewModel.deleteFavorite(favorite.movie.id, CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
    }
}
