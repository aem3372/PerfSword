package com.aemiot.android.perfsword.async;

import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;

import java.util.Map;

public class ViewLayoutUtils {
    private static final String TAG = "ViewLayoutUtils";

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

    public static void printLayoutParams(View view, String path) {
        if (TextUtils.isEmpty(path)) {
            path = "/";
        }
        if (view != null) {
            if (view.getLayoutParams() != null) {
                Log.e(TAG, path + "|lp.width|" + view.getLayoutParams().width + "|lp.height|" + view.getLayoutParams().height);
            } else {
                Log.e(TAG, path + "|not find layoutParams");
            }
            if (view instanceof ViewGroup && ((ViewGroup) view).getChildCount() > 0) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); ++i) {
                    printLayoutParams(viewGroup.getChildAt(i), path + "/" + view.getClass().getSimpleName() + "(" + view.getId() + ")");
                }
            }
        }
    }

    public static void replaceLayoutParams(View view, Map<View, Pair<Integer, Integer>> orginValueMap) {
        if (view != null) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams != null) {
                Pair<Integer, Integer> orgin = Pair.create(layoutParams.width, layoutParams.height);
                layoutParams.width = view.getMeasuredWidth();
                layoutParams.height = view.getMeasuredHeight();
                orginValueMap.put(view, orgin);
            }
            if (view instanceof ViewGroup && ((ViewGroup) view).getChildCount() > 0) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); ++i) {
                    replaceLayoutParams(viewGroup.getChildAt(i), orginValueMap);
                }
            }
        }
    }
}
