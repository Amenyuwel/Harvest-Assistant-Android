package com.example.app;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class StageDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage_detail);

        // Set the ActionBar background color
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Set the ActionBar background color
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.matcha)));
            // Remove ActionBar Title
            actionBar.setTitle("");
            // Show the back button in action bar
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ImageView imageViewDetail = findViewById(R.id.imageViewDetail);
        TextView tvStageNameDetail = findViewById(R.id.tvStageNameDetail);
        TextView tvDescriptionDetail = findViewById(R.id.tvDescriptionDetail);
        TextView tvSuggestionsDetail = findViewById(R.id.tvSuggestionsDetail);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String description = intent.getStringExtra("description");
        String suggestions = intent.getStringExtra("suggestions");
        int imageResId = intent.getIntExtra("imageResId", 0);

        tvStageNameDetail.setText(name);
        tvDescriptionDetail.setText(description);
        tvSuggestionsDetail.setText(suggestions);
        imageViewDetail.setImageResource(imageResId);
    }
    // This event will enable the back function to the button on press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


