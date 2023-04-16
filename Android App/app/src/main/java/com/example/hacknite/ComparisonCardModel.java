package com.example.hacknite;

import android.app.Activity;

import java.net.URL;
import java.util.ArrayList;

public class ComparisonCardModel {

    private String medName;
    private String medImage;
    private String medPrice;
    private String nameOfPRovider;
    private String time;
    private String rating;
    private ArrayList<String> options;
    Activity activityContext;

    // Constructor
    public ComparisonCardModel(String medName, String medImage, String medPrice, String nameOfPRovider,
                               String time, String rating, ArrayList<String> options, Activity activityContext) {
        this.medName = medName;
        this.medImage = medImage;
        this.medPrice = medPrice;
        this.nameOfPRovider = nameOfPRovider;
        this.time = time;
        this.rating = rating;
        this.options = options;
        this.activityContext = activityContext;
    }

    // Getter and Setter
    public String getMedName() {
        return medName;
    }
    public void setMedName(String course_name) {
        this.medName = course_name;
    }

    public String getMedImage() {
        return medImage;
    }
    public void setCourse_rating(String medImage) {
        this.medImage = medImage;
    }

    public String getMedPrice() {
        return medPrice;
    }
    public void setMedPrice(String medPrice) {
        this.medPrice = medPrice;
    }

    public String getNameOfPRovider() {
        return nameOfPRovider;
    }
    public void setNameOfPRovider(String nameOfPRovider) {
        this.nameOfPRovider = nameOfPRovider;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public String getRating() {
        return rating;
    }
    public void setRating(String rating) {
        this.rating = rating;
    }

    public ArrayList<String> getOptions() {
        return options;
    }
    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public Activity getActivityContext() {
        return activityContext;
    }
    public void setActivityContext(Activity activityContext) {
        this.activityContext = activityContext;
    }
}
