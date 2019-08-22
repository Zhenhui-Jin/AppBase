package com.app.base.activity;

import android.content.Context;
import android.os.Bundle;

import com.app.base.mvp.LibBaseMvpPresenter;
import com.app.base.mvp.LibBaseMvpView;

/**
 * @param <VM> ViewModel
 * @param <P>  MvpPresenter
 * @Description
 * @Author Zhenhui
 * @Time 2019/8/19 11:22
 */
public abstract class LibBaseMvpActivity<VM, P extends LibBaseMvpPresenter<VM, ? extends LibBaseMvpView>> extends LibBaseActivity {
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
