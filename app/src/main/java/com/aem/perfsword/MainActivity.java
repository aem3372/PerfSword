package com.aem.perfsword;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.AsyncLayoutInflater;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;

import com.aemiot.android.perfsword.LayoutMonitor;
import com.aemiot.android.perfsword.async.ViewLayoutUtils;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LayoutMonitor.addMapping("android.support.constraint.ConstraintLayout", ConstraintLayout.class);
        LayoutMonitor.addMapping("android.support.v7.widget.RecyclerView", RecyclerView.class);
        LayoutMonitor.preload(this);

        new AsyncLayoutInflater(getApplicationContext()).inflate(R.layout.item_test_2, null, new AsyncLayoutInflater.OnInflateFinishedListener() {
            @Override
            public void onInflateFinished(@NonNull final View view, int resid, @Nullable ViewGroup parent) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        Resources resources = getApplication().getResources();
                        DisplayMetrics dm = resources.getDisplayMetrics();
                        final int width = dm.widthPixels;
                        final int height = dm.heightPixels;
                        long startTime1 = SystemClock.elapsedRealtime();
                        view.measure(
                                View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                                View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.AT_MOST));
                        Log.e("ViewLayoutUtils", "measure1:" + (SystemClock.elapsedRealtime() - startTime1));
                        ViewLayoutUtils.printLayoutParams(view, null);
                        ViewLayoutUtils.replaceLayoutParams(view, new HashMap<View, Pair<Integer, Integer>>());
                        ViewLayoutUtils.printLayoutParams(view, null);
                        startTime1 = SystemClock.elapsedRealtime();
                        view.measure(
                                View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                                View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.AT_MOST));
                        Log.e("ViewLayoutUtils", "measure2:" + (SystemClock.elapsedRealtime() - startTime1));
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                ViewGroup viewGroup = findViewById(R.id.user_root);
                                viewGroup.addView(view);
                            }
                        });
                    }
                });
            }
        });
    }

    /**
     * 用于测试多次measure，Cache是否生效
     * */
    private void test(final View view, final int width, final int height) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                long startTime2 = SystemClock.elapsedRealtime();
                view.measure(
                        View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.AT_MOST));
                Log.e("ViewLayoutUtils", "measure2:" + (SystemClock.elapsedRealtime() - startTime2));
                test(view, width, height);
            }
        }, 5000L);
    }

    public void listEmpty(View view) {
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("layoutResId", R.layout.item_empty);
        startActivity(intent);
    }

    public void list(View view) {
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }

    public void list2(View view) {
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("layoutResId", R.layout.item_test_2);
        startActivity(intent);
    }
}
