package com.app.base.net;

import com.google.gson.reflect.TypeToken;

/**
 * @Description
 * @Author Zhenhui
 * @Time 2019/8/19 16:33
 */
public class DefaultApiCallback<D> implements LibBaseHttpCallback<DefaultApiResponse<D>> {

    private static final String SUCCESS_CODE = "0";

    private TypeToken<DefaultApiResponse<D>> typeToken;

    public DefaultApiCallback() {
        typeToken = new TypeToken<DefaultApiResponse<D>>() {
        };
    }

    @Override
    public TypeToken<DefaultApiResponse<D>> getTypeToken() {
        return typeToken;
    }

    @Override
    public final boolean isSuccessful(DefaultApiResponse<D> result) {
        if (result != null) {
            DefaultApiStatus status = result.getStatus();
            String code = "-1";
            if (status != null) {
                code = status.getCode();
            }
            return SUCCESS_CODE.equalsIgnoreCase(code);
        }
        return false;
    }

    @Override
    public final void onFailed(DefaultApiResponse<D> result) {
        DefaultApiStatus status = result.getStatus();
        String code = "-1";
        String message = "";
        if (status != null) {
            code = status.getCode();
            message = status.getMessage();
        }
        onFailed(code, message);
    }

    @Override
    public void onFailed(String code, String message) {
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onSuccess(DefaultApiResponse<D> result) {

    }

    @Override
    public void onFinish() {
    }
}
