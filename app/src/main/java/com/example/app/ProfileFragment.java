package com.example.app;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;


public class ProfileFragment extends Fragment {

    private static final int PICK_IMAGE = 100;
    private static final int TAKE_PHOTO = 101;
    private static final int CAMERA_PERMISSION_CODE = 102;
    ImageView profileImageView;
    CardView accInfoCV;
    CardView changePasswordCV;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        accInfoCV = rootView.findViewById(R.id.pfp_Acc_Settings);
        changePasswordCV = rootView.findViewById(R.id.Change_Password);

        accInfoCV.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AccInfoCV.class);
            startActivity(intent);
        });

        // Set OnClickListener for change password action
        changePasswordCV.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChangePassCV.class);
            startActivity(intent);
        });


        return rootView;
    }


}

