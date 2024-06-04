package com.example.app;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class Calendarclass extends AppCompatActivity {

    ImageButton ivDate, calendarBackButton;
    private TextView tvSelectedDate, tvCrop, tvVariant, tvHectares, tvEstimatedProduce, tvEstimatedIncome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_activity);

        ivDate = findViewById(R.id.ivDate);
        calendarBackButton = findViewById(R.id.calendarBackButton);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        tvCrop = findViewById(R.id.tvCrop);
        tvVariant = findViewById(R.id.tvVariant);
        tvHectares = findViewById(R.id.tvHectares);
        tvEstimatedProduce = findViewById(R.id.tvEstimatedProduce);
        tvEstimatedIncome = findViewById(R.id.tvEstimatedIncome);

        ivDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Calendarclass.this, FormActivity.class);
                startActivity(i);
            }
        });

        calendarBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Calendarclass.this, Dashboard.class);
                startActivity(i);
            }
        });



        Intent intent = getIntent();
        String selectedDate = intent.getStringExtra("selectedDate");
        String crop = intent.getStringExtra("crop");
        String variant = intent.getStringExtra("variant");
        String hectares = intent.getStringExtra("hectares");
        double estimatedProduce = intent.getDoubleExtra("estimatedProduce", 0);
        double estimatedIncome = intent.getDoubleExtra("estimatedIncome", 0);

        tvSelectedDate.setText(selectedDate);
        tvCrop.setText(crop);
        tvVariant.setText(variant);
        tvHectares.setText(hectares);
        tvEstimatedProduce.setText(String.valueOf(estimatedProduce));
        tvEstimatedIncome.setText(String.format("%.2f", estimatedIncome));


    }
}