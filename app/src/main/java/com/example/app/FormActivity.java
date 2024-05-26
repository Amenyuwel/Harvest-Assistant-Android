package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class FormActivity extends AppCompatActivity {


    private CalendarView calendarView;
    private Spinner spinnerCrops, spinnerVariants;
    private EditText editTextHectares;
    private Button submitButton;
    private String selectedDate;
    private static final double PRODUCE_PER_HECTARE = 5; // in tons
    private static final double INCOME_PER_TON = 40500; // in PHP (40.50 PHP/kg * 1000 kg)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        calendarView = findViewById(R.id.calendarView);
        spinnerCrops = findViewById(R.id.spinnerCrops);
        spinnerVariants = findViewById(R.id.spinnerVariants);
        editTextHectares = findViewById(R.id.editTextHectares);
        submitButton = findViewById(R.id.submitButton);



        // Populate spinnerCrops
        ArrayAdapter<CharSequence> cropsAdapter = ArrayAdapter.createFromResource(this,
                R.array.crops_array, android.R.layout.simple_spinner_item);
        cropsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCrops.setAdapter(cropsAdapter);

        // Populate spinnerVariants
        ArrayAdapter<CharSequence> variantsAdapter = ArrayAdapter.createFromResource(this,
                R.array.variants_array, android.R.layout.simple_spinner_item);
        variantsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVariants.setAdapter(variantsAdapter);

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
        });

        submitButton.setOnClickListener(v -> {
            String crop = spinnerCrops.getSelectedItem().toString();
            String variant = spinnerVariants.getSelectedItem().toString();
            String hectaresStr = editTextHectares.getText().toString();
            double hectares = Double.parseDouble(hectaresStr);
            double estimatedProduce = hectares * PRODUCE_PER_HECTARE;
            double estimatedIncome = estimatedProduce * INCOME_PER_TON;

            Intent intent = new Intent(FormActivity.this, Calendarclass.class);
            intent.putExtra("selectedDate", selectedDate);
            intent.putExtra("crop", crop);
            intent.putExtra("variant", variant);
            intent.putExtra("hectares", hectaresStr);
            intent.putExtra("estimatedProduce", estimatedProduce);
            intent.putExtra("estimatedIncome", estimatedIncome);
            startActivity(intent);
        });
    }
}