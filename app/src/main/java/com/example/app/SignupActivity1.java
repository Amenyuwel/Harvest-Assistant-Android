package com.example.app;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.exifinterface.media.ExifInterface;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity1 extends AppCompatActivity {
    private static final String TAG = "SignupActivity1";
    private static final int IMAGE_REQUEST_CODE = 100;
    private static final int REQUEST_POST_NOTIFICATIONS = 101;
    private static final String CHANNEL_ID = "signup_notifications_channel";

    private EditText etFirstName, etMiddleName, etLastName, etContact, etArea, etRsbsaNum, etPassword, etConfirmPassword;
    private Spinner cropSpinner, brgySpinner;
    private Button btnSignup, btnSelectImage;
    private ImageView ivSelectedImage;
    private Bitmap selectedImageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup1);

        // Initialize UI components
        etFirstName = findViewById(R.id.etFirstName);
        etMiddleName = findViewById(R.id.etMiddleName);
        etLastName = findViewById(R.id.etLastName);
        etContact = findViewById(R.id.etContact);
        etArea = findViewById(R.id.etArea);
        etRsbsaNum = findViewById(R.id.etRsbsaNum);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        cropSpinner = findViewById(R.id.cropSpinner);
        brgySpinner = findViewById(R.id.brgySpinner);
        btnSignup = findViewById(R.id.btnSignup);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        ivSelectedImage = findViewById(R.id.ivSelectedImage);

        // Set up spinners
        setupCropSpinner();
        setupBrgySpinner();

        // Set up button listeners
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        // Create notification channel
        createNotificationChannel();
    }

    private void setupCropSpinner() {
        ArrayAdapter<CharSequence> cropAdapter = ArrayAdapter.createFromResource(this,
                R.array.crop_array, android.R.layout.simple_spinner_item);
        cropAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cropSpinner.setAdapter(cropAdapter);
    }

    private void setupBrgySpinner() {
        ArrayAdapter<CharSequence> brgyAdapter = ArrayAdapter.createFromResource(this,
                R.array.brgy_array, android.R.layout.simple_spinner_item);
        brgyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        brgySpinner.setAdapter(brgyAdapter);
    }

    private void selectImage() {
        // Check for READ_EXTERNAL_STORAGE permission (required for Android 12 and below)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        IMAGE_REQUEST_CODE);
                return;
            }
        }
        // Launch image picker
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle image selection
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                try {
                    selectedImageBitmap = getBitmapFromUri(imageUri);
                    ivSelectedImage.setImageBitmap(selectedImageBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Image selection failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Retrieves a Bitmap from a given Uri, handling EXIF orientation.
     *
     * @param uri The Uri of the image.
     * @return The correctly oriented Bitmap.
     * @throws IOException If an error occurs during reading.
     */
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        inputStream.close();

        // Handle EXIF orientation
        InputStream exifInputStream = getContentResolver().openInputStream(uri);
        ExifInterface exif = new ExifInterface(exifInputStream);
        exifInputStream.close();

        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        Bitmap rotatedBitmap = rotateBitmap(bitmap, orientation);

        return rotatedBitmap;
    }

    /**
     * Rotates a Bitmap based on the provided EXIF orientation.
     *
     * @param bitmap      The original Bitmap.
     * @param orientation The EXIF orientation.
     * @return The rotated Bitmap.
     */
    private Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.postRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.postRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.postRotate(270);
                break;
            default:
                return bitmap;
        }

        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap.recycle(); // Recycle the original bitmap
        return rotatedBitmap;
    }

    private void registerUser() {
        final String firstName = etFirstName.getText().toString().trim();
        final String middleName = etMiddleName.getText().toString().trim();
        final String lastName = etLastName.getText().toString().trim();
        final String contact = etContact.getText().toString().trim();
        final String area = etArea.getText().toString().trim();
        final String rsbsaNum = etRsbsaNum.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();
        final String confirmPassword = etConfirmPassword.getText().toString().trim();
        final String crop = cropSpinner.getSelectedItem().toString();
        final String barangay = brgySpinner.getSelectedItem().toString();

        // Validate inputs
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (firstName.isEmpty() || contact.isEmpty() || area.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Log the input values for debugging
        Log.d(TAG, "First Name: " + firstName);
        Log.d(TAG, "Middle Name: " + middleName);
        Log.d(TAG, "Last Name: " + lastName);
        Log.d(TAG, "Contact: " + contact);
        Log.d(TAG, "Area: " + area);
        Log.d(TAG, "RSBSA Number: " + rsbsaNum);
        Log.d(TAG, "Password: " + password);
        Log.d(TAG, "Crop: " + crop);
        Log.d(TAG, "Barangay: " + barangay);

        // API endpoint URL
        String url = "https://harvest.dermocura.net/PHP_API/register.php";

        // Create a POST request using Volley
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Log the raw response
                            Log.d(TAG, "Response: " + response);

                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            String message = jsonObject.getString("message");

                            // Log the parsed response
                            Log.d(TAG, "Success: " + success);
                            Log.d(TAG, "Message: " + message);

                            Toast.makeText(SignupActivity1.this, message, Toast.LENGTH_SHORT).show();

                            if (success) {
                                // Show a notification upon successful registration
                                showNotification("Registration Successful", "Welcome to the app!");

                                // Optionally, redirect to login or another activity
                                // Intent intent = new Intent(SignupActivity1.this, LoginActivity.class);
                                // startActivity(intent);
                                // finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, "Error parsing response: " + e.getMessage());
                            Toast.makeText(SignupActivity1.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Volley Error: " + error.getMessage());
                        Toast.makeText(SignupActivity1.this, "Registration failed", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Prepare POST parameters
                Map<String, String> params = new HashMap<>();
                params.put("first_name", firstName);
                params.put("middle_name", middleName);
                params.put("last_name", lastName);
                params.put("contact_number", contact);
                params.put("area", area);
                params.put("rsbsa_num", rsbsaNum);
                params.put("password", password);
                params.put("crop_id", String.valueOf(getCropId(crop)));
                params.put("barangay_id", String.valueOf(getBrgyId(barangay)));
                params.put("role_id", "1");  // Ensure role_id is sent as "1" for farmers
                if (selectedImageBitmap != null) {
                    String encodedImage = encodeImageToString(selectedImageBitmap);
                    if (encodedImage != null) {
                        params.put("image", encodedImage);  // Include the image as base64 string
                    } else {
                        Log.e(TAG, "Failed to encode image.");
                        Toast.makeText(SignupActivity1.this, "Failed to encode image.", Toast.LENGTH_SHORT).show();
                    }
                }
                return params;
            }
        };

        // Add request to the RequestQueue
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private int getCropId(String cropName) {
        switch (cropName.toLowerCase()) {
            case "rice":
                return 1;
            case "corn":
                return 2;
            // Add other crops as needed
            default:
                return -1;  // Handle unknown crop appropriately
        }
    }

    private int getBrgyId(String brgyName) {
        switch (brgyName.toLowerCase()) {
            case "lagao":
                return 1;
            case "san isidro":
                return 2;
            case "bula":
                return 3;
            case "apopong":
                return 4;
            case "baluan":
                return 5;
            case "city heights":
                return 6;
            case "conel":
                return 7;
            case "dadiangas east":
                return 8;
            case "dadiangas west":
                return 9;
            case "dadiangas south":
                return 10;
            case "fatima":
                return 11;
            case "katangawan":
                return 12;
            case "ligaya":
                return 13;
            case "mabuhay":
                return 14;
            case "olympog":
                return 15;
            case "san jose":
                return 16;
            case "tinagacan":
                return 17;
            default:
                return -1;  // Handle unknown barangay appropriately
        }
    }

    /**
     * Converts a Bitmap to a Base64 encoded string after resizing and compression.
     *
     * @param bitmap The original Bitmap.
     * @return The Base64 encoded string, or null if encoding fails.
     */
    private String encodeImageToString(Bitmap bitmap) {
        if (bitmap == null) {
            Log.e(TAG, "Bitmap is null, cannot encode.");
            return null;
        }

        // Resize the image before encoding
        Bitmap resizedBitmap = resizeBitmap(bitmap, 500, 500); // Resize to 500x500 max dimension
        if (resizedBitmap == null) {
            Log.e(TAG, "Resized bitmap is null.");
            return null;
        }

        // Compress the bitmap
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        boolean compressed = resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream); // 80% quality
        if (!compressed) {
            Log.e(TAG, "Bitmap compression failed.");
            return null;
        }

        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    /**
     * Resizes a bitmap while preserving the aspect ratio.
     *
     * @param bitmap    The original Bitmap.
     * @param maxWidth  The maximum width.
     * @param maxHeight The maximum height.
     * @return The resized Bitmap.
     */
    private Bitmap resizeBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {
        if (bitmap == null) {
            Log.e(TAG, "Bitmap is null, cannot resize.");
            return null;
        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // Calculate the aspect ratio
        float aspectRatio = (float) width / height;

        // Determine the new width and height while preserving the aspect ratio
        if (width > height) {
            width = maxWidth;
            height = Math.round(maxWidth / aspectRatio);
        } else {
            height = maxHeight;
            width = Math.round(maxHeight * aspectRatio);
        }

        // Create the resized bitmap
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        if (resizedBitmap != bitmap) {
            bitmap.recycle(); // Recycle the original bitmap if it's different
        }
        return resizedBitmap;
    }

    /**
     * Creates a notification channel for Android O and above.
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Signup Notifications";
            String description = "Notifications related to user signup";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Register the channel with the system
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            } else {
                Log.e(TAG, "NotificationManager is null, cannot create channel.");
            }
        }
    }

    /**
     * Shows a notification with the given title and message.
     *
     * @param title   The notification title.
     * @param message The notification message.
     */
    private void showNotification(String title, String message) {
        // For Android 13 and above, check and request notification permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        REQUEST_POST_NOTIFICATIONS);
                return;
            }
        }

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification) // Ensure you have this icon in your drawable resources
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true); // Dismiss the notification when clicked

        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        int notificationId = (int) System.currentTimeMillis(); // Unique ID for each notification
        notificationManager.notify(notificationId, builder.build());
    }

    /**
     * Handles the result of permission requests.
     *
     * @param requestCode  The request code passed in requestPermissions().
     * @param permissions  The requested permissions.
     * @param grantResults The grant results for the corresponding permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_POST_NOTIFICATIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with showing the notification
                showNotification("Registration Successful", "Welcome to the app!");
            } else {
                // Permission denied, inform the user
                Toast.makeText(this, "Notification permission denied. Unable to show notifications.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == IMAGE_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with image selection
                selectImage();
            } else {
                // Permission denied, inform the user
                Toast.makeText(this, "Permission denied to read external storage.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
