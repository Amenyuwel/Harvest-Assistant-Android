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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

        ricecv1 = findViewById(R.id.ricecv1);
        ricecv2 = findViewById(R.id.ricecv2);
        ricecv3 = findViewById(R.id.ricecv3);
        ricecv4 = findViewById(R.id.ricecv4);
        corncv1 = findViewById(R.id.corncv1);
        corncv2 = findViewById(R.id.corncv2);
        corncv3 = findViewById(R.id.corncv3);

        btBack = findViewById(R.id.btBack);

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent (laNina.this, PestReal.class);
                startActivity(i);
            }
        });

        dialog1= new Dialog(laNina.this);
        dialog1.setContentView(R.layout.planthoper_dbox);
        dialog1.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog1.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_bg));
        dialog1.setCancelable(false);

        dialog2= new Dialog(laNina.this);
        dialog2.setContentView(R.layout.greenplanthoper_dbox);
        dialog2.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog2.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_bg));
        dialog2.setCancelable(false);

        dialog3= new Dialog(laNina.this);
        dialog3.setContentView(R.layout.riceblackbug_dbox);
        dialog3.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog3.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_bg));
        dialog3.setCancelable(false);

        dialog4= new Dialog(laNina.this);
        dialog4.setContentView(R.layout.rice_stem_borer_dbox);
        dialog4.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog4.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_bg));
        dialog4.setCancelable(false);

        dialog5= new Dialog(laNina.this);
        dialog5.setContentView(R.layout.fallarmyworm_dbox);
        dialog5.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog5.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_bg));
        dialog5.setCancelable(false);

        dialog6= new Dialog(laNina.this);
        dialog6.setContentView(R.layout.cornearworm_dbox);
        dialog6.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog6.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_bg));
        dialog6.setCancelable(false);

        dialog7= new Dialog(laNina.this);
        dialog7.setContentView(R.layout.asiaticcornborer_dbox);
        dialog7.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog7.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_bg));
        dialog7.setCancelable(false);

        ricebtn1 = dialog4.findViewById(R.id.ricebtn1);
        ricebtn2 = dialog1.findViewById(R.id.ricebtn2);
        ricebtn01 = dialog2.findViewById(R.id.ricebtn01);
        ricebtn04 = dialog3.findViewById(R.id.ricebtn04);

        cornbtn1 = dialog5.findViewById(R.id.cornbtn1);
        cornbtn02 = dialog6.findViewById(R.id.cornbtn02);
        cornbtn03 = dialog7.findViewById(R.id.cornbtn03);


        ricebtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(laNina.this, laNina.class);
                startActivity(i);
            }
        });

        ricecv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.show();
            }
        });

        ricebtn01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(laNina.this, laNina.class);
                startActivity(i);
            }
        });

        ricecv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2.show();
            }
        });

        ricebtn04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(laNina.this, laNina.class);
                startActivity(i);
            }
        });

        ricecv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog3.show();
            }
        });

        ricebtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(laNina.this, laNina.class);
                startActivity(i);
            }
        });

        ricecv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog4.show();
            }
        });

        cornbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(laNina.this, laNina.class);
                startActivity(i);
            }
        });

        corncv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog5.show();
            }
        });

        cornbtn02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(laNina.this, laNina.class);
                startActivity(i);
            }
        });

        corncv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog6.show();
            }
        });

        cornbtn03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(laNina.this, laNina.class);
                startActivity(i);
            }
        });

        corncv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog7.show();
            }
        });


    }
}