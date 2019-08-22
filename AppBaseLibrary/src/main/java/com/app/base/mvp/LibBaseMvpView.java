package com.app.base.mvp;

/**
 * @param <VM> ViewModel
 */
public interface LibBaseMvpView<VM> extends LibIView {

    /**
     * 数据返回
     *
     * @param model
     */
    void updateData(VM model);

}