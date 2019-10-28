package com.app.base.activity;

import android.view.ViewStub;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.app.base.R;
import com.app.base.view.TopBarType;

import static com.app.base.view.TopBarType.TitleBar;

/**
 * @param <DB> ViewDataBinding
 * @Description
 * @Author Zhenhui
 * @Time 2019/8/19 11:22
 */
public abstract class LibBaseBindingActivity<DB extends ViewDataBinding> extends LibBaseActivity {
    protected DB mDataBinding;

    @Override
    protected final void initContentView() {
        super.setContentView(R.layout.base_root_layout);
        ViewStub viewStub = findViewById(R.id.contentVs);
//        viewStub.setLayoutResource(getContentLayoutId());
//        View contentVs = viewStub.inflate();
        ViewStub toolbarVs = findViewById(R.id.toolbarVs);
        TopBarType topBarType = getTopBarType();
        if (topBarType == TitleBar) {
            toolbarVs.setLayoutResource(getTitleLayoutId());
            mToolbarView = toolbarVs.inflate();
        }
        mDataBinding = DataBindingUtil.setContentView(this, getContentLayoutId());
        mContentView = mDataBinding.getRoot();
    }
}
