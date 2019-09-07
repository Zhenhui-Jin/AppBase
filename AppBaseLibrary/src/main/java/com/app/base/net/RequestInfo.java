package com.app.base.net;

import java.util.HashMap;
import java.util.Map;

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
    private Map<String, String> params = new HashMap<>();     //添加的param

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

    public Map<String, String> getParams() {
        return params;
    }

    public RequestInfo setParams(Map<String, String> params) {
        this.params = params;
        return this;
    }

    public RequestInfo setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public RequestInfo addParams(String key, String value) {
        params.put(key, value);
        return this;
    }

    public RequestInfo addHeaders(String key, String value) {
        headers.put(key, value);
        return this;
    }
}
