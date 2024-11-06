package com.example.app;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

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

        // Set up the ActionBar from the parent activity
        setupActionBar();

        // Open Account Info Activity
        accInfoCV.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AccInfoCV.class);
            startActivity(intent);
        });

        // Open Change Password Activity
        changePasswordCV.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChangePassCV.class);
            startActivity(intent);
        });

        return rootView;
    }

    private void setupActionBar() {
        // Access ActionBar from the parent activity
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(requireActivity(), R.color.darker_matcha)));
            actionBar.setTitle("PROFILE");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    // Optional: Request Camera Permission
    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
            } else {
                // Permission denied
                new AlertDialog.Builder(requireContext())
                        .setTitle("Permission needed")
                        .setMessage("Camera permission is required to take photos.")
                        .setPositiveButton("OK", (dialog, which) -> requestCameraPermission())
                        .create()
                        .show();
            }
        }
    }
}
