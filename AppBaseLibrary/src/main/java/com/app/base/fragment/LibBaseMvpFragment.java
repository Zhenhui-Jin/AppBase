package com.app.base.fragment;

import android.content.Context;

import com.app.base.mvp.LibBaseMvpPresenter;
import com.app.base.mvp.LibBaseMvpView;

/**
 * @param <VM> ViewModel
 * @param <P>  MvpPresenter
 * @Description mvp基础类
 * @Author Zhenhui
 * @Time 2019/8/19 11:00
 */
public abstract class LibBaseMvpFragment<VM, P extends LibBaseMvpPresenter<VM, ? extends LibBaseMvpView>> extends LibBaseFragment {
    protected P mPresenter;

    protected abstract P createPresenter();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mPresenter = createPresenter();
    }

    @Override
    public void onDestroyView() {
        mPresenter.detachView();
        super.onDestroyView();
    }

}
