package com.example.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.RectF;
import android.util.Log;

import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PestDetector {
    private static final String TAG = "PestDetector";
    private static final float CONFIDENCE_THRESHOLD = 0.5f;
    private static final int INPUT_SIZE = 224;
    private static final int NUM_CLASSES = 10;

    private final Interpreter interpreter;
    private final String[] labels = new String[]{"aphids", "armyworm", "beetle", "caterpillar", "earwig", "grasshopper", "slug", "snail", "stem_borer", "weevil"};

    public PestDetector(Context context) {
        try {
            ByteBuffer model = FileUtil.loadMappedFile(context, "model.tflite");
            interpreter = new Interpreter(model);
        } catch (IOException e) {
            throw new RuntimeException("Error initializing TensorFlow Lite model", e);
        }
    }

    public List<Detection> classifyImage(Bitmap bitmap) {
        if (bitmap == null) {
            Log.e(TAG, "Bitmap is null, returning empty detection list.");
            return new ArrayList<>();
        }

        ByteBuffer input = preprocessImage(bitmap);

        // Assuming the model outputs only class probabilities
        float[][] classProbabilities = new float[1][NUM_CLASSES];

        try {
            interpreter.run(input, classProbabilities);
            // Verify the shape of the output
            if (classProbabilities[0].length != NUM_CLASSES) {
                throw new RuntimeException("Unexpected output size: " + classProbabilities[0].length);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error during model inference: " + e.getMessage(), e);
            return new ArrayList<>();
        }

        // Log the output probabilities for debugging
        for (int i = 0; i < classProbabilities[0].length; i++) {
            Log.d(TAG, "Class " + labels[i] + " confidence: " + classProbabilities[0][i]);
        }

        return processOutput(classProbabilities[0]);
    }

    private ByteBuffer preprocessImage(Bitmap bitmap) {
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, true);
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * INPUT_SIZE * INPUT_SIZE * 3);
        byteBuffer.order(ByteOrder.nativeOrder());

        int[] intValues = new int[INPUT_SIZE * INPUT_SIZE];
        resizedBitmap.getPixels(intValues, 0, INPUT_SIZE, 0, 0, INPUT_SIZE, INPUT_SIZE);

        for (int pixel : intValues) {
            byteBuffer.putFloat((Color.red(pixel) - 127.5f) / 127.5f);
            byteBuffer.putFloat((Color.green(pixel) - 127.5f) / 127.5f);
            byteBuffer.putFloat((Color.blue(pixel) - 127.5f) / 127.5f);
        }

        return byteBuffer;
    }

    private List<Detection> processOutput(float[] probabilities) {
        List<Detection> detections = new ArrayList<>();

        // Get the index of the class with the highest probability
        int detectedClass = getMaxIndex(probabilities);
        float confidence = probabilities[detectedClass];

        // Only add detections that meet the confidence threshold
        if (confidence >= CONFIDENCE_THRESHOLD) {
            detections.add(new Detection(labels[detectedClass], confidence));
        }

        return detections;
    }

    private int getMaxIndex(float[] probabilities) {
        int maxIndex = 0;
        float maxProb = probabilities[0];
        for (int i = 1; i < probabilities.length; i++) {
            if (probabilities[i] > maxProb) {
                maxIndex = i;
                maxProb = probabilities[i];
            }
        }
        return maxIndex;
    }

    public static class Detection {
        public String label;
        public float confidence;
        public RectF boundingBox;  // Optional bounding box

        Detection(String label, float confidence) {
            this.label = label;
            this.confidence = confidence;
            this.boundingBox = null;  // No bounding box provided
        }

        Detection(String label, float confidence, RectF boundingBox) {
            this.label = label;
            this.confidence = confidence;
            this.boundingBox = boundingBox;
        }
    }
}