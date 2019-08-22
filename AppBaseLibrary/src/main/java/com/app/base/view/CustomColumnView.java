package com.app.base.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.app.base.R;

/**
 *
 */
public class CustomColumnView extends LinearLayout {

    private String mColumnLeftText;
    private String mColumnRightText;
    private int mColumnLeftTextSize = 14;
    private int mColumnRightTextSize = 14;
    private int mColumnLeftTextColor = Color.BLACK;
    private int mColumnRightTextColor = Color.GRAY;
    private int mColumnRightIconId;

    private TextView leftTextView;
    private TextView rightTextView;
    private ImageView rightIconImageView;

    public CustomColumnView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public CustomColumnView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public CustomColumnView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.CustomColumnView, defStyle, 0);

        mColumnLeftText = a.getString(R.styleable.CustomColumnView_columnLeftText);
        mColumnRightText = a.getString(R.styleable.CustomColumnView_columnRightText);
        mColumnLeftTextSize = a.getInteger(R.styleable.CustomColumnView_columnLeftTextSize, mColumnLeftTextSize);
        mColumnRightTextSize = a.getInteger(R.styleable.CustomColumnView_columnRightTextSize, mColumnRightTextSize);
        mColumnLeftTextColor = a.getColor(R.styleable.CustomColumnView_columnLeftTextColor, mColumnLeftTextColor);
        mColumnRightTextColor = a.getColor(R.styleable.CustomColumnView_columnRightTextColor, mColumnRightTextColor);
        mColumnRightIconId = a.getResourceId(R.styleable.CustomColumnView_columnRightIcon, mColumnRightIconId);

        a.recycle();

        inflate(context, R.layout.layout_custom_column_view, this);
        leftTextView = findViewById(R.id.tv_left_text);
        rightTextView = findViewById(R.id.tv_right_text);
        rightIconImageView = findViewById(R.id.iv_right_icon);

        setLeftText(mColumnLeftText);
        setLeftTextColor(mColumnLeftTextColor);
        setRightText(mColumnRightText);
        setRightTextColor(mColumnRightTextColor);
        setRightIcon(mColumnRightIconId);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

    }

    public TextView getLeftTextView() {
        return leftTextView;
    }

    public void setLeftText(@NonNull CharSequence text) {
        leftTextView.setText(text);
    }

    public void setLeftTextColor(@ColorInt int color) {
        leftTextView.setTextColor(color);
    }

    public TextView getRightTextView() {
        return rightTextView;
    }

    public void setRightText(@NonNull CharSequence text) {
        rightTextView.setText(text);
    }

    public void setRightTextColor(@ColorInt int color) {
        rightTextView.setTextColor(color);
    }

    public ImageView getRightIconImageView() {
        return rightIconImageView;
    }

    public void setRightIcon(@DrawableRes int iconId) {
        if (iconId != 0) {
            rightIconImageView.setImageResource(iconId);
        }
    }

    public void isShowRightIcon(boolean isShow) {
        rightIconImageView.setVisibility(isShow ? VISIBLE : GONE);
    }

}
