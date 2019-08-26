package com.app.base.bus;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * @Description
 * @Author Zhenhui
 * @Time 2019/8/23 17:14
 */
public class RxBus {
    private static final RxBus mInstance = new RxBus();
    private final Subject<Object> mBus = PublishSubject.create().toSerialized();

    private RxBus() {
    }

    /**
     * 单例模式RxBus
     *
     * @return
     */
    public static RxBus get() {
        return mInstance;
    }


    /**
     * 发送消息
     *
     * @param event
     */
    public void post(Object event) {
        mBus.onNext(event);
    }

    private <T> Observable<T> toObservable(Class<T> clazz) {
        return mBus.ofType(clazz).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 注册
     *
     * @param <T>
     * @param onNext
     */
    public <T> RxBus register(Class<T> clazz, Consumer<? super T> onNext) {
        Disposable subscribe = toObservable(clazz).subscribe(onNext);
        return get();
    }

}
