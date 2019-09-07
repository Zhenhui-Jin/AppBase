package com.google.zxing.client.android;

import android.graphics.Bitmap;

/**
 * @Description 扫描结果回调
 * @Author Zhenhui
 * @Time 2019/9/6 17:16
 */
public interface ScanResultCallback {
    /**
     * 扫描结果
     *
     * @param result      结果
     * @param barcode     解析的图片
     * @param scaleFactor 缩放
     * @return 刷新扫描时间间隔，0为不扫描
     */
    int onScanResult(String result, Bitmap barcode, float scaleFactor);

    /**
     *扫描错误
     */
    void onError();
}
