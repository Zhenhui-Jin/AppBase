package com.app.base.manage;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.kaopiz.kprogresshud.KProgressHUD;

/**
 * @Description
 * @Author Zhenhui
 * @Time 2019/9/29 10:49
 */
public class DialogManage {
    private static final DialogManage ourInstance = new DialogManage();

    public static DialogManage getInstance() {
        return ourInstance;
    }

    private KProgressHUD loadingDialog;
    private int loadCount;

    private DialogManage() {

    }

    public void showLoadingDialog(@NonNull Activity activity, String label) {
        //Log.i("jinhui", "showLoadingDialog: " + Log.getStackTraceString(new Throwable()));
        if (loadingDialog == null || !loadingDialog.isShowing()) {
            loadCount = 0;
            loadingDialog = KProgressHUD.create(activity)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel(label);
        }
        loadCount++;
        loadingDialog.show();
    }

    public void hideLoadingDialog() {
        //Log.i("jinhui", "hideLoadingDialog: " + Log.getStackTraceString(new Throwable()));
        loadCount--;
        if (loadCount <= 0) {
            if (loadingDialog != null) {
                loadingDialog.dismiss();
            }
            loadingDialog = null;
            loadCount = 0;
        }
    }

    public void hideAllLoadingDialog() {
        loadCount = 0;
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        loadingDialog = null;
    }
}
