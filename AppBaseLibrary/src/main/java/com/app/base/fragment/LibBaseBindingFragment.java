package com.app.base.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.app.base.R;
import com.app.base.view.TopBarType;

import static com.app.base.view.TopBarType.TitleBar;

/**
 * @param <DB> ViewDataBinding
 * @Description 使用DataBinding
 * @Author Zhenhui
 * @Time 2019/8/19 9:53
 */
public abstract class LibBaseBindingFragment<DB extends ViewDataBinding> extends LibBaseFragment {
    protected DB mDataBinding;

    @Override
    protected final View initContentView() {
        int contentLayoutId = getContentLayoutId();
        View view = mLayoutInflater.inflate(R.layout.base_root_layout, null);
        ViewStub toolbarVs = view.findViewById(R.id.toolbarVs);
        TopBarType topBarType = getTopBarType();
        if (topBarType == TitleBar) {
            toolbarVs.setLayoutResource(getTitleLayoutId());
            mToolbarView = toolbarVs.inflate();
        }
        ViewGroup contentLayout = view.findViewById(R.id.content_layout);
        mDataBinding = DataBindingUtil.inflate(mLayoutInflater, contentLayoutId, contentLayout, true);
        return view;
    }
}
