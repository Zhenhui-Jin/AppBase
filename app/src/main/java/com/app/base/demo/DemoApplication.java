package com.app.base.demo;

import com.app.base.LibBaseApplication;

/**
 * Created by JinHui on 2020/03/25.
 */
public class DemoApplication extends LibBaseApplication {
    @Override
    protected void init() {
        DemoApplication application = DemoApplication.getApplication();
    }

    @Override
    protected boolean isShowStackView() {
        return true;
    }
}
