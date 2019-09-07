package com.google.zxing.client.android;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.google.zxing.Result;
import com.google.zxing.client.android.camera.CameraManager;

/**
 * @Description
 * @Author Zhenhui
 * @Time 2019/9/6 17:16
 */
interface ZXingCallback {
    /**
     * A valid barcode has been found, so give an indication of success and show the results.
     *
     * @param rawResult   The contents of the barcode.
     * @param scaleFactor amount by which thumbnail was scaled
     * @param barcode     A greyscale bitmap of the camera data which was decoded.
     */
    void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor);

    @NonNull
    Activity getActivity();

    @NonNull
    CameraManager getCameraManager();

    @NonNull
    Handler getHandler();

    @NonNull
    ViewfinderView getViewfinderView();

    void drawViewfinder();

    void onError();
}
