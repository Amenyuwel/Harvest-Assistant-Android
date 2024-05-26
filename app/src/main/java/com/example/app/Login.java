package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null){
            actionBar.setTitle("");
        }


        Button loginButton = findViewById(R.id.btnLogin);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Start the Dashboard activity

                Intent intent = new Intent(Login.this, Dashboard.class);
                startActivity(intent);
            }
        });
    }
}
