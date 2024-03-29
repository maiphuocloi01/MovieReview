package com.cnjava.moviereview.view.fragment.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cnjava.moviereview.databinding.FragmentCategoryBinding;
import com.cnjava.moviereview.model.Genres;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.view.adapter.CategoryAdapter;
import com.cnjava.moviereview.view.fragment.BaseFragment;
import com.cnjava.moviereview.view.fragment.result.ResultFragment;
import com.cnjava.moviereview.viewmodel.CommonViewModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends BaseFragment<FragmentCategoryBinding, CommonViewModel> implements CategoryAdapter.CategoryCallBack {

    public static final String TAG = CategoryFragment.class.getName();

    @Override
    protected Class<CommonViewModel> getClassVM() {
        return CommonViewModel.class;
    }

    @Override
    protected void initViews() {

        binding.ivBack.setOnClickListener(view -> callBack.backToPrev());
        List<Genres> listGenres = addGenres();
        CategoryAdapter adapter = new CategoryAdapter(context, listGenres, this);
        binding.rvResult.setAdapter(adapter);

        /*binding.etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideSoftInput(binding.etSearch);
                    //viewModel.searchKeyword(v.getText().toString().trim());
                    callBack.replaceFragment(KeywordFragment.TAG, v.getText().toString().trim(), true, Constants.ANIM_SLIDE);
                    return true;
                }
                return false;
            }
        });*/

    }

    @Override
    protected FragmentCategoryBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentCategoryBinding.inflate(inflater, container, false);
    }

    @Override
    public void apiSuccess(String key, Object data) {

    }

    @Override
    public void apiError(String key, int code, Object data) {

    }

    @Override
    public void gotoSearchByCategory(String id, String name) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_TYPE_RESULT, Constants.KEY_SEARCH_MOVIE_BY_CATEGORY);
        bundle.putString("category", id);
        bundle.putString("name", name);
        callBack.replaceFragment(ResultFragment.TAG, bundle, true, Constants.ANIM_FADE);
    }

    private List<Genres> addGenres() {
        List<Genres> genres = new ArrayList<>();
        genres.add(new Genres(28, "Action"));
        genres.add(new Genres(12, "Adventure"));
        genres.add(new Genres(16, "Animation"));
        genres.add(new Genres(35, "Comedy"));
        genres.add(new Genres(80, "Crime"));
        genres.add(new Genres(99, "Documentary"));
        genres.add(new Genres(18, "Drama"));
        genres.add(new Genres(10751, "Family"));
        genres.add(new Genres(14, "Fantasy"));
        genres.add(new Genres(36, "History"));
        genres.add(new Genres(27, "Horror"));
        genres.add(new Genres(10402, "Music"));
        genres.add(new Genres(9648, "Mystery"));
        genres.add(new Genres(10749, "Romance"));
        genres.add(new Genres(878, "Science Fiction"));
        genres.add(new Genres(53, "Thriller"));
        genres.add(new Genres(10752, "War"));
        genres.add(new Genres(37, "Western"));

        return genres;
    }
}
