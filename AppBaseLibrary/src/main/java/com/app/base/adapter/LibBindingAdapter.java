package com.app.base.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public abstract class LibBindingAdapter<T> extends BaseQuickAdapter<T, LibBindingViewHolder> {

    public LibBindingAdapter(int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }

    public LibBindingAdapter(@Nullable List<T> data) {
        super(data);
    }

    public LibBindingAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected View getItemView(int layoutResId, ViewGroup parent) {
        ViewDataBinding binding = DataBindingUtil.inflate(mLayoutInflater, layoutResId, parent, false);
        if (binding == null) {
            return super.getItemView(layoutResId, parent);
        }
        return binding.getRoot();
    }

}