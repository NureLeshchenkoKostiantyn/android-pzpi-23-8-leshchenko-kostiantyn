package com.example.kostiantyn_leshchenko_pzpi_23_8;

import java.io.Serializable;

public class Note implements Serializable {
    private String title;
    private String description;
    private int priority; // 1 - Low, 2 - Medium, 3 - High
    private String dateTime;
    private String imagePath;

    public Note(String title, String description, int priority, String dateTime, String imagePath) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dateTime = dateTime;
        this.imagePath = imagePath;
    }

    // Геттери та сеттери
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getPriority() { return priority; }
    public String getDateTime() { return dateTime; }
    public String getImagePath() { return imagePath; }
}