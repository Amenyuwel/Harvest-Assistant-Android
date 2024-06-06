package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class PestReal extends AppCompatActivity {

    CardView elNinoCardView;
    CardView laNinaCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pest_real);

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
