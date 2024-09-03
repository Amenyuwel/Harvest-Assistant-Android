package com.example.app;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class RecoMainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommendations_layout);


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
    }


    // this event will enable the back function to the button on press
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
