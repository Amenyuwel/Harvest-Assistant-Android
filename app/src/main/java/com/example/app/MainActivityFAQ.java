package com.example.app;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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

        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.darker_matcha));

        // Set the ActionBar background color
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Set the ActionBar background color
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.darker_matcha)));
            // Remove ActionBar Title
            actionBar.setTitle("");
            // Show the back button in action bar
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        expandableListView = findViewById(R.id.expandableListView);

        // Preparing the data
        prepareListData();

        faqAdapter = new FAQAdapter(this, listDataHeader, listDataChild);
        expandableListView.setAdapter(faqAdapter);
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();


        listDataHeader.add("What are some traditional methods used to improve corn and rice?");
        listDataHeader.add("I've heard about drought-resistant corn. How does it work?");
        listDataHeader.add("Is there a difference between hybrid and genetically modified rice?");
        listDataHeader.add("My paddy land suffers from poor water retention. What can I do to improve it?");
        listDataHeader.add("Weeds are a constant problem in my paddy field. How can I manage them more effectively?");
        listDataHeader.add("I'm interested in improving the overall health of my paddy soil. What practices can I adopt?");
        listDataHeader.add("What are some strategies for increasing corn yields in my fields?");
        listDataHeader.add("I'm concerned about insect damage to my corn crop. What are some options for control?");
        listDataHeader.add("With rising fertilizer costs, are there ways to improve corn production without relying heavily on them?");
        listDataHeader.add("What role does water management play in enhancing crop productivity? ");
        listDataHeader.add("What are some sustainable farming practices that can improve crop yield?");
        listDataHeader.add("What strategies can farmers employ to mitigate the negative impacts of climate change on crop yield?");
        listDataHeader.add("What are the advantages of using organic fertilizers and soil amendments in corn and rice farming?");
        listDataHeader.add("What role do beneficial microorganisms, such as mycorrhizal fungi, play in improving soil health and crop yields in corn and rice fields?");
        listDataHeader.add("How do grain crop improvements contribute to global food security?");
        listDataHeader.add("What are some specific examples of grain crop improvements and their impact on food security?");
        listDataHeader.add("How do grain crop improvements contribute to the economic sustainability of farming communities?");
        listDataHeader.add("What are some common grain crops?");
        listDataHeader.add("What role does technology play in grain crop improvement?");
        listDataHeader.add("What are some techniques to increase the resistance of rice plants to pests and diseases?");
        listDataHeader.add("How can soil fertility be improved to increase grain production?");
        listDataHeader.add("What irrigation practices can help in boosting grain production?");
        listDataHeader.add("How can pest and disease control be managed to enhance grain yields?");
        listDataHeader.add("What role does technology play in increasing grain production?");
        listDataHeader.add("How can crop yield be enhanced through improved planting practices?");


        listDataChild.put(listDataHeader.get(0), "Traditional breeding is a time-tested method that involves selecting plants with desirable traits, like high yield or disease resistance, and crossing them to create new generations with those traits. This method has been used for centuries and is still a valuable tool for crop improvement.");
        listDataChild.put(listDataHeader.get(1), "Researchers are exploring several strategies for drought resistance in corn. Some varieties have deeper root systems to access more water from the soil. Others have genes that allow them to tolerate drier conditions by closing their stomata (leaf pores) to reduce water loss. Additionally, scientists are investigating genes that regulate stress response and osmoprotectant production, molecules that help plants maintain cell function during drought.");
        listDataChild.put(listDataHeader.get(2), "Yes, there is. Hybrid rice varieties are created by crossing two different, high-performing rice lines. This allows them to \"combine\" desirable traits from each parent, leading to increased yield potential. Genetically modified (GM) rice, on the other hand, involves directly modifying the genes of a rice plant to introduce a specific trait, such as herbicide tolerance or insect resistance. Hybrid rice does not involve altering the plant's DNA, while GM rice does.");
        listDataChild.put(listDataHeader.get(3), "Several strategies can help. Consider incorporating organic matter like composted rice straw or green manure crops into your soil. This improves soil structure and water-holding capacity. Additionally, practices like no-till farming or minimal tillage can minimize soil disturbance and promote healthy microbial life that benefits water retention.");
        listDataChild.put(listDataHeader.get(4), "Integrated Pest Management (IPM) is a successful approach. Start with preventative measures like proper water management and maintaining a healthy soil ecosystem. Look into using competitive rice varieties and mechanical weeding techniques. If herbicides are necessary, choose selective ones that target specific weeds and use them judiciously to minimize resistance development.");
        listDataChild.put(listDataHeader.get(5), "Focus on promoting soil fertility. Rotate rice with other crops like legumes or green manure crops to fix nitrogen and add organic matter. Consider using cover crops during fallow periods to suppress weeds and improve soil health. Practices like composting rice straw and using organic fertilizers can further enrich the soil.");
        listDataChild.put(listDataHeader.get(6), "A combination of approaches works best. Utilize high-yielding corn varieties suited to your local climate and soil conditions. Implement proper soil testing and nutrient management to ensure optimal levels of essential nutrients for corn growth. Additionally, consider practices like crop rotation to break pest and disease cycles and improve soil health.");
        listDataChild.put(listDataHeader.get(7), "IPM is again crucial. Look for natural control methods like planting beneficial insectary plants to attract predators of corn pests. Utilize scouting techniques to monitor pest populations and only use insecticides when necessary and targeted to specific pests. Planting insect-resistant corn varieties can also be helpful.");
        listDataChild.put(listDataHeader.get(8), "Absolutely! Practices like cover cropping and crop rotation help fix nitrogen in the soil, reducing reliance on synthetic fertilizers. Utilizing manure or compost as nutrient sources can also be beneficial. Additionally, soil testing can help ensure you're applying only the fertilizers your specific soil needs.");
        listDataChild.put(listDataHeader.get(9), "Effective water management is crucial for optimizing crop growth and yield. Techniques such as drip irrigation, mulching, and rainwater harvesting help conserve water, reduce water stress on plants, and ensure optimal moisture levels in the soil, leading to improved crop productivity.");
        listDataChild.put(listDataHeader.get(10), "Sustainable farming practices focus on preserving natural resources, minimizing environmental impact, and maintaining long-term soil health. Examples include conservation tillage, agroforestry, organic farming, and the use of natural fertilizers and pest control methods. By adopting these practices, farmers can improve crop yield while safeguarding the environment for future generations.");
        listDataChild.put(listDataHeader.get(11), "Climate change poses significant challenges to agriculture, including increased frequency of extreme weather events, shifts in temperature and precipitation patterns, and changing pest and disease dynamics. Farmers can mitigate these impacts by implementing climate-smart agricultural practices, such as planting heat-tolerant crop varieties, improving water management and irrigation efficiency, diversifying crop rotations, and enhancing soil carbon sequestration. By adapting their farming practices to changing climatic conditions, farmers can maintain or even improve crop yields in the face of climate change.");
        listDataChild.put(listDataHeader.get(12), "Using organic fertilizers and soil amendments, such as compost, manure, and green manures, improves soil fertility, enhances nutrient cycling, promotes beneficial soil microorganisms, and reduces reliance on synthetic chemicals. These practices contribute to sustainable soil management, improved crop health, and higher yields in corn and rice farming while minimizing environmental impact.\n");
        listDataChild.put(listDataHeader.get(13), "Beneficial microorganisms, including mycorrhizal fungi, form symbiotic relationships with plant roots, enhancing nutrient uptake, improving soil structure, and increasing resistance to stressors. Mycorrhizal associations contribute to improved soil health, nutrient availability, and water retention, ultimately leading to healthier crops and higher yields in corn and rice fields.");
        listDataChild.put(listDataHeader.get(14), "Grain crop improvements, achieved through breeding programs, genetic engineering, and agronomic practices, enhance crop yields, nutritional content, and resilience to environmental stresses. These advancements play a crucial role in increasing food production to meet the growing demands of a rising global population, thereby bolstering food security and reducing the risk of hunger and malnutrition worldwide.");
        listDataChild.put(listDataHeader.get(15), "Grain crop improvements encompass a range of strategies, such as developing high-yielding varieties, enhancing disease and pest resistance, and improving nutritional profiles. For instance, the development of drought-tolerant maize varieties enables cultivation in regions prone to water scarcity, increasing overall crop resilience. Similarly, genetic modifications in rice have led to enhanced vitamin and mineral content, addressing nutritional deficiencies in populations reliant on rice as a staple food. These advancements contribute to increased food production, stability, and accessibility, ultimately strengthening global food security efforts.");
        listDataChild.put(listDataHeader.get(16), "Grain crop improvements, including the development of high-yielding varieties and the implementation of sustainable farming practices, enhance the economic sustainability of farming communities in several ways. Firstly, increased yields result in higher crop productivity, leading to greater income potential for farmers. Secondly, improvements in crop resilience to pests, diseases, and environmental stresses reduce production risks, safeguarding farmers' investments and incomes. ");
        listDataChild.put(listDataHeader.get(17), "Common grain crops include wheat, rice, maize (corn), barley, oats, sorghum, and millet.");
        listDataChild.put(listDataHeader.get(18), "Technology aids in precision farming, allowing for better management of resources like water and fertilizers, as well as the use of advanced breeding techniques like marker-assisted selection.");
        listDataChild.put(listDataHeader.get(19), "Enhancing rice plant resistance can be achieved through breeding for disease-resistant varieties, practicing crop rotation, implementing integrated pest management (IPM) strategies, and utilizing biocontrol agents or resistant cultivars.");
        listDataChild.put(listDataHeader.get(20), "To optimize soil fertility, it's essential to follow a comprehensive approach. Regular soil testing enables the assessment of nutrient levels and deficiencies, guiding the application of fertilizers accordingly. Achieving balanced fertilization involves using the right amount and type of fertilizers, incorporating both organic options like compost and manure, as well as inorganic fertilizers. Additionally, integrating organic amendments such as compost and green manure into the soil enhances its structure and fertility, fostering a conducive environment for plant growth.");
        listDataChild.put(listDataHeader.get(21), "To optimize water management in agriculture, employing efficient irrigation systems like drip or sprinkler systems is crucial to minimize water wastage and ensure effective water distribution. Incorporating water conservation techniques such as rainwater harvesting, mulching, and selecting drought-resistant crop varieties further enhances water efficiency. Additionally, scheduling irrigation based on crop requirements and soil moisture levels allows for precise water usage, maximizing crop yield while conserving water resources.");
        listDataChild.put(listDataHeader.get(22), "Integrated Pest Management (IPM) employs a holistic approach by blending biological, physical, and chemical techniques to tackle pest issues, while Resistant Varieties offer a proactive measure against crop losses by cultivating plants that are disease-resistant. Additionally, Regular Monitoring of fields plays a crucial role in promptly identifying pests and diseases, facilitating timely interventions to mitigate their impact.");
        listDataChild.put(listDataHeader.get(23), "Precision Agriculture integrates GPS, sensors, and drones to monitor crop health, soil conditions, and growth patterns, facilitating precise input application. Variable Rate Technology (VRT) further enhances resource utilization by applying inputs like fertilizers and water at rates customized to individual field conditions. Leveraging data analysis through digital tools and platforms enables informed decision-making regarding planting, fertilization, and pest management, fostering efficient and sustainable agricultural practices.");
        listDataChild.put(listDataHeader.get(24), "Utilizing high-quality seeds that are certified, high-yielding, and disease-resistant constitutes the foundation of successful agriculture. Pairing this with proper planting techniques, including accurate depth and spacing to optimize root development and nutrient absorption, further enhances crop performance. Additionally, adjusting planting density to minimize inter-plant competition and provide ample room for growth is essential for maximizing yield potential and overall crop health.");
    }
    // This event will enable the back function to the button on press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

