package com.example.app;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LaninaMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laninamain);

        // To remove ActionBar Title
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        String s3[] = getResources().getStringArray(R.array.PestLanina);
        String s4[] = getResources().getStringArray(R.array.laninadescription);
        int images[] = {R.drawable.slug, R.drawable.snail, R.drawable.calendar_guy};

        MyAdapter2 myAdapter2 = new MyAdapter2(this, s3, s4, images);
        recyclerView.setAdapter(myAdapter2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
