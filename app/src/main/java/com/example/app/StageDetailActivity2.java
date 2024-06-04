package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class StageDetailActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
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
}