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

        ivDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Calendarclass.this, FormActivity.class);
                startActivity(i);
            }
        });

        // Retrieve data from the intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String selectedDate = extras.getString("selectedDate");
            String crop = extras.getString("crop");
            String variant = extras.getString("variant");
            String hectares = extras.getString("hectares");
            // Calculate estimated produce per sack and estimated income per sack
            double estimatedProducePerSack = Double.parseDouble(hectares) * 5; // Assume 5 tons per hectare
            double estimatedIncomePerSack = estimatedProducePerSack * 40.50; // 40.50 pesos per kilogram

            // Update TextViews with the retrieved data
            TextView tvSelectedDate = findViewById(R.id.tvSelectedDate);
            tvSelectedDate.setText(selectedDate);

            TextView tvCrop = findViewById(R.id.tvCrop);
            tvCrop.setText(crop);

            TextView tvVariant = findViewById(R.id.tvVariant);
            tvVariant.setText(variant);

            TextView tvHectares = findViewById(R.id.tvHectares);
            tvHectares.setText(hectares);

            TextView tvEstimatedProduce = findViewById(R.id.tvEstimatedProduce);
            tvEstimatedProduce.setText(String.format("%.2f tons", estimatedProducePerSack));

            TextView tvEstimatedIncome = findViewById(R.id.tvEstimatedIncome);
            tvEstimatedIncome.setText(String.format("%.2f PHP", estimatedIncomePerSack));
        }
    }
}