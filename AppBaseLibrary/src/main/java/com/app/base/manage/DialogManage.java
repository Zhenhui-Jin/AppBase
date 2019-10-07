package com.app.base.manage;

import android.app.Activity;

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
        loadCount++;
        if (loadingDialog == null || !loadingDialog.isShowing()) {
            loadingDialog = KProgressHUD.create(activity)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel(label);
        }
        loadingDialog.show();
    }

    public void hideLoadingDialog() {
        loadCount--;
        if (loadCount <= 0) {
            if (loadingDialog != null) {
                loadingDialog.dismiss();
            }
            loadingDialog = null;
            loadCount = 0;
        }
    }
}
