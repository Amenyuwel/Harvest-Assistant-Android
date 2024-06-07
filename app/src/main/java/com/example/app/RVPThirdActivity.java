package com.example.app;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RVPThirdActivity extends AppCompatActivity {

    ImageView mainImageView;
    TextView title, description;

    String data3, data4;
    int myImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rvpthird);

        mainImageView = findViewById(R.id.mainImageView2);
        title = findViewById(R.id.laninatitle);
        description = findViewById(R.id.laninadescription);

        getData();
        setData();
    }

    private void getData() {
        if (getIntent().hasExtra("myImage") && getIntent().hasExtra("data3") && getIntent().hasExtra("data4")) {
            data3 = getIntent().getStringExtra("data3");
            data4 = getIntent().getStringExtra("data4");
            myImage = getIntent().getIntExtra("myImage", 1);
        } else {
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        }
    }

    private void setData() {
        title.setText(data3);
        description.setText(data4);
        mainImageView.setImageResource(myImage);
    }
}
