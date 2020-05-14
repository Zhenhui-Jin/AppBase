package com.app.base.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.base.R;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

/**
 * 自定义Title控件
 */
public class CustomTitleBar extends LinearLayout implements View.OnClickListener {

    private String mColumnTitleText;
    private String mColumnTitleTextTypefaceName;
    private float mColumnTitleTextSize = 0;
    private int mColumnTitleTextColor = Color.BLACK;

    private String mColumnRightText;
    private String mColumnRightTextTypefaceName;
    private float mColumnRightTextSize = 0;
    private int mColumnRightTextColor = Color.BLACK;

    private int mColumnLeftIconId;
    private int mColumnRightIconId;
    private int mColumnRightExtendIconId;

    private TextView titleTextView;
    private TextView rightTextView;
    private ImageView leftIconImageView;
    private ImageView rightIconImageView;
    private ImageView rightExtendIconImageView;
    private View llRightLayout;
    private boolean mColumnLeftIconShow = false;
    private boolean mColumnRightIconShow = false;
    private boolean mColumnRightExtendIconShow = false;
    private boolean mColumnRightTextShow = false;
    private OnTitleBarClickListener mOnTitleBarClickListener;

    public CustomTitleBar(Context context) {
        super(context);
        init(context, null, 0);
    }

    public CustomTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public CustomTitleBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        final TypedArray typedArray = getContext().obtainStyledAttributes(
                attrs, R.styleable.CustomTitleBar, defStyle, 0);
        mColumnTitleText = typedArray.getString(R.styleable.CustomTitleBar_columnTitleText);
        mColumnTitleTextSize = typedArray.getDimension(R.styleable.CustomTitleBar_columnTitleTextSize, sp2px(context, 18));
        mColumnTitleTextColor = typedArray.getColor(R.styleable.CustomTitleBar_columnTitleTextColor, mColumnTitleTextColor);
        mColumnTitleTextTypefaceName = typedArray.getString(R.styleable.CustomTitleBar_columnTitleTextTypeface);

        mColumnRightText = typedArray.getString(R.styleable.CustomTitleBar_columnTitleRightText);
        mColumnRightTextSize = typedArray.getDimension(R.styleable.CustomTitleBar_columnTitleRightTextSize, sp2px(context, 16));
        mColumnRightTextColor = typedArray.getColor(R.styleable.CustomTitleBar_columnTitleRightTextColor, mColumnRightTextColor);
        mColumnRightTextShow = typedArray.getBoolean(R.styleable.CustomTitleBar_columnTitleRightTextShow, mColumnRightTextShow);
        mColumnRightTextTypefaceName = typedArray.getString(R.styleable.CustomTitleBar_columnTitleRightTextTypeface);

        mColumnLeftIconId = typedArray.getResourceId(R.styleable.CustomTitleBar_columnTitleLeftIcon, mColumnLeftIconId);
        mColumnLeftIconShow = typedArray.getBoolean(R.styleable.CustomTitleBar_columnTitleLeftIconShow, mColumnLeftIconShow);

        mColumnRightIconId = typedArray.getResourceId(R.styleable.CustomTitleBar_columnTitleRightIcon, mColumnRightIconId);
        mColumnRightIconShow = typedArray.getBoolean(R.styleable.CustomTitleBar_columnTitleRightIconShow, mColumnRightIconShow);

        mColumnRightExtendIconId = typedArray.getResourceId(R.styleable.CustomTitleBar_columnTitleRightExtendIcon, mColumnRightExtendIconId);
        mColumnRightExtendIconShow = typedArray.getBoolean(R.styleable.CustomTitleBar_columnTitleRightExtendIconShow, mColumnRightExtendIconShow);

        typedArray.recycle();


        inflate(context, R.layout.layout_custom_title_bar, this);
        llRightLayout = findViewById(R.id.ll_right_layout);
        titleTextView = findViewById(R.id.tv_title_text);
        rightTextView = findViewById(R.id.tv_right_text);
        leftIconImageView = findViewById(R.id.iv_left_icon);
        rightIconImageView = findViewById(R.id.iv_right_icon);
        rightExtendIconImageView = findViewById(R.id.iv_right_extend_icon);
        leftIconImageView.setOnClickListener(this);
        rightIconImageView.setOnClickListener(this);
        rightExtendIconImageView.setOnClickListener(this);
        rightTextView.setOnClickListener(this);

        int iconPadding = dp2px(context, 5);

        int paddingLeft = getPaddingLeft() /*- iconPadding*/;
        int paddingTop = getPaddingTop() - iconPadding;
        int paddingRight = getPaddingRight() /*- iconPadding*/;
        int paddingBottom = getPaddingBottom() - iconPadding;

        leftIconImageView.setPadding(0, iconPadding, 0, iconPadding);
        rightIconImageView.setPadding(0, iconPadding, 0, iconPadding);
        rightExtendIconImageView.setPadding(0, iconPadding, 0, iconPadding);
        titleTextView.setPadding(0, iconPadding, 0, iconPadding);
        rightTextView.setPadding(0, iconPadding, 0, iconPadding);
        setPadding(0, paddingTop, 0, paddingBottom);

        setTitleText(mColumnTitleText);
        setTitleTextColor(mColumnTitleTextColor);
        setTitleTextSize(TypedValue.COMPLEX_UNIT_PX, mColumnTitleTextSize);

        setTitleRightText(mColumnRightText);
        setTitleRightTextColor(mColumnRightTextColor);
        setTitleRightTextSize(TypedValue.COMPLEX_UNIT_PX, mColumnRightTextSize);
        showTitleRightText(mColumnRightTextShow);

        setLeftIcon(mColumnLeftIconId);
        showLeftIcon(mColumnLeftIconShow);

        setRightIcon(mColumnRightIconId);
        showRightIcon(mColumnRightIconShow);

        setRightExtendIcon(mColumnRightExtendIconId);
        showRightExtendIcon(mColumnRightExtendIconShow);

        setTypeface();
    }

    private void setTypeface() {
        try {
            if (mColumnTitleTextTypefaceName != null) {
                Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + mColumnTitleTextTypefaceName);
                if (typeface != null) {
                    titleTextView.setTypeface(typeface);
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

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int left = Math.max(titleTextView.getPaddingLeft(), leftIconImageView.getMeasuredWidth());
        int right = Math.max(titleTextView.getPaddingRight(), llRightLayout.getMeasuredWidth());
        int padding = Math.max(left, right);
        if (titleTextView.getGravity() == Gravity.CENTER) {
            titleTextView.setPadding(padding, titleTextView.getPaddingTop(), padding, titleTextView.getPaddingBottom());
        }
    }

    public TextView getTitleTextView() {
        return titleTextView;
    }

    public void setTitleText(@NonNull CharSequence text) {
        titleTextView.setText(text);
    }

    public void setTitleTextColor(@ColorInt int color) {
        titleTextView.setTextColor(color);
    }

    public void setTitleTextSize(float size) {
        setTitleTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public void setTitleTextSize(int unit, float size) {
        titleTextView.setTextSize(unit, size);
    }

    public void setTitleTextGravity(int gravity) {
        titleTextView.setGravity(gravity);
        invalidate();
    }

    public TextView getTitleRightTextView() {
        return rightTextView;
    }

    public void setTitleRightText(@NonNull CharSequence text) {
        rightTextView.setText(text);
        showTitleRightText(!TextUtils.isEmpty(text));
    }

    public void setTitleRightTextColor(@ColorInt int color) {
        rightTextView.setTextColor(color);
    }

    public void setTitleRightTextSize(float size) {
        setTitleRightTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public void setTitleRightTextSize(int unit, float size) {
        rightTextView.setTextSize(unit, size);
    }

    public void showTitleRightText(boolean isShow) {
        int visibility = isShow ? VISIBLE : GONE;
        if (rightTextView.getVisibility() != visibility) {
            rightTextView.setVisibility(visibility);
            invalidate();
        }
    }

    public ImageView getLeftIconImageView() {
        return leftIconImageView;
    }

    public void setLeftIcon(@DrawableRes int iconId) {
        if (iconId != 0) {
            leftIconImageView.setImageResource(iconId);
            showLeftIcon(true);
        } else {
            showLeftIcon(false);
        }
    }

    public void showLeftIcon(boolean isShow) {
        int visibility = isShow ? VISIBLE : GONE;
        if (leftIconImageView.getVisibility() != visibility) {
            leftIconImageView.setVisibility(visibility);
            invalidate();
        }
    }

    public ImageView getRightIconImageView() {
        return rightIconImageView;
    }

    public ImageView getRightExtendIconImageView() {
        return rightExtendIconImageView;
    }

    public void setRightIcon(@DrawableRes int iconId) {
        if (iconId != 0) {
            rightIconImageView.setImageResource(iconId);
            showRightIcon(true);
        } else {
            showRightIcon(false);
        }
    }

    public void showRightIcon(boolean isShow) {
        int visibility = isShow ? VISIBLE : GONE;
        if (rightIconImageView.getVisibility() != visibility) {
            rightIconImageView.setVisibility(visibility);
            invalidate();
        }
    }

    public void setRightExtendIcon(@DrawableRes int iconId) {
        if (iconId != 0) {
            rightExtendIconImageView.setImageResource(iconId);
            showRightExtendIcon(true);
        } else {
            showRightExtendIcon(false);
        }
    }

    public void showRightExtendIcon(boolean isShow) {
        int visibility = isShow ? VISIBLE : GONE;
        if (rightExtendIconImageView.getVisibility() != visibility) {
            rightExtendIconImageView.setVisibility(visibility);
            invalidate();
        }
    }

    public void setOnTitleBarClickListener(OnTitleBarClickListener listener) {
        this.mOnTitleBarClickListener = listener;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == rightIconImageView.getId()) {
            if (mOnTitleBarClickListener != null) {
                mOnTitleBarClickListener.onClickRightIcon();
            }
        } else if (view.getId() == rightTextView.getId()) {
            if (mOnTitleBarClickListener != null) {
                mOnTitleBarClickListener.onClickRightText();
            }
        } else if (view.getId() == rightExtendIconImageView.getId()) {
            if (mOnTitleBarClickListener != null) {
                mOnTitleBarClickListener.onClickRightExtendIcon();
            }
        } else if (view.getId() == leftIconImageView.getId()) {
            if (mOnTitleBarClickListener != null) {
                mOnTitleBarClickListener.onClickLeftIcon();
            }
        }
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

    public interface OnTitleBarClickListener {
        void onClickLeftIcon();

        void onClickRightIcon();

        void onClickRightExtendIcon();

        void onClickRightText();
    }
}
