package com.app.base.net;

import androidx.annotation.NonNull;

import com.google.gson.reflect.TypeToken;

/**
 * @Description
 * @Author Zhenhui
 * @Time 2019/8/15 10:32
 */
public interface LibBaseHttpCallback<T> {
    String SOCKET_TIMEOUT_CODE = "timeout";

    @NonNull
    TypeToken<T> getTypeToken();

    /**
     * 判断成功的规则
     *
     * @param result
     * @return
     */
    boolean isSuccessful(T result);

    /**
     * 开始
     */
    void onStart();

    /**
     * 成功
     *
     * @param result
     * @return
     */
    void onSuccess(T result);

    /**
     * api返回失败数据
     *
     * @param result
     * @return
     */
    void onFailed(T result);

    /**
     * api返回失败数据
     *
     * @param code
     * @param message
     * @return
     */
    void onFailed(String code, String message);

    void onFinish();
}