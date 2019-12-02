package com.app.base.mvp;

/**
 * @Description
 * @Author Zhenhui
 * @Time 2019/8/19 14:40
 */
public interface LibIView {
    /**
     * 是否在显示生命周期中
     *
     * @return
     */
    boolean isVisible();

    /**
     * 显示正在加载view
     */
    void showLoading();

    /**
     * 关闭正在加载view
     */
    void hideLoading();

    /**
     * 刷新界面
     */
    void refreshView();
}
