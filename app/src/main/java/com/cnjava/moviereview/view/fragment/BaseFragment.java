package com.cnjava.moviereview.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.ViewBinding;

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
        viewModel = new ViewModelProvider(this).get(getClassVM());
        viewModel.setCallBack(this);
        initViews();
        return binding.getRoot();
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
}