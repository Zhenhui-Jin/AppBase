package com.app.base.fragment;

import android.content.Context;

import com.app.base.mvp.LibBaseMvpPresenter;
import com.app.base.mvp.LibBaseMvpView;

import androidx.databinding.ViewDataBinding;

/**
 * @param <VM> ViewModel
 * @param <DB> ViewDataBinding
 * @param <P>  MvpPresenter
 * @Description MVP + DataBinding
 * @Author Zhenhui
 * @Time 2019/8/19 11:00
 */
public abstract class LibBaseBindingMvpFragment<VM, DB extends ViewDataBinding, P extends LibBaseMvpPresenter<VM, ? extends LibBaseMvpView>> extends LibBaseBindingFragment<DB> {
    protected P mPresenter;

    protected abstract P createPresenter();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mPresenter = createPresenter();
    }

    @Override
    public void onDestroyView() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDestroyView();
    }

}
