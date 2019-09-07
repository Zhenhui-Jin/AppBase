package com.app.base.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;

import androidx.annotation.ColorRes;
import androidx.annotation.LayoutRes;

import com.app.base.R;
import com.app.base.bus.RxBus;
import com.app.base.bus.event.LanguageChangeEvent;
import com.app.base.fragment.FragmentOnTouchListener;
import com.app.base.manage.PermissionsManage;
import com.app.base.utils.LanguageUtils;
import com.app.base.view.TopBarType;
import com.gyf.immersionbar.ImmersionBar;
import com.tbruyelle.rxpermissions2.RxPermissions;

import me.yokeyword.fragmentation_swipeback.SwipeBackActivity;

import static com.app.base.view.TopBarType.None;
import static com.app.base.view.TopBarType.TitleBar;

/**
 * @Description
 * @Author Zhenhui
 * @Time 2019/8/15 12:04
 */
public abstract class LibBaseActivity extends SwipeBackActivity {
    protected final String TAG = getClass().getSimpleName();

    /**
     * 权限请求
     */
    private RxPermissions rxPermissions;

    protected View mToolbarView;

    /**
     * 保存FragmentOnTouchListener接口
     */
    private FragmentOnTouchListener onTouchListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rxPermissions = PermissionsManage.getRxPermissions(this);
        initContentView();
//        ButterKnife.bind(this);
        setSwipeBackEnable(false);

        if (isImmersionBarEnabled()) {
            initImmersionBar();
        }

        initView(savedInstanceState);
        initData(savedInstanceState);
        setListener();

        RxBus.get().register(LanguageChangeEvent.class, event -> onLanguageChange());
    }

    @Override
    protected void attachBaseContext(Context context) {
        //获取对应的语言上下文
        Context attachBaseContext = LanguageUtils.getAttachBaseContext(context);
        super.attachBaseContext(attachBaseContext);
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
        RxBus.get().post(event);
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

    /**
     * 请求权限
     * 重写 requestPermissionsResult()
     *
     * @param permissions
     */
    @SuppressLint("CheckResult")
    protected void requestPermissions(String... permissions) {
        rxPermissions.requestEach(permissions).subscribe(permission -> {
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
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * 语言切换回调
     */
    protected void onLanguageChange() {
    }

}
