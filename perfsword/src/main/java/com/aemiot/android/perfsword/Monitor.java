package com.aemiot.android.perfsword;

import android.util.Log;

@SuppressWarnings("unused")
public class Monitor {
    public static long startTime = 0;

    public static void layoutStart(String layoutName) {

    }

    public static void layoutEnd() {

    }

    public static void measureStart() {
        startTime = System.currentTimeMillis();
    }

    public static void measureEnd(Object obj) {
        Log.e(LayoutMonitor.TAG, "measure|" + obj.getClass().getName() + "|" + (System.currentTimeMillis() - startTime));
    }

    public static void layoutStart() {
        startTime = System.currentTimeMillis();
    }

    public static void layoutEnd(Object obj) {
        Log.e(LayoutMonitor.TAG, "layout|" + obj.getClass().getName() + "|" + (System.currentTimeMillis() - startTime));
    }

    public static void drawStart() {
        startTime = System.currentTimeMillis();
    }

    public static void drawEnd(Object obj) {
        Log.e(LayoutMonitor.TAG, "draw|" + obj.getClass().getName() + "|" + (System.currentTimeMillis() - startTime));
    }
}
