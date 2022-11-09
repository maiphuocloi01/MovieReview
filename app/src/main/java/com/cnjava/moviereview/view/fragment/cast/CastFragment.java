package com.cnjava.moviereview.view.fragment.cast;

import static com.cnjava.moviereview.util.NumberUtils.convertDateType3;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.cnjava.moviereview.R;
import com.cnjava.moviereview.databinding.FragmentCastBinding;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.util.TimeUtils;
import com.cnjava.moviereview.view.fragment.BaseFragment;
import com.google.android.material.appbar.AppBarLayout;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CastFragment extends BaseFragment<FragmentCastBinding, CastViewModel> {

    public static final String TAG = CastFragment.class.getName();
    private Object mData;

    @Override
    public void apiSuccess(String key, Object data) {

    }

    @Override
    public void apiError(String key, int code, Object data) {

    }

    @Override
    protected Class<CastViewModel> getClassVM() {
        return CastViewModel.class;
    }

    @Override
    protected void initViews() {

        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbar);

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        String creditId = (String) mData;

        binding.collapsingToolbarLayout.setTitleEnabled(false);

        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.backToPrev();
            }
        });

        viewModel.getCastDetail(creditId);
        viewModel.castDetailLD().observe(this, credit -> {
            binding.appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                boolean isShow = true;
                int scrollRange = -1;

                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    if (scrollRange == -1) {
                        scrollRange = appBarLayout.getTotalScrollRange();
                    }
                    if (scrollRange + verticalOffset == 0) {
                        binding.toolbar.setTitle(credit.person.name);
                        isShow = true;
                    } else if (isShow) {
                        binding.toolbar.setTitle(" ");//careful there should a space between double quote otherwise it wont work
                        isShow = false;
                    }
                }
            });
            binding.viewDetailHeaderPerson.textSubtitle.setText(credit.department);
            binding.viewDetailHeaderPerson.textCredits.setText(credit.media.character);
            //binding.toolbar.setTitle(credit.person.name);
            //Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(credit.person.name);
            binding.viewDetailHeaderPerson.textTitle.setText(credit.person.name);
            Glide.with(context)
                    .load(Constants.IMAGE_URL + credit.person.profile_path)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .placeholder(R.drawable.progress_animation)
                    .into(binding.viewDetailHeaderPerson.imagePoster);
        });
    }

    @Override
    protected FragmentCastBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentCastBinding.inflate(inflater, container, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            callBack.backToPrev();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setData(Object data) {
        this.mData = data;
    }
}
