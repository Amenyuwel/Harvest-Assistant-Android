package com.example.app;

public class PredictResponse {
    private String pestType;
    private Float confidence; // Use Float instead of float

    // Constructor
    public PredictResponse(String pestType, Float confidence) {
        this.pestType = pestType;
        this.confidence = confidence;
    }

    // Getter methods
    public String getPestType() {
        return pestType;
    }

    public Float getConfidence() {
        return confidence;
    }
}

