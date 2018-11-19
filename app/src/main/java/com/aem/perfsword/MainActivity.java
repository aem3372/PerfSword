package com.aem.perfsword;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aemiot.android.perfsword.LayoutMonitor;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LayoutMonitor.addMapping("android.support.constraint.ConstraintLayout", ConstraintLayout.class);
        LayoutMonitor.addMapping("android.support.v7.widget.RecyclerView", RecyclerView.class);
        LayoutMonitor.preload(this);
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
