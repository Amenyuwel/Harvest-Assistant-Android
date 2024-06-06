package com.example.app;

import android.os.Bundle;
import android.widget.ExpandableListView;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivityFAQ extends AppCompatActivity {

    private ExpandableListView expandableListView;
    private FAQAdapter faqAdapter;
    private List<String> listDataHeader;
    private HashMap<String, String> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_faq);

        expandableListView = findViewById(R.id.expandableListView);

        // Preparing the data
        prepareListData();

        faqAdapter = new FAQAdapter(this, listDataHeader, listDataChild);
        expandableListView.setAdapter(faqAdapter);
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding header data
        listDataHeader.add("What is your name?");
        listDataHeader.add("asdasdasdasdas?");
        listDataHeader.add("asdasdsdsddddddddddddas?");

        // Adding child data
        listDataChild.put(listDataHeader.get(0), "My name is Zhak.");
        listDataChild.put(listDataHeader.get(1), "sadasdasdasdsadasdk.");
        listDataChild.put(listDataHeader.get(2), "Myasdasdask.");
    }
}
