package com.example.app;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    CardView cardView;
    CardView pestCardView;
    CardView tutorialCardView;
    CardView chatbotCardView;
    ImageSlider imageSlider;
    ImageView ivCamera;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Set up the ActionBar
        setupActionBar();

        cardView = rootView.findViewById(R.id.cvcalendar);
        pestCardView = rootView.findViewById(R.id.card3);
        tutorialCardView = rootView.findViewById(R.id.card5);
        chatbotCardView = rootView.findViewById(R.id.card6);
        imageSlider = rootView.findViewById(R.id.imageSlider);
        ivCamera = rootView.findViewById(R.id.ivCamera);

        // Set up the ImageSlider with images
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.aphids, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.catterpillar, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.cottonbolworm, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.earwig, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.grasshopper, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.sawfly, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.slug, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.snail, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.stemborer, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.weevil, ScaleTypes.FIT));
        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

        // Set up the Camera button
        ivCamera.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), CameraActivity.class);
            startActivity(intent);
        });

        // Calendar activity navigation
        cardView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Calendarclass.class);
            startActivity(intent);
        });

        // Pest activity navigation
        pestCardView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NewPest.class);
            startActivity(intent);
        });

        // Tutorial activity navigation
        tutorialCardView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TutorialMainActivity.class);
            startActivity(intent);
        });

        // Chatbot/FAQ activity navigation
        chatbotCardView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), BayotEmmanFAQ.class);
            startActivity(intent);
        });

        return rootView;
    }

    private void setupActionBar() {
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(requireContext(), R.color.darker_matcha)));
            actionBar.setTitle("HOME");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
