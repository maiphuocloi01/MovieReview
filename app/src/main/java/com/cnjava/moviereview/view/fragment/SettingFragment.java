package com.cnjava.moviereview.view.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cnjava.moviereview.R;
import com.cnjava.moviereview.databinding.FragmentSettingBinding;
import com.cnjava.moviereview.viewmodel.CommonViewModel;

public class SettingFragment extends BaseFragment<FragmentSettingBinding, CommonViewModel> {
    public static final String TAG = SettingFragment.class.getName();


    @Override
    public void apiSuccess(String key, Object data) {

    }

    @Override
    public void apiError(String key, int code, Object data) {

    }

    @Override
    protected Class<CommonViewModel> getClassVM() {
        return CommonViewModel.class;
    }

    @Override
    protected void initViews() {



    }

    @Override
    protected FragmentSettingBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentSettingBinding.inflate(inflater, container, false);
    }
}
