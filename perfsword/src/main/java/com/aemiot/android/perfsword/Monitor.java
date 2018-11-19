package com.aemiot.android.perfsword;

import android.util.Log;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

@SuppressWarnings("unused")
public class Monitor {
    static class RuntimeTrace {
        public static final int TYPE_INFLATE = 0;
        public static final int TYPE_CREATE_VIEW = 1;
        public static final int TYPE_MEASURE = 2;
        public static final int TYPE_LAYOUT = 3;
        public static final int TYPE_DRAW = 4;

        public Object object;
        public int type;
        public long startTime;
        public long endTime;

        public static RuntimeTrace inflate(String layoutName) {
            RuntimeTrace runtimeTrace = new RuntimeTrace();
            runtimeTrace.type = TYPE_INFLATE;
            runtimeTrace.object = layoutName;
            runtimeTrace.startTime = System.nanoTime();
            return runtimeTrace;
        }

        public static RuntimeTrace createView(String name) {
            RuntimeTrace runtimeTrace = new RuntimeTrace();
            runtimeTrace.type = TYPE_CREATE_VIEW;
            runtimeTrace.object = name;
            runtimeTrace.startTime = System.nanoTime();
            return runtimeTrace;
        }

        public static RuntimeTrace measure(Object object) {
            RuntimeTrace runtimeTrace = new RuntimeTrace();
            runtimeTrace.type = TYPE_MEASURE;
            runtimeTrace.object = object;
            runtimeTrace.startTime = System.nanoTime();
            return runtimeTrace;
        }

        public static RuntimeTrace layout(Object object) {
            RuntimeTrace runtimeTrace = new RuntimeTrace();
            runtimeTrace.type = TYPE_LAYOUT;
            runtimeTrace.object = object;
            runtimeTrace.startTime = System.nanoTime();
            return runtimeTrace;
        }

        public static RuntimeTrace draw(Object object) {
            RuntimeTrace runtimeTrace = new RuntimeTrace();
            runtimeTrace.type = TYPE_DRAW;
            runtimeTrace.object = object;
            runtimeTrace.startTime = System.nanoTime();
            return runtimeTrace;
        }

        public void end() {
            endTime = System.nanoTime();
        }

        public long time() {
            return endTime - startTime;
        }
    }

    public static Map<Object, RuntimeTrace> traceMap = new HashMap<>();
    public static Queue<RuntimeTrace> logQueue = new ArrayDeque<>(128);

    public static void inflateStart(String layoutName) {
        traceMap.put(layoutName, RuntimeTrace.inflate(layoutName));
    }

    public static void inflateEnd(String layoutName) {
        RuntimeTrace runtimeTrace = traceMap.get(layoutName);
        runtimeTrace.end();
        logQueue.add(runtimeTrace);
    }

    public static void createViewStart(String name) {
        traceMap.put(name, RuntimeTrace.createView(name));
    }

    public static void createViewEnd(String name) {
        RuntimeTrace runtimeTrace = traceMap.get(name);
        runtimeTrace.end();
        logQueue.add(runtimeTrace);
    }

    public static void measureStart(Object obj) {
        traceMap.put(obj, RuntimeTrace.measure(obj));
    }

    public static void measureEnd(Object obj) {
        RuntimeTrace runtimeTrace = traceMap.get(obj);
        runtimeTrace.end();
        logQueue.add(runtimeTrace);
    }

    public static void layoutStart(Object obj) {
        traceMap.put(obj, RuntimeTrace.layout(obj));
    }

    public static void layoutEnd(Object obj) {
        RuntimeTrace runtimeTrace = traceMap.get(obj);
        runtimeTrace.end();
        logQueue.add(runtimeTrace);
    }

    public static void drawStart(Object obj) {
        traceMap.put(obj, RuntimeTrace.draw(obj));
    }

    public static void drawEnd(Object obj) {
        RuntimeTrace runtimeTrace = traceMap.get(obj);
        runtimeTrace.end();
        logQueue.add(runtimeTrace);
    }

    public static void log() {
        for (RuntimeTrace runtimeTrace : logQueue) {
            log(runtimeTrace);
        }
        logQueue.clear();
    }

    public static void log(RuntimeTrace runtimeTrace) {
        if (runtimeTrace.type == RuntimeTrace.TYPE_INFLATE) {
            Log.d(LayoutMonitor.TAG, "inflate|" + runtimeTrace.object + "|" + runtimeTrace.startTime + "|" + runtimeTrace.endTime + "|" + runtimeTrace.time());
        } else if (runtimeTrace.type == RuntimeTrace.TYPE_CREATE_VIEW) {
            Log.d(LayoutMonitor.TAG, "createView|" + runtimeTrace.object + "|" + runtimeTrace.startTime + "|" + runtimeTrace.endTime + "|" + runtimeTrace.time());
        } else if (runtimeTrace.type == RuntimeTrace.TYPE_MEASURE) {
            Log.d(LayoutMonitor.TAG, "measure|" + runtimeTrace.object.getClass().getName() + "|" + runtimeTrace.startTime + "|" + runtimeTrace.endTime + "|" + runtimeTrace.time());
        } else if (runtimeTrace.type == RuntimeTrace.TYPE_LAYOUT) {
            Log.d(LayoutMonitor.TAG, "layout|" + runtimeTrace.object.getClass().getName() + "|" + runtimeTrace.startTime + "|" + runtimeTrace.endTime + "|" + runtimeTrace.time());
        } else if (runtimeTrace.type == RuntimeTrace.TYPE_DRAW) {
            Log.d(LayoutMonitor.TAG, "draw|" + runtimeTrace.object.getClass().getName() + "|" + runtimeTrace.startTime + "|" + runtimeTrace.endTime + "|" + runtimeTrace.time());
        }
    }
}
