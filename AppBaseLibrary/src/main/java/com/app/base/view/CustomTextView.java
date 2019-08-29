package com.app.base.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.app.base.R;

/**
 * 自定义TextView
 */
public class CustomTextView extends AppCompatTextView {

    private String mColumnTextTypeface;

    public CustomTextView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {

        final TypedArray typedArray = getContext().obtainStyledAttributes(
                attrs, R.styleable.CustomTextView, defStyle, 0);
        mColumnTextTypeface = typedArray.getString(R.styleable.CustomTextView_columnTextTypeface);

        typedArray.recycle();


        setTypeface();
    }

    private void setTypeface() {
        try {
            if (mColumnTextTypeface != null) {
                Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + mColumnTextTypeface);
                if (typeface != null) {
                    setTypeface(typeface);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
