package com.example.app;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.example.app.bayotemman.FAQAdapter;
import com.example.app.bayotemman.FAQ;

public class BayotEmmanFAQ extends AppCompatActivity {

    private static final String TAG = "BayotEmmanFAQ";
    private RecyclerView recyclerViewFAQ;
    private FAQAdapter faqAdapter;
    private List<FAQ> faqList;
    private List<FAQ> filteredFaqList;  // Separate list for filtered data
    private SearchView searchView;
    private static final String URL = "https://harvest.dermocura.net/PHP_API/FAQYOUEMMAN.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Activity Created");
        setContentView(R.layout.activity_bayot_emman_faq);

        // Initialize views
        recyclerViewFAQ = findViewById(R.id.recyclerViewFAQ);
        recyclerViewFAQ.setLayoutManager(new LinearLayoutManager(this));
        searchView = findViewById(R.id.searchView);

        // Initially, we disable the SearchView functionality until the data is fetched
        searchView.setEnabled(false);

        // Initialize lists
        faqList = new ArrayList<>();
        filteredFaqList = new ArrayList<>();

        // Fetch the FAQ data from the server
        Log.d(TAG, "onCreate: Calling makeHTTPRequest to fetch data");
        makeHTTPRequest();
    }

    // Method to fetch FAQ data from backend
    private void makeHTTPRequest() {
        RequestQueue queue = Volley.newRequestQueue(this);
        Log.d(TAG, "makeHTTPRequest: Sending GET request to URL: " + URL);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                response -> {
                    Log.d(TAG, "makeHTTPRequest: Response received, processing response");
                    handleResponse(response);
                },
                error -> {
                    Log.e(TAG, "makeHTTPRequest: Error occurred during request", error);
                    onRequestError(error);
                }
        );

        queue.add(request);
        Log.d(TAG, "makeHTTPRequest: Request added to queue");
    }

    // Method to handle successful response
    private void handleResponse(JSONArray response) {
        Log.d(TAG, "handleResponse: Handling JSON response");
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject faqObject = response.getJSONObject(i);
                String question = faqObject.getString("question");
                String answer = faqObject.getString("answer");

                // Log the FAQ data
                Log.d(TAG, "handleResponse: FAQ fetched - Question: " + question + ", Answer: " + answer);

                // Add FAQ to the list
                faqList.add(new FAQ(question, answer));
            }

            // Copy faqList to filteredFaqList initially
            filteredFaqList.addAll(faqList);

            // Set the adapter with the fetched FAQs
            faqAdapter = new FAQAdapter(this, filteredFaqList);
            recyclerViewFAQ.setAdapter(faqAdapter);

            // Enable SearchView and set up listener now that data is loaded
            enableSearch();

            Log.d(TAG, "handleResponse: FAQs successfully processed and displayed");

        } catch (JSONException e) {
            Log.e(TAG, "handleResponse: Error parsing JSON response", e);
        }
    }

    // Method to enable search functionality
    private void enableSearch() {
        Log.d(TAG, "enableSearch: Enabling SearchView");
        searchView.setEnabled(true);  // Enable the SearchView once data is loaded
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "enableSearch: Query submitted: " + query);
                filterQuestions(query);  // Filter questions based on search
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "enableSearch: Query text changed: " + newText);
                filterQuestions(newText);  // Filter as user types
                return false;
            }
        });
    }

    // Method to filter questions based on user input
    private void filterQuestions(String query) {
        Log.d(TAG, "filterQuestions: Filtering questions with query: " + query);
        filteredFaqList.clear();  // Clear the filtered list

        if (TextUtils.isEmpty(query)) {
            Log.d(TAG, "filterQuestions: Query is empty, showing all FAQs");
            // If query is empty, show all questions
            filteredFaqList.addAll(faqList);
        } else {
            // Search for questions containing the query (case-insensitive)
            String lowerCaseQuery = query.toLowerCase();

            for (FAQ faq : faqList) {
                if (faq.getQuestion().toLowerCase().contains(lowerCaseQuery)) {
                    Log.d(TAG, "filterQuestions: Matching question found: " + faq.getQuestion());
                    filteredFaqList.add(faq);  // Add matching question to filtered list
                }
            }
        }

        // Notify adapter of changes
        faqAdapter.notifyDataSetChanged();
        Log.d(TAG, "filterQuestions: Filtered list updated, adapter notified");
    }

    // Method to handle request errors
    private void onRequestError(VolleyError error) {
        Log.e(TAG, "onRequestError: Error fetching FAQs", error);
        Toast.makeText(this, "Error fetching FAQs", Toast.LENGTH_SHORT).show();
    }
}
