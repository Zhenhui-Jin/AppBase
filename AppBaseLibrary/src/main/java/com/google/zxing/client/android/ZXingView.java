package com.google.zxing.client.android;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.app.base.R;
import com.google.zxing.Result;
import com.google.zxing.client.android.camera.CameraManager;


public class ZXingView extends FrameLayout {
    private static final String TAG = ZXingView.class.getSimpleName();
    private static final long RESTART_PREVIEW_AFTER_DELAY = 1500L;
    private static int statusMargin;

    private CameraManager mCameraManager;
    private ViewfinderView mViewfinderView;
    private SurfaceView mSurfaceView;
    private boolean hasSurface = false;

    private BeepManager mBeepManager;
    private AmbientLightManager mAmbientLightManager;
    private CaptureHandler mHandler;

    private ScanResultCallback mScanResultCallback;
    private TextView tvStatus;
    private ImgRectListener imgRectListener;

    public ZXingView(Context context) {
        this(context, null);
    }

    public ZXingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZXingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private SurfaceHolder.Callback mSurfaceHolderCallback = new SurfaceHolder.Callback() {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (holder == null) {
                Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
            }
            if (!hasSurface) {
                hasSurface = true;
                initCamera(holder);
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            hasSurface = false;
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            // do nothing
        }

    };

    private ZXingCallback mZXingCallback = new ZXingCallback() {


        /**
         * A valid barcode has been found, so give an indication of success and show the results.
         *
         * @param rawResult   The contents of the barcode.
         * @param scaleFactor amount by which thumbnail was scaled
         * @param barcode     A greyscale bitmap of the camera data which was decoded.
         */
        @Override
        public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
            Log.i(TAG, "onScanResult: rawResult=" + rawResult);
            boolean fromLiveScan = barcode != null;
            if (fromLiveScan && mScanResultCallback != null) {
                // Then not from history, so beep/vibrate and we have an image to draw on
                mBeepManager.playBeepSoundAndVibrate();
            }

            String result = rawResult.getText();
            boolean valid = !TextUtils.isEmpty(result) && result.length() == 18;
            if (valid){
                try {
                    Long value = Long.valueOf(result);
                } catch (NumberFormatException e) {
                    valid = false;
                }
            }
            if (mScanResultCallback != null) {

                long time = RESTART_PREVIEW_AFTER_DELAY;
                if (valid){
                    time = mScanResultCallback.onScanResult(result, barcode, scaleFactor);
                    tvStatus.setText(R.string.tip_scan_qr_code);
                }else {
                    tvStatus.setText(R.string.tip_invalid_code);
                }

                if (time > 0) {
                    restartPreviewAfterDelay(time);
                }
            } else {
                restartPreviewAfterDelay(RESTART_PREVIEW_AFTER_DELAY);
            }
        }

        @NonNull
        @Override
        public Activity getActivity() {
            return (Activity) getContext();
        }

        @NonNull
        @Override
        public CameraManager getCameraManager() {
            return mCameraManager;
        }

        @NonNull
        @Override
        public Handler getHandler() {
            return mHandler;
        }

        @NonNull
        @Override
        public ViewfinderView getViewfinderView() {
            return mViewfinderView;
        }

        @Override
        public void drawViewfinder() {
            mViewfinderView.drawViewfinder();
        }

        @Override
        public void onError() {
            if (mScanResultCallback != null) {
                mScanResultCallback.onError();
            }
        }
    };

    private void init(Context context, AttributeSet attrs, int defStyle) {
        inflate(context, R.layout.layout_zxing_view, this);
        mCameraManager = new CameraManager(context);
        mViewfinderView = findViewById(R.id.viewfinder_view);
        mViewfinderView.setCameraManager(mCameraManager);

        mSurfaceView = findViewById(R.id.preview_view);

        mBeepManager = new BeepManager(mZXingCallback);
        mAmbientLightManager = new AmbientLightManager(mZXingCallback);
        tvStatus = findViewById(R.id.status_view);
        statusMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,45,context.getResources().getDisplayMetrics());
        setWillNotDraw(false);

    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (mCameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            mCameraManager.openDriver(surfaceHolder);
            // Creating the mHandler starts the preview, which can also throw a RuntimeException.
            if (mHandler == null) {
                mHandler = new CaptureHandler(mZXingCallback, null, null, null, mCameraManager);
            }
        } catch (Exception e) {
            Log.w(TAG, e);
        }
    }


    public void startCamera() {
        mBeepManager.updatePrefs();
        mAmbientLightManager.start(mCameraManager);

        SurfaceHolder surfaceHolder = mSurfaceView.getHolder();
        if (hasSurface) {
            // The activity was paused but not stopped, so the surface still exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(surfaceHolder);
        } else {
            // Install the callback and wait for surfaceCreated() to init the camera.
            surfaceHolder.addCallback(mSurfaceHolderCallback);
        }
    }

    public void stopCamera() {
        if (mHandler != null) {
            mHandler.quitSynchronously();
            mHandler = null;
        }
        mAmbientLightManager.stop();
        if (mBeepManager != null) {
            mBeepManager.close();
        }
        mCameraManager.closeDriver();
        if (!hasSurface) {
            SurfaceHolder surfaceHolder = mSurfaceView.getHolder();
            surfaceHolder.removeCallback(mSurfaceHolderCallback);
        }
    }

    /**
     * 是否播放声音
     *
     * @param playBeep
     */
    public void setPlayBeep(boolean playBeep) {
        if (mBeepManager != null) {
            mBeepManager.setPlayBeep(playBeep);
        }
    }

    /**
     * 是否震动
     *
     * @param vibrate
     */
    public void setVibrate(boolean vibrate) {
        if (mBeepManager != null) {
            mBeepManager.setVibrate(vibrate);
        }
    }


    private void restartPreviewAfterDelay(long delayMS) {
        if (mHandler != null) {
            mHandler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
    }

    public void setScanResultCallback(ScanResultCallback callback) {
        this.mScanResultCallback = callback;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        Rect framingRect = mCameraManager.getFramingRect();
        if (framingRect != null) {
            int statusLeft = tvStatus.getLeft();
            int statusRight = tvStatus.getRight();
            int statusBottom = framingRect.top - statusMargin;
            int statusTop = statusBottom - tvStatus.getMeasuredHeight();
            tvStatus.layout(statusLeft,statusTop,statusRight,statusBottom);
        }

    }

    private boolean statusSet = false;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!statusSet){
            requestLayout();
            statusSet = true;
        }else {
            Rect imgRect = mViewfinderView.getImgRect();
            if (imgRect.left == 0){
                return;
            }
            if (imgRectListener != null) {
                imgRectListener.onImgRectGet(imgRect);
            }
        }
    }

    public void setImgRectListener(ImgRectListener imgRectListener) {
        this.imgRectListener = imgRectListener;
    }

    public interface ImgRectListener{
        public void onImgRectGet(Rect rect);
    }
}
