package com.example.app;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class laNina extends AppCompatActivity {

    CardView ricecv1, ricecv2, ricecv3, ricecv4;
    CardView corncv1, corncv2, corncv3;
    Dialog dialog1, dialog2, dialog3, dialog4, dialog5, dialog6, dialog7;
    ImageButton ricebtn1, ricebtn2, ricebtn01, ricebtn04;
    ImageButton cornbtn1, cornbtn02, cornbtn03;
    ImageButton btBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_la_nina);

        initializeViews();
        initializeDialogs();
        setupClickListeners();
    }

    // Method to initialize views to reduce repetition
    private void initializeViews() {
        ricecv1 = findViewById(R.id.ricecv1);
        ricecv2 = findViewById(R.id.ricecv2);
        ricecv3 = findViewById(R.id.ricecv3);
        ricecv4 = findViewById(R.id.ricecv4);
        corncv1 = findViewById(R.id.corncv1);
        corncv2 = findViewById(R.id.corncv2);
        corncv3 = findViewById(R.id.corncv3);
        btBack = findViewById(R.id.btBack);
    }

    // Method to initialize all dialog instances
    private void initializeDialogs() {
        dialog1 = createDialog(R.layout.planthoper_dbox);
        dialog2 = createDialog(R.layout.greenplanthoper_dbox);
        dialog3 = createDialog(R.layout.riceblackbug_dbox);
        dialog4 = createDialog(R.layout.rice_stem_borer_dbox);
        dialog5 = createDialog(R.layout.fallarmyworm_dbox);
        dialog6 = createDialog(R.layout.cornearworm_dbox);
        dialog7 = createDialog(R.layout.asiaticcornborer_dbox);
    }

    // Method to create dialog instances to avoid redundancy
    private Dialog createDialog(int layoutResId) {
        Dialog dialog = new Dialog(laNina.this);
        dialog.setContentView(layoutResId);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_bg));
        dialog.setCancelable(false);
        return dialog;
    }

    // Method to set up click listeners for views
    private void setupClickListeners() {
        btBack.setOnClickListener(view -> {
            Intent intent = new Intent(laNina.this, PestReal.class);
            startActivity(intent);
        });

        // Set up CardView click listeners
        View.OnClickListener showDialogListener = view -> {
            int id = view.getId();
            if (id == R.id.ricecv1) {
                dialog1.show();
            } else if (id == R.id.ricecv2) {
                dialog2.show();
            } else if (id == R.id.ricecv3) {
                dialog3.show();
            } else if (id == R.id.ricecv4) {
                dialog4.show();
            } else if (id == R.id.corncv1) {
                dialog5.show();
            } else if (id == R.id.corncv2) {
                dialog6.show();
            } else if (id == R.id.corncv3) {
                dialog7.show();
            }
        };

        ricecv1.setOnClickListener(showDialogListener);
        ricecv2.setOnClickListener(showDialogListener);
        ricecv3.setOnClickListener(showDialogListener);
        ricecv4.setOnClickListener(showDialogListener);
        corncv1.setOnClickListener(showDialogListener);
        corncv2.setOnClickListener(showDialogListener);
        corncv3.setOnClickListener(showDialogListener);

        // Set up button click listeners to restart the activity
        View.OnClickListener restartActivityListener = view -> {
            Intent intent = new Intent(laNina.this, laNina.class);
            startActivity(intent);
        };

        ricebtn1 = dialog4.findViewById(R.id.ricebtn1);
        ricebtn2 = dialog1.findViewById(R.id.ricebtn2);
        ricebtn01 = dialog2.findViewById(R.id.ricebtn01);
        ricebtn04 = dialog3.findViewById(R.id.ricebtn04);
        cornbtn1 = dialog5.findViewById(R.id.cornbtn1);
        cornbtn02 = dialog6.findViewById(R.id.cornbtn02);
        cornbtn03 = dialog7.findViewById(R.id.cornbtn03);

        ricebtn1.setOnClickListener(restartActivityListener);
        ricebtn2.setOnClickListener(restartActivityListener);
        ricebtn01.setOnClickListener(restartActivityListener);
        ricebtn04.setOnClickListener(restartActivityListener);
        cornbtn1.setOnClickListener(restartActivityListener);
        cornbtn02.setOnClickListener(restartActivityListener);
        cornbtn03.setOnClickListener(restartActivityListener);
    }
}
