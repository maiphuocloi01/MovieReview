package com.cnjava.moviereview.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.ViewBinding;

import com.cnjava.moviereview.util.CommonUtils;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.view.callback.OnAPICallBack;
import com.cnjava.moviereview.view.callback.OnMainCallBack;
import com.cnjava.moviereview.viewmodel.BaseViewModel;

public abstract class BaseFragment<B extends ViewBinding, V extends BaseViewModel> extends Fragment
        implements OnAPICallBack {
    private static final String TAG = BaseFragment.class.getName();
    protected B binding;
    protected V viewModel;
    protected Context context;
    protected OnMainCallBack callBack;
    protected Object data;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = initViewBinding(inflater, container);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(getClassVM());
        viewModel.setCallBack(this);
        initViews();
    }

    public final void setCallBack(OnMainCallBack callBack) {
        this.callBack = callBack;
    }

    public void setData(Object data) {
        this.data = data;
    }

    protected abstract Class<V> getClassVM();

    protected abstract void initViews();

    protected abstract B initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container);

    @Override
    public void onDestroy() {
        binding = null;
        String token = CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN);
        Log.d(TAG, "onDestroyView: Frg" + token);
        String token2 = CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN);
        Log.d(TAG, "initViews onDestroyView: " + token2);
        Log.d(TAG, "onDestroy: " + binding);
        super.onDestroy();
    }
}