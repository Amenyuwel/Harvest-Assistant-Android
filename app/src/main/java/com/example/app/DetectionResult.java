package com.example.app;

import android.graphics.RectF;

public class DetectionResult {
    private RectF boundingBox;
    private String className;
    private float confidence;

    public DetectionResult(RectF boundingBox, String className, float confidence) {
        this.boundingBox = boundingBox;
        this.className = className;
        this.confidence = confidence;
    }

    public RectF getBoundingBox() {
        return boundingBox;
    }

    public String getClassName() {
        return className;
    }

    public float getConfidence() {
        return confidence;
    }
}
