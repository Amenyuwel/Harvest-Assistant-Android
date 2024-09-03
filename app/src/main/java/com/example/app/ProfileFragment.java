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
        profileImageView = rootView.findViewById(R.id.userPfp);
        accInfoCV = rootView.findViewById(R.id.pfp_Acc_Settings);
        changePasswordCV = rootView.findViewById(R.id.Change_Password);
        profileImageView.setOnClickListener(v -> showPictureDialog());

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

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera",
                "Remove photo" };
        pictureDialog.setItems(pictureDialogItems,
                (dialog, which) -> {
                    switch (which) {
                        case 0:
                            openGallery();
                            break;
                        case 1:
                            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                            } else {
                                openCamera();
                            }
                            break;
                        case 2:
                            removePhoto();
                            break;
                    }
                });
        pictureDialog.show();
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    private void removePhoto() {
        profileImageView.setImageResource(R.drawable.svg_add);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == PICK_IMAGE && data != null && data.getData() != null) {
                Uri imageUri = data.getData();
                profileImageView.setImageURI(imageUri);
            } else if (requestCode == TAKE_PHOTO && data != null && data.getExtras() != null) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                profileImageView.setImageBitmap(thumbnail);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            }
        }
    }
}
