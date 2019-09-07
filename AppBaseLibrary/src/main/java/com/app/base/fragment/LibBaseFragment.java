package com.app.base.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.RelativeLayout;

import androidx.annotation.ColorRes;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.base.R;
import com.app.base.bus.RxBus;
import com.app.base.bus.event.LanguageChangeEvent;
import com.app.base.view.TopBarType;
import com.gyf.immersionbar.ImmersionBar;
import com.tbruyelle.rxpermissions2.RxPermissions;

import me.yokeyword.fragmentation_swipeback.SwipeBackFragment;

import static com.app.base.view.TopBarType.None;
import static com.app.base.view.TopBarType.TitleBar;

/**
 * @Description
 * @Author Zhenhui
 * @Time 2019/8/15 9:57
 */
public abstract class LibBaseFragment extends SwipeBackFragment {

    protected final String TAG = getClass().getSimpleName();
    /**
     * 权限请求
     */
    private RxPermissions rxPermissions;

    protected Handler mHandler = new Handler();

    protected LayoutInflater mLayoutInflater;
    private View mContentView;
    protected View mToolbarView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rxPermissions = new RxPermissions(this);
    }

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
        initArguments();
        initView(savedInstanceState);
        initData(savedInstanceState);
        setListener();

        RxBus.get().register(LanguageChangeEvent.class, event -> onLanguageChange());
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
     * 获取数据
     */
    protected void initArguments() {

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

            View baseToolbarHeightView = findViewById(R.id.base_toolbar_height_view);
            bar.statusBarView(baseToolbarHeightView)
                    .statusBarColor(statusBarColor());
            ViewGroup.LayoutParams layoutParams = baseToolbarHeightView.getLayoutParams();
            layoutParams.width = getStatusBarHeight();

            if (mToolbarView != null) {
                bar.titleBar(mToolbarView);
            }

            setToolbarSuspending();

        }
        if (isStatusBarDarkFont()) {
            bar.statusBarDarkFont(true, 0.2f);
        }
        bar.init();
    }

    /**
     * 设置悬浮标题栏
     */
    private void setToolbarSuspending() {
        View baseContentLayout = findViewById(R.id.base_content_layout);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) baseContentLayout.getLayoutParams();
        params.addRule(RelativeLayout.BELOW, isSuspendingToolbar() ? 0 : R.id.base_toolbar_layout);
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    protected int getStatusBarHeight() {
        return ImmersionBar.getStatusBarHeight(this);
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

    /**
     * 是否悬浮标题栏
     */
    protected boolean isSuspendingToolbar() {
        return false;
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

    /**
     * 发送事件
     *
     * @param event
     */
    protected void sendEvent(Object event) {
        RxBus.get().post(event);
    }


    /**
     * 请求权限
     * 重写 requestPermissionsResult()
     *
     * @param permissions
     */
    @SuppressLint("CheckResult")
    protected void requestPermissions(String... permissions) {
        rxPermissions.requestEach(permissions).subscribe(permission -> {
            Log.i(TAG, "requestPermissions: " + permission);
            if (permission.granted) {
                //权限请求成功
            } else if (permission.shouldShowRequestPermissionRationale) {
                //拒绝权限，可再次询问
            } else {
                //拒绝权限，不再询问
            }
            requestPermissionsResult(permission.name, permission.granted, permission.shouldShowRequestPermissionRationale);
        });
    }

    /**
     * 请求权限结果回调，请求权限时需重写逻辑
     *
     * @param permission    权限
     * @param isGranted     是否同意
     * @param isShowRequest 是否可再次询问  false：选择了不再询问
     */
    protected void requestPermissionsResult(String permission, boolean isGranted, boolean isShowRequest) {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * 语言切换回调
     */
    protected void onLanguageChange() {

    }

}
