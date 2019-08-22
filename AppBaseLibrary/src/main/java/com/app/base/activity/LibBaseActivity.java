package com.app.base.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;

import androidx.annotation.ColorRes;
import androidx.annotation.LayoutRes;

import com.app.base.R;
import com.app.base.fragment.FragmentOnTouchListener;
import com.app.base.view.TopBarType;
import com.gyf.immersionbar.ImmersionBar;

import me.yokeyword.fragmentation_swipeback.SwipeBackActivity;

import static com.app.base.view.TopBarType.None;
import static com.app.base.view.TopBarType.TitleBar;

/**
 * @Description
 * @Author Zhenhui
 * @Time 2019/8/15 12:04
 */
public abstract class LibBaseActivity extends SwipeBackActivity {

    protected View mToolbarView;

    /**
     * 保存FragmentOnTouchListener接口
     */
    private FragmentOnTouchListener onTouchListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView();
//        ButterKnife.bind(this);
        setSwipeBackEnable(false);

        if (isImmersionBarEnabled()) {
            initImmersionBar();
        }

        initView(savedInstanceState);
        initData(savedInstanceState);
        setListener();

    }

    @SuppressLint("RestrictedApi")
    protected void initContentView() {
        super.setContentView(R.layout.base_root_layout);
        ViewStub viewStub = findViewById(R.id.contentVs);
        viewStub.setLayoutResource(getContentLayoutId());
        View contentVs = viewStub.inflate();

        ViewStub toolbarVs = findViewById(R.id.toolbarVs);
        TopBarType topBarType = getTopBarType();
        if (topBarType == TitleBar) {
            toolbarVs.setLayoutResource(getTitleLayoutId());
            mToolbarView = toolbarVs.inflate();
        }
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
        return false;
    }

    /**
     * 是否有标题栏
     */
    protected boolean isHaveToolbar() {
        return false;
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

    protected void sendEvent(Object event) {

    }


    /**
     * 分发触摸事件给所有注册了FragmentOnTouchListener的接口
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (onTouchListener != null) {
            boolean onTouchEvent = onTouchListener.onTouchEvent(event);
            if (onTouchEvent) {
                return true;
            }
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 提供给Fragment通过getActivity()方法来注册自己的触摸事件的方法
     */
    public void registerMyOnTouchListener(FragmentOnTouchListener listener) {
        onTouchListener = listener;
    }

    /**
     * 提供给Fragment通过getActivity()方法来注销自己的触摸事件的方法
     */
    public void unregisterMyOnTouchListener() {
        onTouchListener = null;
    }
}
