package com.example.app;

import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;
public class ChatbotMainActivity extends AppCompatActivity{
    RecyclerView recyclerView;
    TextView welcomeTextView;
    EditText messageEditText;
    ImageButton sendButton;
    List<Message_repo> messageList;
    MessageAdapter_repo messageAdapterRepo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatbot_repolayout);
        messageList = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler_view);
        welcomeTextView = findViewById(R.id.welcome_text);
        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_btn);

        //setup recycler view
        messageAdapterRepo = new MessageAdapter_repo(messageList);
        recyclerView.setAdapter(messageAdapterRepo);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);

        sendButton.setOnClickListener((v)->{
            String question = messageEditText.getText().toString().trim();
            addToChat(question,Message_repo.SENT_BY_ME);
            messageEditText.setText("");
            welcomeTextView.setVisibility(View.GONE);
        });

        // To remove ActionBar Title
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("");
        }
    }

    void addToChat(String message, String sentBy) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(new Message_repo(message, sentBy));
                messageAdapterRepo.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(messageAdapterRepo.getItemCount());
                botreply(message, Message_repo.SENT_BY_BOT);
            }
        });
    }

    void botreply(String message, String sentBy) {
        String bot_response = " ";
        ArrayList<String> words = new ArrayList<String>();
        if (message.contains("drought resistant corn")) {
            bot_response = "Researchers are exploring several strategies for drought resistance in corn. " +
                    "Some varieties have deeper root systems to access more water from the soil. " +
                    "Others have genes that allow them to tolerate drier conditions by closing their stomata (leaf pores) to reduce water loss." +
                    " Additionally, scientists are investigating genes that regulate stress response and osmoprotectant production, " +
                    "molecules that help plants maintain cell function during drought.";
            messageList.add(new Message_repo(bot_response, sentBy));
            words.add("improve corn and rice");
        } else if (message.contains("traditional methods")) {
            bot_response = "Traditional breeding is a time-tested method that involves selecting plants with desirable traits, " +
                    "like high yield or disease resistance, and crossing them to create new generations with those traits. " +
                    "This method has been used for centuries and is still a valuable tool for crop improvement.";
            messageList.add(new Message_repo(bot_response, sentBy));

        } else if (message.contains("genetically modified rice")) {
            bot_response = "Yes, there is. Hybrid rice varieties are created by crossing two different, " +
                    "high-performing rice lines. This allows them to \"combine\" desirable traits from each parent, " +
                    "leading to increased yield potential. Genetically modified (GM) rice, on the other hand, " +
                    "involves directly modifying the genes of a rice plant to introduce a specific trait, " +
                    "such as herbicide tolerance or insect resistance. Hybrid rice does not involve altering the plant's DNA, while GM rice does.\n";
            messageList.add(new Message_repo(bot_response, sentBy));

        } else if (message.contains("poor water retention")) {
            bot_response = "Several strategies can help. Consider incorporating organic matter like composted rice straw or green manure crops into your soil. " +
                    "This improves soil structure and water-holding capacity. Additionally, " +
                    "practices like no-till farming or minimal tillage can minimize soil disturbance and promote healthy microbial life that benefits water retention.";
            messageList.add(new Message_repo(bot_response, sentBy));

        } else if (message.contains("constant problem")) {
            bot_response = "Integrated Pest Management (IPM) is a successful approach. " +
                    "Start with preventative measures like proper water management and maintaining a healthy soil ecosystem. " +
                    "Look into using competitive rice varieties and mechanical weeding techniques. If herbicides are necessary, " +
                    "choose selective ones that target specific weeds and use them judiciously to minimize resistance development.";
            messageList.add(new Message_repo(bot_response, sentBy));

        } else if (message.contains("interested in improving the overall health")) {
            bot_response = "Focus on promoting soil fertility. Rotate rice with other crops like legumes or green manure crops to fix nitrogen and add organic matter. " +
                    "Consider using cover crops during fallow periods to suppress weeds and improve soil health. " +
                    "Practices like composting rice straw and using organic fertilizers can further enrich the soil.";
            messageList.add(new Message_repo(bot_response, sentBy));

        } else if (message.contains("strategies for increasing corn yields")) {
            bot_response = "A combination of approaches works best. Utilize high-yielding corn varieties suited to your local climate and soil conditions. " +
                    "Implement proper soil testing and nutrient management to ensure optimal levels of essential nutrients for corn growth. " +
                    "Additionally, consider practices like crop rotation to break pest and disease cycles and improve soil health.";
            messageList.add(new Message_repo(bot_response, sentBy));

        } else if (message.contains("insect damage")) {
            bot_response = "IPM is again crucial. Look for natural control methods like planting beneficial insectary plants to attract predators of corn pests. " +
                    "Utilize scouting techniques to monitor pest populations and only use insecticides when necessary and targeted to specific pests. " +
                    "Planting insect-resistant corn varieties can also be helpful.";
            messageList.add(new Message_repo(bot_response, sentBy));

        } else if (message.contains("improve corn production without relying heavily")) {
            bot_response = "Absolutely! Practices like cover cropping and crop rotation help fix nitrogen in the soil, " +
                    "reducing reliance on synthetic fertilizers. Utilizing manure or compost as nutrient sources can also be beneficial. " +
                    "Additionally, soil testing can help ensure you're applying only the fertilizers your specific soil needs.";
            messageList.add(new Message_repo(bot_response, sentBy));

        } else if (message.contains("enhancing crop productivity")) {
            bot_response = "Effective water management is crucial for optimizing crop growth and yield. " +
                    "Techniques such as drip irrigation, mulching, and rainwater harvesting help conserve water, " +
                    "reduce water stress on plants, and ensure optimal moisture levels in the soil, leading to improved crop productivity.";
            messageList.add(new Message_repo(bot_response, sentBy));

        } else if (message.contains("improve crop yield")) {
            bot_response = "Sustainable farming practices focus on preserving natural resources, " +
                    "minimizing environmental impact, and maintaining long-term soil health. " +
                    "Examples include conservation tillage, agroforestry, organic farming, and the use of natural fertilizers and pest control methods. " +
                    "By adopting these practices, farmers can improve crop yield while safeguarding the environment for future generations.";
            messageList.add(new Message_repo(bot_response, sentBy));

        } else if (message.contains("negative impacts of climate change on crop yield")) {
            bot_response = "Climate change poses significant challenges to agriculture, including increased frequency of extreme weather events, " +
                    "shifts in temperature and precipitation patterns, and changing pest and disease dynamics. Farmers can mitigate these impacts " +
                    "by implementing climate-smart agricultural practices, such as planting heat-tolerant crop varieties, improving water management " +
                    "and irrigation efficiency, diversifying crop rotations, and enhancing soil carbon sequestration. By adapting their farming practices " +
                    "to changing climatic conditions, farmers can maintain or even improve crop yields in the face of climate change.";
            messageList.add(new Message_repo(bot_response, sentBy));

        } else if (message.contains("advantages of using organic fertilizers")) {
            bot_response = "Using organic fertilizers and soil amendments, such as compost, manure, and green manures, improves soil fertility, " +
                    "enhances nutrient cycling, promotes beneficial soil microorganisms, and reduces reliance on synthetic chemicals. " +
                    "These practices contribute to sustainable soil management, improved crop health, " +
                    "and higher yields in corn and rice farming while minimizing environmental impact.";
            messageList.add(new Message_repo(bot_response, sentBy));

        } else if (message.contains("beneficial microorganisms")) {
            bot_response = "Beneficial microorganisms, including mycorrhizal fungi, form symbiotic relationships with plant roots, enhancing nutrient uptake, " +
                    "improving soil structure, and increasing resistance to stressors. Mycorrhizal associations contribute to improved soil health, " +
                    "nutrient availability, and water retention, ultimately leading to healthier crops and higher yields in corn and rice fields.";
            messageList.add(new Message_repo(bot_response, sentBy));

        } else if (message.contains("grain crop improvements contribute")) {
            bot_response = "Grain crop improvements, achieved through breeding programs, genetic engineering, and agronomic practices, " +
                    "enhance crop yields, nutritional content, and resilience to environmental stresses. " +
                    "These advancements play a crucial role in increasing food production to meet the growing demands of a rising global population, " +
                    "thereby bolstering food security and reducing the risk of hunger and malnutrition worldwide.";
            messageList.add(new Message_repo(bot_response, sentBy));

        } else if (message.contains("specific examples of grain crop improvements")) {
            bot_response = "Grain crop improvements encompass a range of strategies, such as developing high-yielding varieties, enhancing disease and pest resistance, " +
                    "and improving nutritional profiles. For instance, the development of drought-tolerant maize varieties enables cultivation in regions prone to water scarcity, " +
                    "increasing overall crop resilience. Similarly, genetic modifications in rice have led to enhanced vitamin and mineral content, " +
                    "addressing nutritional deficiencies in populations reliant on rice as a staple food. These advancements contribute to increased food production, " +
                    "stability, and accessibility, ultimately strengthening global food security efforts.";
            messageList.add(new Message_repo(bot_response, sentBy));

        } else if (message.contains("economic sustainability")) {
            bot_response = "Grain crop improvements, including the development of high-yielding varieties and the implementation of sustainable farming practices, " +
                    "enhance the economic sustainability of farming communities in several ways. Firstly, increased yields result in higher crop productivity, " +
                    "leading to greater income potential for farmers. Secondly, improvements in crop resilience to pests," +
                    " diseases, and environmental stresses reduce production risks, safeguarding farmers' investments and incomes. ";
            messageList.add(new Message_repo(bot_response, sentBy));

        } else if (message.contains("common grain crops")) {
            bot_response = "Common grain crops include wheat, rice, maize (corn), barley, oats, sorghum, and millet.\n";
            messageList.add(new Message_repo(bot_response, sentBy));

        } else if (message.contains("technology play in grain crop improvement")) {
            bot_response = "Technology aids in precision farming, allowing for better management of resources " +
                    "like water and fertilizers, as well as the use of advanced breeding techniques like marker-assisted selection.";
            messageList.add(new Message_repo(bot_response, sentBy));

        } else if (message.contains("some techniques to increase the resistance of rice plants")) {
            bot_response = "Enhancing rice plant resistance can be achieved through breeding for disease-resistant varieties, practicing crop rotation, " +
                    "implementing integrated pest management (IPM) strategies, and utilizing biocontrol agents or resistant cultivars.";
            messageList.add(new Message_repo(bot_response, sentBy));

        } else if (message.contains("improve grain crops")) {
            bot_response = "To enhance grain crop productivity, focus on selecting suitable varieties adapted to local conditions, " +
                    "implementing effective soil and water management practices, and employing integrated pest and weed control measures. " +
                    "Optimize planting density, utilize precision agriculture technologies for efficient input management, and prioritize post-harvest handling to minimize losses..";
            messageList.add(new Message_repo(bot_response, sentBy));

        } else {
            Toast.makeText(this, "Input Something", Toast.LENGTH_SHORT).show();
        }


        messageAdapterRepo.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(messageAdapterRepo.getItemCount());
    }


}


