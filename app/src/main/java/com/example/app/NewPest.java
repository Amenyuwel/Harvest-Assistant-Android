package com.example.app;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewPest extends AppCompatActivity {

    private static final String URL = "https://harvest.dermocura.net/PHP_API/pestinfo.php";  // Update with your server's URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pest);

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
                        List<Pest> pestList = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject pestObject = response.getJSONObject(i);
                                Pest pest = new Pest();
                                pest.setPestName(pestObject.getString("pest_name"));
                                pest.setPestDescription(pestObject.getString("pest_description"));
                                pest.setRecommendation(pestObject.getString("recommendation"));
                                pest.setActiveMonth(pestObject.getString("active_month"));
                                pest.setSeason(pestObject.getString("season"));

                                pestList.add(pest);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        // Populate the UI with the fetched pest data
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

        // Add the request to the RequestQueue
        queue.add(jsonArrayRequest);
    }

    // Function to dynamically create CardViews for each pest
    private void populatePests(List<Pest> pestList) {
        LinearLayout pestContainer = findViewById(R.id.pest_container);

        for (Pest pest : pestList) {
            // Create a new CardView
            CardView cardView = new CardView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(16, 16, 16, 16);
            cardView.setLayoutParams(layoutParams);

            // Create a TextView for the CardView content
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

            // Add the TextView to the CardView
            cardView.addView(textView);

            // Add the CardView to the LinearLayout container
            pestContainer.addView(cardView);
        }
    }
}
