package com.example.app;

import android.content.Context;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.ByteBuffer;

public class MyTFLiteModel {

    private Interpreter interpreter;
    private int inputSize;
    private int inputWidth;
    private int inputHeight;
    private String[] labels;

    public MyTFLiteModel(Context context) throws IOException {
        interpreter = new Interpreter(loadModelFile(context, "optimized_model.tflite"));
        // Set your model input size, width, height, and labels
        inputWidth = 224; // Example width
        inputHeight = 224; // Example height
        inputSize = inputWidth * inputHeight * 3 * 4; // RGB input with 4 bytes per float
        labels = new String[]{"aphids", "armyworm", "beetles", "caterpillar", "earwig", "grasshopper", "slug", "snail", "stem_borer", "weevil"}; // Example labels
    }

    private MappedByteBuffer loadModelFile(Context context, String modelPath) throws IOException {
        FileInputStream inputStream = new FileInputStream(context.getAssets().openFd(modelPath).getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = context.getAssets().openFd(modelPath).getStartOffset();
        long declaredLength = context.getAssets().openFd(modelPath).getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    public int getInputSize() {
        return inputSize;
    }

    public int getInputWidth() {
        return inputWidth;
    }

    public int getInputHeight() {
        return inputHeight;
    }

    public String getClassLabel(int classId) {
        if (classId >= 0 && classId < labels.length) {
            return labels[classId];
        }
        return "Unknown";
    }

    public float[] runInference(ByteBuffer inputBuffer) {
        int numClasses = labels.length;
        float[][] output = new float[1][numClasses];
        interpreter.run(inputBuffer, output);
        return output[0];
    }

    public void close() {
        interpreter.close();
    }
}
