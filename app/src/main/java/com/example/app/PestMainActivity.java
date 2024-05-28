package com.example.app;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PestMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pest_layout);

        // To remove ActionBar Title
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        String s1[] = getResources().getStringArray(R.array.Pest);
        String s2[] = getResources().getStringArray(R.array.description);
        int images[] = {R.drawable.snail_2, R.drawable.weevil_2, R.drawable.planthopper,
                R.drawable.leafhopper, R.drawable.earworm, R.drawable.fallarmyworm,
                R.drawable.cornborer, R.drawable.lanina, R.drawable.blackbug,
                R.drawable.freenleafhopper, R.drawable.riceblast, R.drawable.asiancornborer,
                R.drawable.downymildew, R.drawable.cutworm};

        MyAdapter myAdapter = new MyAdapter(this, s1, s2, images);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
