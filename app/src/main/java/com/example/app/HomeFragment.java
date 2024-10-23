package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.app.Calendarclass;
import com.example.app.CameraActivity;
import com.example.app.MainActivityFAQ;
import com.example.app.PestReal;
import com.example.app.R;
import com.example.app.RecoMainActivity;
import com.example.app.TutorialMainActivity;

import java.util.ArrayList;


public class HomeFragment extends Fragment {


    CardView cardView;

    CardView recoCardView;
    CardView analyticsCardView;
    CardView pestCardView;
    CardView tutorialCardView;
    CardView chatbotCardView;
    ImageSlider imageSlider;
    ImageView ivCamera;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);


        cardView = rootView.findViewById(R.id.cvcalendar);
        recoCardView = rootView.findViewById(R.id.card1);
        pestCardView = rootView.findViewById(R.id.card3);
        tutorialCardView = rootView.findViewById(R.id.card5);
        chatbotCardView = rootView.findViewById(R.id.card6);
        imageSlider = rootView.findViewById(R.id.imageSlider);
        ivCamera = rootView.findViewById(R.id.ivCamera);

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




        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch CameraActivity
                Intent intent = new Intent(getActivity(), CameraActivity.class);
                startActivity(intent);
            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create intent to navigate to the next activity
                Intent intent = new Intent(getActivity(), Calendarclass.class);
                startActivity(intent);
            }
        });
        recoCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecoMainActivity.class);
                startActivity(intent);
            }
        });

        pestCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PestReal.class);
                startActivity(intent);
            }
        });
        tutorialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TutorialMainActivity.class);
                startActivity(intent);
            }
        });
        chatbotCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BayotEmmanFAQ.class);
                startActivity(intent);
            }
        });
        return rootView;
    }
}