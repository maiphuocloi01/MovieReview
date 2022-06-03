package com.cnjava.moviereview.view.fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.cnjava.moviereview.R;
import com.cnjava.moviereview.databinding.FragmentNavigateBinding;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.viewmodel.CommonViewModel;
import com.cnjava.moviereview.viewmodel.NavigateViewModel;

public class NavigateFragment extends BaseFragment<FragmentNavigateBinding, NavigateViewModel>{

    public static final String TAG = NavigateFragment.class.getName();

    @Override
    protected Class<NavigateViewModel> getClassVM() {
        return NavigateViewModel.class;
    }

    @Override
    protected void initViews() {


        if (viewModel.getFragment() == null) {
            loadFragment(new HomeFragment());
        } else {
            loadFragment(viewModel.getFragment());
        }

        binding.bnvHome.setOnItemSelectedListener(item -> {
            Fragment frg;
            int id = item.getItemId();
            switch (id) {
                case R.id.bottom_home:
                    frg = new HomeFragment();
                    loadFragment(frg);
                    viewModel.setFragment(frg);
                    return true;
                case R.id.bottom_search:
                    frg = new SearchFragment();
                    loadFragment(frg);
                    viewModel.setFragment(frg);
                    return true;
                case R.id.bottom_favorite:
                    frg = new FavoriteFragment();
                    loadFragment(frg);
                    viewModel.setFragment(frg);
                    return true;
                case R.id.bottom_profile:
                    frg = new ProfileFragment();
                    loadFragment(frg);
                    viewModel.setFragment(frg);
                    return true;
            }
            return false;
        });
    }

    @Override
    protected FragmentNavigateBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentNavigateBinding.inflate(inflater, container, false);
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    public void setActionShowFragment(String tag, Object data, boolean isBack, int anim) {
        callBack.showFragment(tag, data, isBack, anim);
    }

    @Override
    public void apiSuccess(String key, Object data) {

    }

    @Override
    public void apiError(String key, int code, Object data) {

    }
}
