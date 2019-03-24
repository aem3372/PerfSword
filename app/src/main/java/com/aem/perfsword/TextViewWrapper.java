package com.aem.perfsword;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;

public class TextViewWrapper extends AppCompatTextView {
    public TextViewWrapper(Context context) {
        super(context);
    }

    public TextViewWrapper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewWrapper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e("ViewTreeLog", "" + widthMeasureSpec + "|" + heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
