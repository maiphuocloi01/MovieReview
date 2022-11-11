package com.cnjava.moviereview.view.fragment.cast;

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
import com.cnjava.moviereview.model.Actor;
import com.cnjava.moviereview.model.CastDetail;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.util.NumberUtils;
import com.cnjava.moviereview.util.TimeUtils;
import com.cnjava.moviereview.util.ViewUtils;
import com.cnjava.moviereview.util.cutom.ExpandableTextView;
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
        Actor.Cast cast = (Actor.Cast) mData;

        binding.collapsingToolbarLayout.setTitleEnabled(false);
        ExpandableTextView.onChange(binding.textOverview.textBody, binding.textOverview.buttonReadMore, 3);

        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.backToPrev();
            }
        });

        if (viewModel.castDetailLD().getValue() == null) {
            viewModel.getCastDetail(cast.creditId);
            viewModel.castDetailLD().observe(this, credit -> {
                displayCredit(credit);

            });
        } else {
            displayCredit(viewModel.castDetailLD().getValue());
        }

        if (viewModel.peopleDetailLD().getValue() == null) {
            viewModel.getPeopleDetail(String.valueOf(cast.id));
            viewModel.peopleDetailLD().observe(this, people -> {

                if (people.birthday == null || people.birthday.equals("")) {
                    ViewUtils.gone(binding.textAgeTitle);
                    ViewUtils.gone(binding.textBornTitle);
                    ViewUtils.gone(binding.textAge);
                    ViewUtils.gone(binding.textBorn);
                } else {
                    binding.textAge.setText(String.valueOf(TimeUtils.calculateAge(people.birthday)));
                    binding.textBorn.setText(NumberUtils.convertDateType3(people.birthday));
                }

                if (people.deathday == null || people.deathday.equals("")) {
                    ViewUtils.gone(binding.textDeadTitle);
                    ViewUtils.gone(binding.textDead);
                } else {
                    binding.textDead.setText(NumberUtils.convertDateType3(people.deathday));
                }

                if (people.place_of_birth == null || people.place_of_birth.equals("")) {
                    ViewUtils.gone(binding.textFromTitle);
                    ViewUtils.gone(binding.textFrom);
                } else {
                    binding.textFrom.setText(people.place_of_birth);
                }

                binding.textOverview.textBody.setText(people.biography);
                Log.d(TAG, "initViews: " + ExpandableTextView.isEllipsized(binding.textOverview.textBody));
                /*if (!ExpandableTextView.isEllipsized(binding.textOverview.textBody)){
                    binding.textOverview.buttonReadMore.setVisibility(View.GONE);
                }*/

            });
        } else {
            //displayCredit(viewModel.castDetailLD().getValue());
        }
    }

    private void displayCredit(CastDetail credit) {

        Glide.with(context)
                .load(String.format(Constants.IMAGE_URL + credit.media.backdrop_path))
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.ic_movie2)
                .into(binding.viewDetailHeaderPerson.backdrop);

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
        binding.viewDetailHeaderPerson.textTitle.setText(credit.person.name);
        Glide.with(context)
                .load(Constants.IMAGE_URL + credit.person.profile_path)
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.progress_animation)
                .into(binding.viewDetailHeaderPerson.imagePoster);
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
