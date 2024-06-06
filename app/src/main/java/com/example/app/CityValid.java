package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CityValid extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_valid);

        // Initialize the "GO BACK" button
        Button backButton = findViewById(R.id.backToLogin);

        // Set OnClickListener for the button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define the action to be performed when the button is clicked
                // For example, navigating back to the login screen
                Intent intent = new Intent(CityValid.this, Login.class);
                startActivity(intent);
                finish(); // Optional: finish this activity to prevent it from appearing in the back stack
            }
        });
    }
}
