package com.aem.perfsword;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new LayoutMonitor().apply(this, true);
        setContentView(R.layout.activity_main);
    }
}
