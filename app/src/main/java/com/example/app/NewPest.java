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
            actionBar.setTitle("PEST INFORMATION");  // Remove ActionBar Title
            actionBar.setDisplayHomeAsUpEnabled(true);  // Show the back button
        }

        // Get the pest type from the Intent
        String pestType = getIntent().getStringExtra("pest_type");

        // Fetch data and pass the pest type
        fetchPests(pestType);
    }

    private void fetchPests(String pestType) {
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
                                pest.setPestImg(pestObject.getString("pest_img"));

                                pestList.add(pest);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        populatePests(pestList, pestType);
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

    private void populatePests(List<Pest> pestList, String pestType) {
        LinearLayout pestContainer = findViewById(R.id.pest_container);
        if (pestContainer == null) {
            Log.e("NewPest", "pest_container not found!");
            return;
        }

        for (Pest pest : pestList) {
            // Check if the pest type is provided
            boolean shouldDisplay = (pestType == null || pestType.isEmpty() || pest.getPestName().equalsIgnoreCase(pestType));

            // Create a CardView for the pest if it should be displayed
            if (shouldDisplay) {
                CardView cardView = new CardView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                layoutParams.setMargins(16, 16, 16, 16);
                cardView.setLayoutParams(layoutParams);

                LinearLayout cardLayout = new LinearLayout(this);
                cardLayout.setOrientation(LinearLayout.VERTICAL);

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

                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        400
                ));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                Glide.with(this)
                        .load(pest.getPestImg())
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.error)
                        .into(imageView);

                cardLayout.addView(textView);
                cardLayout.addView(imageView);

                cardView.addView(cardLayout);

                pestContainer.addView(cardView);
            }
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
