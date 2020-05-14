package com.app.base.net;

import androidx.annotation.NonNull;

/**
 * @Description 统一管理api请求
 * @Author Zhenhui
 * @Time 2019/9/6 11:31
 */
public class DefaultApiManage {
    protected LibBaseOkGo mApiClient;

    public DefaultApiManage(LibBaseOkGo apiClient) {
        this.mApiClient = apiClient;
    }

    protected <T> void execute(RequestInfo requestInfo, LibBaseHttpCallback<T> callback) {
        mApiClient.execute(requestInfo, callback);
    }

    /**
     * 取消请求
     *
     * @param tag
     */
    public void cancelTag(@NonNull Object tag) {
        mApiClient.cancelTag(tag);
    }
}
