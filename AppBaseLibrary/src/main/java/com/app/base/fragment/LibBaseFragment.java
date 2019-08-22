package com.app.base.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import androidx.annotation.ColorRes;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.base.R;
import com.app.base.view.TopBarType;
import com.gyf.immersionbar.ImmersionBar;

import me.yokeyword.fragmentation_swipeback.SwipeBackFragment;

import static com.app.base.view.TopBarType.None;
import static com.app.base.view.TopBarType.TitleBar;

/**
 * @Description
 * @Author Zhenhui
 * @Time 2019/8/15 9:57
 */
public abstract class LibBaseFragment extends SwipeBackFragment {

    protected Handler mHandler = new Handler();

    protected LayoutInflater mLayoutInflater;
    private View mContentView;
    protected View mToolbarView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mLayoutInflater = inflater;
        // 避免多次从xml中加载布局文件
        if (mContentView == null) {
            mContentView = initContentView();
        } else {
            ViewGroup parent = (ViewGroup) mContentView.getParent();
            if (parent != null) {
                parent.removeView(mContentView);
            }
        }

        if (isImmersionBarEnabled()) {
            initImmersionBar();
        }

        if (isSwipeBack()) {
            setSwipeBackEnable(true);
        } else {
            setSwipeBackEnable(false);
        }
        return attachToSwipeBack(mContentView);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(savedInstanceState);
        initData(savedInstanceState);
        setListener();
    }

    @SuppressLint("RestrictedApi")
    protected View initContentView() {
        int contentLayoutId = getContentLayoutId();
        View view = mLayoutInflater.inflate(R.layout.base_root_layout, null);
        ViewStub viewStub = view.findViewById(R.id.contentVs);
        viewStub.setLayoutResource(contentLayoutId);
        viewStub.inflate();
        ViewStub toolbarVs = view.findViewById(R.id.toolbarVs);
        TopBarType topBarType = getTopBarType();
        if (topBarType == TitleBar) {
            toolbarVs.setLayoutResource(getTitleLayoutId());
            mToolbarView = toolbarVs.inflate();
        }
        return view;
    }

    protected final <T extends View> T findViewById(@IdRes int id) {
        T view = mContentView.findViewById(id);
        return view;
    }

    /**
     * 内容布局
     *
     * @return
     */
    @LayoutRes
    protected abstract int getContentLayoutId();

    /**
     * title类型
     *
     * @return
     */
    protected TopBarType getTopBarType() {
        return None;
    }

    /**
     * title布局
     *
     * @return
     */
    @LayoutRes
    protected int getTitleLayoutId() {
        return 0;
    }

    /**
     * 初始化view
     *
     * @param savedInstanceState
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    protected abstract void initData(Bundle savedInstanceState);

    /**
     * 设置监听
     */
    protected abstract void setListener();

    /**
     * 是否支持側滑返回
     *
     * @return
     */
    protected boolean isSwipeBack() {
        return false;
    }

    /**
     * 沉浸式状态栏设置
     */
    protected void initImmersionBar() {
        ImmersionBar bar = ImmersionBar.with(this);
        if (isHaveToolbar()) {
            bar.fitsSystemWindows(true)
                    .statusBarColor(statusBarColor());
            if (mToolbarView != null) {
                bar.titleBar(mToolbarView);
            }
        }
        if (isStatusBarDarkFont()) {
            bar.statusBarDarkFont(true, 0.2f);
        }
        bar.init();
    }

    /**
     * 是否使用沉浸式状态栏
     *
     * @return
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    /**
     * 是否有标题栏
     */
    protected boolean isHaveToolbar() {
        return true;
    }

    /**
     * 状态栏是否是黑色字体
     */
    protected boolean isStatusBarDarkFont() {
        return true;
    }

    /**
     * 状态栏背景色
     */
    @ColorRes
    protected int statusBarColor() {
        return android.R.color.transparent;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (isImmersionBarEnabled()) {
            initImmersionBar();
        }
    }

    public Handler getHandler() {
        return mHandler;
    }

    protected void postDelayed(Runnable runnable, long delayMillis) {
        if (getHandler() != null) {
            getHandler().postDelayed(runnable, delayMillis);
        }
    }

    protected void sendEvent(Object event) {

    }
}
