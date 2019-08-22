package com.app.base.fragment;

import android.view.MotionEvent;

/**
 * @Description Fragment触摸事件接口
 * @Author Zhenhui
 * @Time 2019/8/19 23:02
 */
public interface FragmentOnTouchListener {
    boolean onTouchEvent(MotionEvent ev);
}
