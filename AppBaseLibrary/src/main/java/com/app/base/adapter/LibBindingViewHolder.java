package com.app.base.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public class LibBindingViewHolder extends BaseViewHolder {

    public LibBindingViewHolder(View view) {
        super(view);
    }

    public ViewDataBinding getBinding() {
        ViewDataBinding binding = DataBindingUtil.getBinding(itemView);
        if (binding == null) {
            binding = DataBindingUtil.getBinding(getConvertView());
        }
        return binding;
    }
}