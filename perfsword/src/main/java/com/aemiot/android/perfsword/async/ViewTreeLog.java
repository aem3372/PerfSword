package com.aemiot.android.perfsword.async;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class ViewTreeLog {
    private static final String TAG = "ViewTreeLog";

    public static void printMeasureValue(View view, String path) {
        if (TextUtils.isEmpty(path)) {
            path = "/";
        }
        if (view != null) {
            Log.e(TAG, path + "|measureWidth|" + view.getMeasuredWidth() + "|measureHegiht|" + view.getMeasuredHeight());

            if (view instanceof ViewGroup && ((ViewGroup) view).getChildCount() > 0) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); ++i) {
                    printMeasureValue(viewGroup.getChildAt(i), path + "/" + view.getClass().getSimpleName() + "(" + view.getId() + ")");
                }
            }
        }
    }
}
