package com.aemiot.android.perfsword;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatMultiAutoCompleteTextView;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.FrameMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.Switch;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LayoutMonitor {
    public static final String TAG = "LayoutMonitor";

    private static Map<String, Class> mMap = new HashMap<>();
    private static Set<String> mBlackSet = new HashSet<>();

    public static void addMapping(String key, Class clz) {
        mMap.put(key, clz);
    }

    static {
        mMap.put("FrameLayout", FrameLayout.class);
        mMap.put("RelativeLayout", RelativeLayout.class);
        mMap.put("LinearLayout", LinearLayout.class);
        mMap.put("ListView", ListView.class);
        mMap.put("GridView", GridView.class);
        mMap.put("TextView", AppCompatTextView.class);
        mMap.put("EditText", AppCompatEditText.class);
        mMap.put("ImageView", AppCompatImageView.class);
        mMap.put("ImageButton", AppCompatImageButton.class);
        mMap.put("Button", AppCompatButton.class);
        mMap.put("Spinner", AppCompatSpinner.class);
        mMap.put("RadioButton", AppCompatRadioButton.class);
        mMap.put("CheckedTextView", AppCompatCheckedTextView.class);
        mMap.put("AutoCompleteTextView", AppCompatAutoCompleteTextView.class);
        mMap.put("MultiAutoCompleteTextView", AppCompatMultiAutoCompleteTextView.class);
        mMap.put("RatingBar", AppCompatRatingBar.class);
        mMap.put("SeekBar", AppCompatSeekBar.class);
        mMap.put("ScrollView", ScrollView.class);
        mMap.put("Space", Space.class);
        mMap.put("Switch", Switch.class);
        mMap.put("CheckBox", AppCompatCheckBox.class);
        mMap.put("ViewStub", ViewStub.class);
        mMap.put("View", View.class);

        mBlackSet.add("ViewStub");
        mBlackSet.add("Space");
    }

    public LayoutMonitor() {

    }

    public static void preload(Context context) {
        long startTime = System.nanoTime();
        for (Map.Entry<String, Class> entry : mMap.entrySet()) {
            if (!mBlackSet.contains(entry.getKey())) {
                MeasureViewGenerator.generatorObject(entry.getValue(), context, null);
            }
        }
        Log.d(TAG, "preload|" + (System.nanoTime() - startTime));
    }

    private View measureView(String name, Context context, AttributeSet attrs) {
        Monitor.createViewStart(name);
        Class clz = mMap.get(name);
        if (clz == null) {
            try {
                clz = Class.forName(name);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (clz != null) {
            if (mBlackSet.contains(name)) {
                Constructor constructor = null;
                try {
                    constructor = clz.getConstructor(Context.class, AttributeSet.class);
                    return (View) constructor.newInstance(context, attrs);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            View view = MeasureViewGenerator.generatorObject(clz, context, attrs);
            Monitor.createViewEnd(name);
            return view;
        } else {
            throw new RuntimeException("not find class");
        }
    }

    private static class MonitorLayoutInflater extends LayoutInflater {

        protected MonitorLayoutInflater(Context context) {
            super(context);
        }

        @Override
        public LayoutInflater cloneInContext(Context newContext) {
            return new MonitorLayoutInflater(newContext);
        }

        @Override
        public View inflate(int resource, @Nullable ViewGroup root) {
            return super.inflate(resource, root);
        }

        @Override
        public View inflate(XmlPullParser parser, @Nullable ViewGroup root) {
            return super.inflate(parser, root);
        }

        @Override
        public View inflate(int resource, @Nullable ViewGroup root, boolean attachToRoot) {
            String name = getContext().getResources().getResourceName(resource);
            Monitor.inflateStart(name);
            View view = super.inflate(resource, root, attachToRoot);
            Monitor.inflateEnd(name);
            return view;
        }

        @Override
        public View inflate(XmlPullParser parser, @Nullable ViewGroup root, boolean attachToRoot) {
            return super.inflate(parser, root, attachToRoot);
        }
    }

    public void apply(AppCompatActivity activity, boolean replace) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            activity.getWindow().addOnFrameMetricsAvailableListener(new Window.OnFrameMetricsAvailableListener() {
                @Override
                public void onFrameMetricsAvailable(Window window, FrameMetrics frameMetrics, int dropCountSinceLastInvocation) {
                    Log.e(TAG, frameMetrics.)
                }
            }, new Handler(Looper.getMainLooper()));
        }

        long startTime = System.nanoTime();
        LayoutInflater layoutInflater;
        if (replace) {
            layoutInflater = new MonitorLayoutInflater(activity);
            try {
                ContextThemeWrapper contextThemeWrapper = activity;
                Field field = ContextThemeWrapper.class.getDeclaredField("mInflater");
                field.setAccessible(true);
                field.set(contextThemeWrapper, layoutInflater);

            } catch (NoSuchFieldException e) {
                throw new RuntimeException("Stub");
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Stub");
            }
        } else {
            layoutInflater = LayoutInflater.from(activity);
        }
        apply(layoutInflater);
        Log.d(TAG, "apply|" + (System.nanoTime() - startTime));
    }

    public void apply(LayoutInflater layoutInflater) {
        try {
            Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
            field.setAccessible(true);
            field.setBoolean(layoutInflater, false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        layoutInflater.setFactory(new LayoutInflater.Factory() {
            @Override
            public View onCreateView(String name, Context context, AttributeSet attrs) {
                return measureView(name, context, attrs);
            }
        });
        try {
            Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
            field.setAccessible(true);
            field.setBoolean(layoutInflater, false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        layoutInflater.setFactory2(new LayoutInflater.Factory2() {

            @Override
            public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
                return measureView(name, context, attrs);
            }

            @Override
            public View onCreateView(String name, Context context, AttributeSet attrs) {
                return measureView(name, context, attrs);
            }
        });
    }
}
