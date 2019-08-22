package com.app.base;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDexApplication;

import com.app.base.utils.DataCache;

import me.yokeyword.fragmentation.Fragmentation;
import me.yokeyword.fragmentation.helper.ExceptionHandler;

public abstract class LibBaseApplication extends MultiDexApplication implements ExceptionHandler {

    @Override
    public void onCreate() {
        super.onCreate();

        Fragmentation.builder()
                // 设置 栈视图 模式为 （默认）悬浮球模式   SHAKE: 摇一摇唤出  NONE：隐藏， 仅在Debug环境生效
                .stackViewMode(Fragmentation.BUBBLE)
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
        init();
    }

    protected abstract void init();

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
