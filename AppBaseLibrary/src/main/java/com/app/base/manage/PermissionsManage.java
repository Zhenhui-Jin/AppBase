package com.app.base.manage;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.tbruyelle.rxpermissions2.RxPermissions;

/**
 * @Description 权限管理
 * @Author Zhenhui
 * @Time 2019/8/23 12:06
 */
public class PermissionsManage {

    public static RxPermissions getRxPermissions(Fragment fragment) {
        return new RxPermissions(fragment);
    }

    public static RxPermissions getRxPermissions(FragmentActivity activity) {
        return new RxPermissions(activity);
    }


}
