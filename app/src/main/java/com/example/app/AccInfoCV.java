package com.example.app;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class AccInfoCV extends AppCompatActivity {

    TextView fullname, rsbsanum, sex, contact_num, barangay, crop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_informationcv);

        // Customize StatusBar and ActionBar
        customizeUI();

        // Initialize TextViews
        initializeTextViews();

        // Fetch Farmer Data
        fetchFarmerData();
    }

    private void customizeUI() {
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.darker_matcha));

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.darker_matcha)));
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initializeTextViews() {
        fullname = findViewById(R.id.fullname);
        rsbsanum = findViewById(R.id.rsbsanum);
        sex = findViewById(R.id.sex);
        contact_num = findViewById(R.id.contact_num);
        barangay = findViewById(R.id.barangay);
        crop = findViewById(R.id.crop);
    }

    private void fetchFarmerData() {
        int farmerID = SharedPreferenceManager.getInstance(this).getFarmerID();
        if (farmerID == -1) {
            Toast.makeText(AccInfoCV.this, "Farmer ID not found. Please log in again.", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://harvest.dermocura.net/PHP_API/profile.php?id=" + farmerID;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("error")) {
                                String errorMessage = response.getString("error");
                                Toast.makeText(AccInfoCV.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                            } else {
                                // Extract data from JSON response
                                String fullnameResponse = response.optString("fullname", "N/A");
                                String rsbsaNumResponse = response.optString("rsbsa_num", "N/A");
                                String sexResponse = response.optString("sex", "N/A");
                                String contactNumberResponse = response.optString("contact_number", "N/A");
                                String barangayResponse = response.optString("barangay", "N/A");
                                String cropResponse = response.optString("crop", "N/A");

                                // Update the UI elements with the fetched data
                                fullname.setText(fullnameResponse);
                                rsbsanum.setText(rsbsaNumResponse);
                                sex.setText(sexResponse);
                                contact_num.setText(contactNumberResponse);
                                barangay.setText(barangayResponse);
                                crop.setText(cropResponse);

                                // Save rsbsa_num to shared preferences if needed
                                SharedPreferenceManager.getInstance(AccInfoCV.this).saveRSBSANum(rsbsaNumResponse);
                            }
                        } catch (JSONException e) {
                            Log.e("AccInfoCV", "JSON Parsing Error: " + e.getMessage());
                            Toast.makeText(AccInfoCV.this, "Parsing error, please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("AccInfoCV", "Volley Error: " + error.getMessage());
                Toast.makeText(AccInfoCV.this, "Failed to fetch data, please check your connection.", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}