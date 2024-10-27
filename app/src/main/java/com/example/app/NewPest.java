package com.example.app;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewPest extends AppCompatActivity {

    private static final String URL = "https://harvest.dermocura.net/PHP_API/pestinfo.php";  // Ensure this URL is correct

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pest);

        // Set the ActionBar background color
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.darker_matcha)));
            actionBar.setTitle("");  // Remove ActionBar Title
            actionBar.setDisplayHomeAsUpEnabled(true);  // Show the back button
        }

        // Fetch data from the API using Volley
        fetchPests();
    }

    // Function to fetch pests data using Volley
    private void fetchPests() {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("NewPest", "Fetched data: " + response.toString());
                        List<Pest> pestList = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject pestObject = response.getJSONObject(i);
                                Pest pest = new Pest();
                                pest.setPestName(pestObject.getString("pest_name"));
                                pest.setPestDescription(pestObject.getString("pest_desc"));
                                pest.setRecommendation(pestObject.getString("pest_reco"));
                                pest.setActiveMonth(pestObject.getString("active_month"));
                                pest.setSeason(pestObject.getString("season"));
                                pest.setPestImg(pestObject.getString("pest_img"));  // Set the image URL

                                pestList.add(pest);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        populatePests(pestList);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("NewPest", "Failed to fetch pests", error);
                    }
                }
        );

        queue.add(jsonArrayRequest);
    }

    // Function to dynamically create CardViews for each pest
    private void populatePests(List<Pest> pestList) {
        LinearLayout pestContainer = findViewById(R.id.pest_container);
        if (pestContainer == null) {
            Log.e("NewPest", "pest_container not found!");
            return;
        }

        for (Pest pest : pestList) {
            // Create a new CardView
            CardView cardView = new CardView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(16, 16, 16, 16);
            cardView.setLayoutParams(layoutParams);

            // Create a vertical LinearLayout for TextView and ImageView
            LinearLayout cardLayout = new LinearLayout(this);
            cardLayout.setOrientation(LinearLayout.VERTICAL);

            // Create a TextView for the pest details
            TextView textView = new TextView(this);
            textView.setText(
                    "Pest Name: " + pest.getPestName() + "\n" +
                            "Description: " + pest.getPestDescription() + "\n" +
                            "Recommendation: " + pest.getRecommendation() + "\n" +
                            "Active Month: " + pest.getActiveMonth() + "\n" +
                            "Season: " + pest.getSeason()
            );
            textView.setPadding(16, 16, 16, 16);
            textView.setTextSize(18);

            // Create an ImageView for displaying the pest image
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    400
            ));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            // Load the image using Glide
            Glide.with(this)
                    .load(pest.getPestImg())  // Load image from URL
                    .placeholder(R.drawable.placeholder)  // Placeholder image
                    .error(R.drawable.error)  // Error image
                    .into(imageView);

            // Add TextView and ImageView to card layout
            cardLayout.addView(textView);
            cardLayout.addView(imageView);

            // Add the LinearLayout to the CardView
            cardView.addView(cardLayout);

            // Add the CardView to the LinearLayout container
            pestContainer.addView(cardView);
        }
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
