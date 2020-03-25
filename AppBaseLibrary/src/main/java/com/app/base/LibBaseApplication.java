package com.app.base;

import android.app.Activity;
import android.os.Bundle;

import com.app.base.utils.DataCache;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDexApplication;
import me.yokeyword.fragmentation.Fragmentation;
import me.yokeyword.fragmentation.helper.ExceptionHandler;

public abstract class LibBaseApplication extends MultiDexApplication implements ExceptionHandler {

    private int appCount;
    private boolean isRunInBackground = true;
    private ActivityLifecycleCallbacks mActivityLifecycleCallbacks;

    protected static LibBaseApplication mApplication;

    public static <A extends LibBaseApplication> A getApplication() {
        if (mApplication == null) {
            return null;
        }
        return (A) mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mApplication = this;

        Fragmentation.builder()
                // 设置 栈视图 模式为 （默认）悬浮球模式   SHAKE: 摇一摇唤出  NONE：隐藏， 仅在Debug环境生效
                .stackViewMode(Fragmentation.NONE)
                .debug(BuildConfig.DEBUG)
                /**
                 * 可以获取到{@link me.yokeyword.fragmentation.exception.AfterSaveStateTransactionWarning}
                 * 在遇到After onSaveInstanceState时，不会抛出异常，会回调到下面的ExceptionHandler
                 */
//                .handleException(new ExceptionHandler() {
//                    @Override
//                    public void onException(@NonNull Exception e) {
//                        // 以Bugtags为例子: 把捕获到的 Exception 传到 Bugtags 后台。
//                        // Bugtags.sendException(e);
//                    }
//                })
                .handleException(this)//ExceptionHandler.onException()
                .install();

        DataCache.init(this);

        initBackgroundCallBack();

        init();
    }


    protected abstract void init();

    /**
     * 应用后台切换监听
     *
     * @see #setActivityLifecycleCallbacks
     */
    protected void initBackgroundCallBack() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                if (mActivityLifecycleCallbacks != null) {
                    mActivityLifecycleCallbacks.onActivityCreated(activity, savedInstanceState);
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {
                if (mActivityLifecycleCallbacks != null) {
                    mActivityLifecycleCallbacks.onActivityStarted(activity);
                }
                appCount++;
                if (isRunInBackground) {
                    isRunInBackground = false;
                    //应用从后台回到前台 需要做的操作
                    back2App(activity);
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {
                if (mActivityLifecycleCallbacks != null) {
                    mActivityLifecycleCallbacks.onActivityResumed(activity);
                }
            }

            @Override
            public void onActivityPaused(Activity activity) {
                if (mActivityLifecycleCallbacks != null) {
                    mActivityLifecycleCallbacks.onActivityPaused(activity);
                }
            }

            @Override
            public void onActivityStopped(Activity activity) {
                if (mActivityLifecycleCallbacks != null) {
                    mActivityLifecycleCallbacks.onActivityStopped(activity);
                }
                appCount--;
                if (appCount == 0) {
                    isRunInBackground = true;
                    //应用进入后台 需要做的操作
                    leaveApp(activity);
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                if (mActivityLifecycleCallbacks != null) {
                    mActivityLifecycleCallbacks.onActivitySaveInstanceState(activity, outState);
                }
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                if (mActivityLifecycleCallbacks != null) {
                    mActivityLifecycleCallbacks.onActivityDestroyed(activity);
                }
            }
        });
    }

    /**
     * 设置后台切换监听回调
     *
     * @param mActivityLifecycleCallbacks
     */
    public void setActivityLifecycleCallbacks(ActivityLifecycleCallbacks mActivityLifecycleCallbacks) {
        this.mActivityLifecycleCallbacks = mActivityLifecycleCallbacks;
    }

    /**
     * 从后台回到前台需要执行的逻辑
     *
     * @param activity
     */
    private void back2App(Activity activity) {
    }

    /**
     * 离开应用 压入后台或者退出应用
     *
     * @param activity
     */
    private void leaveApp(Activity activity) {
    }

    /**
     * 可以获取到{@link me.yokeyword.fragmentation.exception.AfterSaveStateTransactionWarning}
     * 在遇到After onSaveInstanceState时，不会抛出异常，会回调到下面的ExceptionHandler
     *
     * @param e
     */
    @Override
    public void onException(@NonNull Exception e) {
        // 以Bugtags为例子: 把捕获到的 Exception 传到 Bugtags 后台。
        // Bugtags.sendException(e);
        e.printStackTrace();
    }
}
