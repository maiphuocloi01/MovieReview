package com.cnjava.moviereview.view.fragment.notification;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cnjava.moviereview.databinding.FragmentNotificationBinding;
import com.cnjava.moviereview.view.fragment.BaseFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NotificationFragment extends BaseFragment<FragmentNotificationBinding, NotificationViewModel> {

    public static final String TAG = NotificationFragment.class.getName();

    @Override
    public void apiSuccess(String key, Object data) {

    }

    @Override
    public void apiError(String key, int code, Object data) {

    }

    @Override
    protected Class<NotificationViewModel> getClassVM() {
        return NotificationViewModel.class;
    }

    @Override
    protected void initViews() {
        binding.ivBack.setOnClickListener(view -> callBack.backToPrev());
    }

    @Override
    protected FragmentNotificationBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentNotificationBinding.inflate(inflater, container, false);
    }
}
