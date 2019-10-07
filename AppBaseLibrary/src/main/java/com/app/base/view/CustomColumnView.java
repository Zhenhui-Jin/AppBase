package com.app.base.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.app.base.R;

/**
 * 自定义控件
 */
public class CustomColumnView extends LinearLayout {

    private String mColumnLeftText;
    private String mColumnRightText;
    private String mColumnLeftTextTypefaceName;
    private String mColumnRightTextTypefaceName;
    private float mColumnLeftTextSize = 0;
    private float mColumnRightTextSize = 0;
    private int mColumnLeftTextColor = Color.BLACK;
    private int mColumnRightTextColor = Color.GRAY;
    private int mColumnLeftIconId;
    private int mColumnRightIconId;

    private TextView leftTextView;
    private TextView rightTextView;
    private ImageView leftIconImageView;
    private ImageView rightIconImageView;
    private boolean mColumnLeftIconShow = false;
    private boolean mColumnRightIconShow = false;

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

        final TypedArray typedArray = getContext().obtainStyledAttributes(
                attrs, R.styleable.CustomColumnView, defStyle, 0);
        mColumnLeftText = typedArray.getString(R.styleable.CustomColumnView_columnLeftText);
        mColumnRightText = typedArray.getString(R.styleable.CustomColumnView_columnRightText);
        mColumnLeftTextSize = typedArray.getDimension(R.styleable.CustomColumnView_columnLeftTextSize, sp2px(context, 16));
        mColumnRightTextSize = typedArray.getDimension(R.styleable.CustomColumnView_columnRightTextSize, sp2px(context, 16));
        mColumnLeftTextColor = typedArray.getColor(R.styleable.CustomColumnView_columnLeftTextColor, mColumnLeftTextColor);
        mColumnRightTextColor = typedArray.getColor(R.styleable.CustomColumnView_columnRightTextColor, mColumnRightTextColor);
        mColumnLeftIconId = typedArray.getResourceId(R.styleable.CustomColumnView_columnLeftIcon, mColumnLeftIconId);
        mColumnRightIconId = typedArray.getResourceId(R.styleable.CustomColumnView_columnRightIcon, mColumnRightIconId);
        mColumnLeftIconShow = typedArray.getBoolean(R.styleable.CustomColumnView_columnLeftIconShow, mColumnLeftIconShow);
        mColumnRightIconShow = typedArray.getBoolean(R.styleable.CustomColumnView_columnRightIconShow, mColumnRightIconShow);

        mColumnLeftTextTypefaceName = typedArray.getString(R.styleable.CustomColumnView_columnLeftTextTypeface);
        mColumnRightTextTypefaceName = typedArray.getString(R.styleable.CustomColumnView_columnRightTextTypeface);

        typedArray.recycle();

        inflate(context, R.layout.layout_custom_column_view, this);
        leftTextView = findViewById(R.id.tv_left_text);
        rightTextView = findViewById(R.id.tv_right_text);
        leftIconImageView = findViewById(R.id.iv_left_icon);
        rightIconImageView = findViewById(R.id.iv_right_icon);
        View leftLayout = findViewById(R.id.left_layout);
        View rightLayout = findViewById(R.id.right_layout);

        int iconPadding = dp2px(context, 3);

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        leftLayout.setPadding(0, paddingTop - iconPadding, 0, paddingBottom - iconPadding);
        rightLayout.setPadding(0, paddingTop, 0, paddingBottom);
//        leftIconImageView.setPadding(0, iconPadding, 0, iconPadding);
//        rightIconImageView.setPadding(0, paddingTop, 0, paddingBottom);
        leftTextView.setPadding(0, paddingTop, 0, paddingBottom);
        setPadding(paddingLeft, 0, paddingRight, 0);


        setLeftText(mColumnLeftText);
        setLeftTextColor(mColumnLeftTextColor);
        setLeftTextSize(TypedValue.COMPLEX_UNIT_PX, mColumnLeftTextSize);
        setLeftIcon(mColumnLeftIconId);
        showLeftIcon(mColumnLeftIconShow);

        setRightText(mColumnRightText);
        setRightTextColor(mColumnRightTextColor);
        setRightTextSize(TypedValue.COMPLEX_UNIT_PX, mColumnRightTextSize);
        setRightIcon(mColumnRightIconId);
        showRightIcon(mColumnRightIconShow);

        setTypeface();
    }

    private void setTypeface() {
        try {
            if (mColumnLeftTextTypefaceName != null) {
                Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + mColumnLeftTextTypefaceName);
                if (typeface != null) {
                    leftTextView.setTypeface(typeface);
                }
            }
            if (mColumnRightTextTypefaceName != null) {
                Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + mColumnRightTextTypefaceName);
                if (typeface != null) {
                    rightTextView.setTypeface(typeface);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void setLeftTextSize(float size) {
        setLeftTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public void setLeftTextSize(int unit, float size) {
        leftTextView.setTextSize(unit, size);
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

    public void setRightTextSize(float size) {
        setRightTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public void setRightTextSize(int unit, float size) {
        rightTextView.setTextSize(unit, size);
    }

    public ImageView getLeftIconImageView() {
        return leftIconImageView;
    }

    public void setLeftIcon(@DrawableRes int iconId) {
        if (iconId != 0) {
            leftIconImageView.setImageResource(iconId);
        }
    }

    public void showLeftIcon(boolean isShow) {
        leftIconImageView.setVisibility(isShow ? VISIBLE : GONE);
    }

    public ImageView getRightIconImageView() {
        return rightIconImageView;
    }

    public void setRightIcon(@DrawableRes int iconId) {
        if (iconId != 0) {
            rightIconImageView.setImageResource(iconId);
        }
    }

    public void showRightIcon(boolean isShow) {
        rightIconImageView.setVisibility(isShow ? VISIBLE : GONE);
    }

    //data binding setter
    public void setColumnLeftText(CharSequence text) {
        leftTextView.setText(text);
    }

    //data binding setter
    public void setColumnRightText(CharSequence text) {
        rightTextView.setText(text);
    }

    //data binding setter
    public void setColumnRightIconShow(boolean show) {
        rightIconImageView.setVisibility(show ? VISIBLE : GONE);
    }

    /**
     * dp to px
     *
     * @param context
     * @param dpValue dp
     * @return px
     */
    static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * sp to px
     *
     * @param context
     * @param spValue
     * @return
     */
    static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
