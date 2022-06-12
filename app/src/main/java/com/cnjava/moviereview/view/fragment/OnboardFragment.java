package com.cnjava.moviereview.view.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.cnjava.moviereview.R;
import com.cnjava.moviereview.databinding.FragmentOnboardingBinding;
import com.cnjava.moviereview.model.Genres;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.view.adapter.OnboardAdapter;
import com.cnjava.moviereview.view.adapter.PopularAdapter;
import com.cnjava.moviereview.viewmodel.CommonViewModel;

import java.util.ArrayList;
import java.util.List;

public class OnboardFragment extends BaseFragment<FragmentOnboardingBinding, CommonViewModel>{

    public static final String TAG = OnboardFragment.class.getName();

    @Override
    protected Class<CommonViewModel> getClassVM() {
        return CommonViewModel.class;
    }

    @Override
    protected void initViews() {

        List<Genres> listOnboard = new ArrayList<>();
        listOnboard.add(new Genres(R.raw.mobile, "Lorem ipsum", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."));
        listOnboard.add(new Genres(R.raw.search, "Lorem ipsum", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."));
        listOnboard.add(new Genres(R.raw.review, "Lorem ipsum", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."));

        OnboardAdapter onboardAdapter = new OnboardAdapter(context, listOnboard);
        binding.vpOnboard.setAdapter(onboardAdapter);

        binding.vpOnboard.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                int currentPosition = binding.vpOnboard.getCurrentItem();
                if (currentPosition == listOnboard.size() - 1) {
                    binding.layoutNext.setVisibility(View.GONE);
                    binding.btStart.setVisibility(View.VISIBLE);
                } else {
                    binding.layoutNext.setVisibility(View.VISIBLE);
                    binding.btStart.setVisibility(View.GONE);
                }
            }
        });

        binding.ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getItem(0) < 3)
                    binding.vpOnboard.setCurrentItem(getItem(1),true);
            }
        });

        binding.cirIndicator.setViewPager(binding.vpOnboard);
        onboardAdapter.registerAdapterDataObserver(binding.cirIndicator.getAdapterDataObserver());

        binding.btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.replaceFragment(HomeFragment.TAG, null, false, Constants.ANIM_SLIDE);
            }
        });

        binding.tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.replaceFragment(HomeFragment.TAG, null, false, Constants.ANIM_SLIDE);
            }
        });

    }

    private int getItem(int i){

        return binding.vpOnboard.getCurrentItem() + i;
    }

    @Override
    protected FragmentOnboardingBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentOnboardingBinding.inflate(inflater, container, false);
    }

    @Override
    public void apiSuccess(String key, Object data) {

    }

    @Override
    public void apiError(String key, int code, Object data) {

    }
}
