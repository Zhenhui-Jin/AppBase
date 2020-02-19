package com.app.base.net;

import android.app.Application;
import android.text.TextUtils;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.MemoryCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okgo.request.PutRequest;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import androidx.annotation.NonNull;
import okhttp3.OkHttpClient;

/**
 * @Description 封装OkGo使用
 * @Author Zhenhui
 * @Time 2019/8/15 23:01
 */
public abstract class LibBaseOkGo {
    private static final int DEFAULT_TIME_OUT = 60;//默认超时时间  SECONDS
    private static final int DEFAULT_READ_TIME_OUT = 60;//默认读写超时 SECONDS
    private static final int RETRY_COUNT = 3;//超时重连次数

    protected Application mApplication;

    public LibBaseOkGo(Application application) {
        mApplication = application;
    }

    protected void init(InputStream... certificates) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        int connectTimeOut = getConnectTimeOut();
        if (connectTimeOut <= 0) {
            connectTimeOut = DEFAULT_TIME_OUT;
        }
        int readTimeOut = getReadTimeOut();
        if (readTimeOut <= 0) {
            readTimeOut = DEFAULT_READ_TIME_OUT;
        }
        builder.readTimeout(readTimeOut, TimeUnit.SECONDS);      //全局的读取超时时间
        builder.writeTimeout(readTimeOut, TimeUnit.SECONDS);     //全局的写入超时时间
        builder.connectTimeout(connectTimeOut, TimeUnit.SECONDS);   //全局的连接超时时间

        //自动管理cookie（或者叫session的保持），以下几种任选其一就行
//        builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));            //使用sp保持cookie，如果cookie不过期，则一直有效
//        builder.cookieJar(new CookieJarImpl(new DBCookieStore(this)));              //使用数据库保持cookie，如果cookie不过期，则一直有效
        builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));            //使用内存保持cookie，app退出后，cookie消失

//        //https相关设置，以下几种方案根据需要自己设置
//        //方法一：信任所有证书,不安全有风险
//        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
//        //方法二：自定义信任规则，校验服务端证书
        HttpsUtils.SSLParams sslParams = certificates == null ? HttpsUtils.getSslSocketFactory() : HttpsUtils.getSslSocketFactory(certificates);
//        //方法三：使用预埋证书，校验服务端证书（自签名证书）
//        //HttpsUtils.SSLParams sslParams3 = HttpsUtils.getSslSocketFactory(getAssets().open("srca.cer"));
//        //方法四：使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
//        //HttpsUtils.SSLParams sslParams4 = HttpsUtils.getSslSocketFactory(getAssets().open("xxx.bks"), "123456", getAssets().open("yyy.cer"));
        builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
//        //配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
//        builder.hostnameVerifier(new SafeHostnameVerifier());

        if (isShowHttpLog()) {
            //log相关
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
            loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);        //log打印级别，决定了log显示的详细程度
            loggingInterceptor.setColorLevel(Level.INFO);                               //log颜色级别，决定了log在控制台显示的颜色
            builder.addInterceptor(loggingInterceptor);                                 //添加OkGo默认debug日志
        }

        OkGo.getInstance().init(mApplication)  //必须调用初始化
                .setOkHttpClient(builder.build()) //建议设置OkHttpClient，不设置会使用默认的
                .setCacheMode(CacheMode.NO_CACHE)  //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE) //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(getRetryCount())  //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
                .addCommonHeaders(getHttpHeaders())  //全局公共头
                .addCommonParams(getHttpParams());  //全局公共参数
    }

    /**
     * 全局公共请求参数
     *
     * @return
     */
    private HttpParams getHttpParams() {
        Map<String, String> defaultParams = getDefaultParams();
        HttpParams params = mapToParams(defaultParams);
        return params;
    }

    /**
     * map集合转换成Params
     *
     * @param map
     * @return
     */
    private HttpParams mapToParams(Map<String, String> map) {
        HttpParams params = new HttpParams();
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                params.put(entry.getKey(), entry.getValue());
            }
        }
        return params;
    }

    /**
     * 全局公共请求参数
     *
     * @return
     */
    private HttpHeaders getHttpHeaders() {
        Map<String, String> defaultHeaders = getDefaultHeaders();
        HttpHeaders headers = mapToHeaders(defaultHeaders);
        return headers;
    }

    /**
     * map集合转换成HttpHeaders
     *
     * @param map
     * @return
     */
    private HttpHeaders mapToHeaders(Map<String, String> map) {
        HttpHeaders headers = new HttpHeaders();
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                headers.put(entry.getKey(), entry.getValue());
            }
        }
        return headers;
    }

    /**
     * 全局公共请求参数
     *
     * @return
     */
    @NonNull
    protected Map<String, String> getDefaultParams() {
        return new HashMap<>();
    }

    /**
     * 全局公共请求参数
     *
     * @return
     */
    @NonNull
    protected Map<String, String> getDefaultHeaders() {
        return new HashMap<>();
    }

    /**
     * 全局统一超时重连次数，默认为3次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以返回0
     *
     * @return
     */
    protected int getRetryCount() {
        return RETRY_COUNT;
    }

    /**
     * 连接超时
     *
     * @return
     */
    protected int getConnectTimeOut() {
        return DEFAULT_TIME_OUT;
    }

    /**
     * 读写超时
     *
     * @return
     */
    protected int getReadTimeOut() {
        return DEFAULT_READ_TIME_OUT;
    }

    /**
     * 是否打印请求日志
     *
     * @return
     */
    protected boolean isShowHttpLog() {
        return true;
    }

    private <T> void get(RequestInfo requestInfo, LibBaseHttpCallback<T> callback) {
        HttpHeaders headers = getHttpHeaders();
        headers.put(mapToHeaders(requestInfo.getHeaders()));
        HttpParams params = getHttpParams();
        params.put(requestInfo.getParams(), true);
        GetRequest<T> request = OkGo.<T>get(requestInfo.getUrl())
                .tag(requestInfo.getTag())
                .headers(headers)
                .params(params);
        request.execute(new LibCallback<>(callback));
    }

    private <T> void post(RequestInfo requestInfo, LibBaseHttpCallback<T> callback) {
        HttpHeaders headers = getHttpHeaders();
        headers.put(mapToHeaders(requestInfo.getHeaders()));
        HttpParams params = getHttpParams();
        params.put(requestInfo.getParams(), true);
        PostRequest<T> request = OkGo.<T>post(requestInfo.getUrl())
                .tag(requestInfo.getTag())
                .headers(headers)
                .params(params);
        String bodyJson = requestInfo.getBodyJson();
        if (!TextUtils.isEmpty(bodyJson)) {
            request.upJson(bodyJson);
        }
        request.execute(new LibCallback<>(callback));
    }

    private <T> void put(RequestInfo requestInfo, LibBaseHttpCallback<T> callback) {
        HttpHeaders headers = getHttpHeaders();
        headers.put(mapToHeaders(requestInfo.getHeaders()));
        HttpParams params = getHttpParams();
        params.put(requestInfo.getParams(), true);
        PutRequest<T> request = OkGo.<T>put(requestInfo.getUrl())
                .tag(requestInfo.getTag())
                .headers(headers)
                .params(params);
        String bodyJson = requestInfo.getBodyJson();
        if (!TextUtils.isEmpty(bodyJson)) {
            request.upJson(bodyJson);
        }
        request.execute(new LibCallback<>(callback));
    }

    /**
     * 接口请求
     *
     * @param requestInfo
     * @param callback
     * @param <T>
     */
    final public <T> void execute(@NonNull RequestInfo requestInfo, LibBaseHttpCallback<T> callback) {
        RequestMethod method = requestInfo.getMethod();
        if (method == RequestMethod.POST) {
            post(requestInfo, callback);
        } else if (method == RequestMethod.PUT) {
            put(requestInfo, callback);
        } else {
            get(requestInfo, callback);
        }
    }

    /**
     * 根据Tag取消请求
     *
     * @param tag
     */
    public void cancelTag(Object tag) {
        OkGo.getInstance().cancelTag(tag);
    }
}
