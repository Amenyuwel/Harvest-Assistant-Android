package com.example.app;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

public class PestReal extends AppCompatActivity {

    CardView elNinoCardView;
    CardView laNinaCardView;
    ImageView btBack1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pest_real);

        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.darker_matcha));

        // Set the ActionBar background color
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Set the ActionBar background color
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.darker_matcha)));
            // Remove ActionBar Title
            actionBar.setTitle("");
            // Show the back button in action bar
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        elNinoCardView = findViewById(R.id.elNinoCardview);
        laNinaCardView = findViewById(R.id.laNinaCardview);
        btBack1 = findViewById(R.id.btBack1);

        elNinoCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PestReal.this, elNino.class); // Start new activity for El Nino
                startActivity(intent);
            }


        });
        laNinaCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PestReal.this, laNina.class); // Start La Nina activity
                startActivity(intent);
            }

        });

        btBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PestReal.this, Dashboard.class);
                startActivity(i);
            }
        });

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

