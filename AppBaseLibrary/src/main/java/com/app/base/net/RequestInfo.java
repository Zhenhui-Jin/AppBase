package com.app.base.net;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

/**
 * @Description 请求参数信息
 * @Author Zhenhui
 * @Time 2019/8/17 15:22
 */
public class RequestInfo {

    private transient Object tag;
    private String url;
    private RequestMethod method;
    private Map<String, String> headers = new HashMap();  //添加的header
    private Map<String, Object> params = new HashMap<>();     //添加的param
    private Map<String, List<File>> fileParams = new HashMap<>();     //添加的param
    private String bodyJson;

    private RequestInfo(Object tag, RequestMethod method, String url) {
        this.tag = tag;
        this.method = method;
        this.url = url;
    }

    /**
     * HTTP GET 请求
     *
     * @param tag
     * @param url
     * @return
     */
    public static RequestInfo get(Object tag, String url) {
        return new RequestInfo(tag, RequestMethod.GET, url);
    }

    /**
     * HTTP POST 请求
     *
     * @param tag
     * @param url
     * @return
     */
    public static RequestInfo post(Object tag, String url) {
        return new RequestInfo(tag, RequestMethod.POST, url);
    }

    /**
     * HTTP PUT 请求
     *
     * @param tag
     * @param url
     * @return
     */
    public static RequestInfo put(Object tag, String url) {
        return new RequestInfo(tag, RequestMethod.PUT, url);
    }

    public Object getTag() {
        return tag;
    }

    public String getUrl() {
        return url;
    }

    public RequestMethod getMethod() {
        return method;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    @NonNull
    public Map<String, List<File>> getFileParams() {
        if (fileParams == null) {
            return new HashMap<>();
        }
        return fileParams;
    }

    public RequestInfo setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public RequestInfo addParams(String key, String value) {
        params.put(key, value);
        return this;
    }

    public RequestInfo addParams(String key, Object value) {
        params.put(key, value);
        return this;
    }

    public RequestInfo addFileParams(String key, List<File> value) {
        List<File> files = new ArrayList<>();
        if (value != null) {
            files.addAll(value);
        }
        fileParams.put(key, files);
        return this;
    }

    public RequestInfo addHeaders(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public RequestInfo setBodyJson(String bodyJson) {
        this.bodyJson = bodyJson;
        return this;
    }

    public String getBodyJson() {
        return bodyJson;
    }
}
