package com.app.base.net;

import java.io.Serializable;

public class DefaultApiStatus implements Serializable {
    //        "status":{"code":"","message":""}
    private String code;
    private String message;

    public DefaultApiStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}