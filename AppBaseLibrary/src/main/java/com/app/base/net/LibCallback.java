package com.app.base.net;

import com.google.gson.Gson;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.IOException;
import java.net.SocketTimeoutException;

import androidx.annotation.NonNull;
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
        String code = "-1";
        String message = "";
        if (response != null) {
            code = String.valueOf(response.code());
            Throwable e = response.getException();
            if (e != null) {
                if (e instanceof SocketTimeoutException
                        || e instanceof ConnectTimeoutException) {
                    code = LibBaseHttpCallback.SOCKET_TIMEOUT_CODE;
                } else if (e instanceof IOException) {
                    code = LibBaseHttpCallback.NETWORK_CONNECTION_ERROR_CODE;
                }
                message = e.getMessage();
            }
        }
        callback.onFailed(code, message);
    }

    @Override
    public void onFinish() {
        callback.onFinish();
    }

}