package com.app.base.net;

import java.io.Serializable;

/**
 * @Description
 * @Author Zhenhui
 * @Time 2019/8/19 16:37
 */
public class DefaultApiResponse<D> implements Serializable {
    private DefaultApiStatus status;
    private D data;

    public DefaultApiStatus getStatus() {
        return status;
    }

    public void setStatus(DefaultApiStatus status) {
        this.status = status;
    }

    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }

}
