package com.app.base.mvp;

import java.lang.ref.WeakReference;

/**
 * Presenter生命周期包装、View的绑定和解除，P层实现的基类
 *
 * @param <VM> ViewModel
 * @param <V>  IView
 */
public abstract class LibBaseMvpPresenter<VM, V extends LibBaseMvpView<VM>> {

    private WeakReference<V> viewRef;

    public LibBaseMvpPresenter(V view) {
        attachView(view);
    }

    final public V getView() {
        if (viewRef != null) {
            return viewRef.get();
        }
        return null;
    }

    private void attachView(V view) {
        viewRef = new WeakReference<>(view);
    }

    public abstract void requestData(Object... objects);

    /**
     * 结束的时候清除，防止内存溢出
     */
    public void detachView() {
        if (viewRef != null) {
            viewRef.clear();
            viewRef = null;
        }
    }

    /**
     * 判断是否在显示生命周期中
     *
     * @return
     */
    final protected boolean isAdded() {
        V view = getView();
        if (view != null) {
            return view.isAdded();
        }
        return false;
    }

    final protected void showLoading() {
        if (isAdded()) {
            getView().showLoading();
        }
    }

    final protected void hideLoading() {
        if (isAdded()) {
            getView().hideLoading();
        }
    }

    final protected void refreshView() {
        if (isAdded()) {
            getView().refreshView();
        }
    }

    final protected void updateData(VM data) {
        if (isAdded()) {
            getView().updateData(data);
        }
    }

}