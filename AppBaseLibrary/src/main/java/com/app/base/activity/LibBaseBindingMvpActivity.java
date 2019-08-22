package com.app.base.activity;

import android.os.Bundle;

import androidx.databinding.ViewDataBinding;

import com.app.base.mvp.LibBaseMvpPresenter;
import com.app.base.mvp.LibBaseMvpView;

/**
 * @param <VM> ViewModel
 * @param <DB> ViewDataBinding
 * @param <P>  MvpPresenter
 * @Description MVP+DataBinding
 * @Author Zhenhui
 * @Time 2019/8/19 11:22
 */
public abstract class LibBaseBindingMvpActivity<VM, DB extends ViewDataBinding, P extends LibBaseMvpPresenter<VM, ? extends LibBaseMvpView>> extends LibBaseBindingActivity<DB> {
    protected P mPresenter;

    protected abstract P createPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
    }

    @Override
    protected void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }
}
