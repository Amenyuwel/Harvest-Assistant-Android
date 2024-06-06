package com.example.app;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class Activity_Main_Faq extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FAQadapter faqAdapter;
    private List<FAQ> faqList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_faq);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create and populate the FAQ list
        faqList = new ArrayList<>();
        faqList.add(new FAQ("What is your name?", "My name is ChatGPT."));
        faqList.add(new FAQ("What is your purpose?", "I am here to assist you with information and tasks."));
        faqList.add(new FAQ("How do you work?", "I process and generate text based on the input I receive."));

        // Set up the adapter
        faqAdapter = new FAQadapter(faqList);
        recyclerView.setAdapter(faqAdapter);
    }
}

