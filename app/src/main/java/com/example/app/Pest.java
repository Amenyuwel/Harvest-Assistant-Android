package com.example.app;

public class Pest {
    private String pestName;
    private String pestDescription;
    private String recommendation;
    private String activeMonth;
    private String season;

    // Getters and Setters
    public String getPestName() {
        return pestName;
    }

    public void setPestName(String pestName) {
        this.pestName = pestName;
    }

    public String getPestDescription() {
        return pestDescription;
    }

    public void setPestDescription(String pestDescription) {
        this.pestDescription = pestDescription;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public String getActiveMonth() {
        return activeMonth;
    }

    public void setActiveMonth(String activeMonth) {
        this.activeMonth = activeMonth;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }
}
