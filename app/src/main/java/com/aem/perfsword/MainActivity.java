package com.aem.perfsword;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aemiot.android.perfsword.LayoutMonitor;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new LayoutMonitor().apply(this, true);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.container);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemViewCacheSize(0);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_test1, null);
                view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
                return new ItemViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 200;
            }
        });
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {

        public ItemViewHolder(View itemView) {
            super(itemView);
        }
    }
}
