package com.aem.perfsword;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final LayoutInflater.Factory factory = layoutInflater.getFactory();
        final LayoutInflater.Factory2 factory2 = layoutInflater.getFactory2();
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
                Log.e("MainActivity", "Factory:" + name);
                if (name.equals("FrameLayout")) {
                    return MeasureViewGenerator.generatorObject(FrameLayout.class, context, attrs);
                } else if (name.equals("TextView")) {
                    return MeasureViewGenerator.generatorObject(TextView.class, context, attrs);
                }
                return factory.onCreateView(name, context, attrs);
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
                Log.e("MainActivity", "Factor2y:" + name);
                if (name.equals("FrameLayout")) {
                    return MeasureViewGenerator.generatorObject(FrameLayout.class, context, attrs);
                } else if (name.equals("TextView")) {
                    return MeasureViewGenerator.generatorObject(TextView.class, context, attrs);
                }
                return factory2.onCreateView(parent, name, context, attrs);
            }

            @Override
            public View onCreateView(String name, Context context, AttributeSet attrs) {
                Log.e("MainActivity", "Factory2:" + name);
                if (name.equals("FrameLayout")) {
                    return MeasureViewGenerator.generatorObject(FrameLayout.class, context, attrs);
                } else if (name.equals("TextView")) {
                    return MeasureViewGenerator.generatorObject(TextView.class, context, attrs);
                }
                return factory2.onCreateView(name, context, attrs);
            }
        });
        setContentView(R.layout.activity_main);
    }
}
