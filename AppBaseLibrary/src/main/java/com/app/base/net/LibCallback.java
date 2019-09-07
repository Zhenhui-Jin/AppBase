package com.app.base.net;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import okhttp3.ResponseBody;

/**
 * @param <T>
 */
class LibCallback<T> extends AbsCallback<T> {

    private final LibBaseHttpCallback<T> callback;

    protected LibCallback(@NonNull LibBaseHttpCallback<T> callback) {
        this.callback = callback;
    }

    @Override
    public T convertResponse(okhttp3.Response response) throws Throwable {
        ResponseBody body = response.body();
        if (body == null) {
            return null;
        }
        String json = body.string();
        T t = new Gson().fromJson(json, callback.getTypeToken().getType());
        return t;
    }

    @Override
    public void onStart(Request<T, ? extends Request> request) {
        callback.onStart();
    }

    @Override
    public void onSuccess(Response<T> response) {
        T t = response.body();
        if (callback.isSuccessful(t)) {
            callback.onSuccess(t);
        } else {
            callback.onFailed(t);
        }
    }

    @Override
    public void onError(Response<T> response) {
        int code = -1;
        String message = "";
        if (response != null) {
            code = response.code();
            Throwable e = response.getException();
            if (e != null) {
                message = e.getMessage();
            }
        }
        callback.onFailed(code, message);
    }
}