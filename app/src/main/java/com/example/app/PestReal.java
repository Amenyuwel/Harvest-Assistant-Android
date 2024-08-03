package com.example.app;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

public class PestReal extends AppCompatActivity {

    CardView elNinoCardView;
    CardView laNinaCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_pest_real);

        // Set the ActionBar background color
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Set the ActionBar background color
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.matcha)));
            // Remove ActionBar Title
            actionBar.setTitle("");
        }

        elNinoCardView = findViewById(R.id.elNinoCardview);
        laNinaCardView = findViewById(R.id.laNinaCardview);

        elNinoCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PestReal.this, ElninoMain.class); // Start new activity for El Nino
                startActivity(intent);
            }


        });
        laNinaCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PestReal.this, LaninaMain.class); // Start La Nina activity
                startActivity(intent);
            }
        });
    }
}
