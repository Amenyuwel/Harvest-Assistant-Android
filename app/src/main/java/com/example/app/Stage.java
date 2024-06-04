package com.example.app;

public class Stage {
    private String name;
    private String description;
    private String suggestions;
    private int imageResId; // Add this line

    public Stage(String name, String description, String suggestions, int imageResId) {
        this.name = name;
        this.description = description;
        this.suggestions = suggestions;
        this.imageResId = imageResId; // Add this line
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getSuggestions() {
        return suggestions;
    }

    public int getImageResId() {
        return imageResId; // Add this line
    }
}

