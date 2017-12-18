package com.example.josh.lab2;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * Created by Josh on 10/2/2017.
 */

public class Comment {
    private long id;
    private String InputType;
    private String ActivityType;
    private Date ActivityDateTime;
    private double ActivityDuration;
    private int ActivityDistance;
    private int ActivityCalories;
    private int ActivityHeartRate;
    private String ActivityComment;
    private String ActivityLatitudes;
    private String ActivityLongitudes;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getInputType() {
        return InputType;
    }

    public void setInputType(String inputType) {
        this.InputType = inputType;
    }

    public String getActivityType() {
        return ActivityType;
    }

    public void setActivityType(String activityType) {
        this.ActivityType = activityType;
    }

    public Date getActivityDateTime() {
        return ActivityDateTime;
    }

    public void setActivityDateTime(Date activityDateTime) {
        this.ActivityDateTime = activityDateTime;
    }

    public double getActivityDuration() {
        return ActivityDuration;
    }

    public void setActivityDuration(double activityDuration) {
        this.ActivityDuration = activityDuration;
    }

    public int getActivityDistance() {
        return ActivityDistance;
    }

    public void setActivityDistance(int activityDistance) {
        this.ActivityDistance = activityDistance;
    }

    public int getActivityCalories() {
        return ActivityCalories;
    }

    public void setActivityCalories(int activityCalories) {
        this.ActivityCalories = activityCalories;
    }

    public int getActivityHeartRate() {
        return ActivityHeartRate;
    }

    public void setActivityHeartRate(int activityHeartRate) {
        this.ActivityHeartRate = activityHeartRate;
    }

    public String getComment() {
        return ActivityComment;
    }

    public void setComment(String comment) {
        this.ActivityComment = comment;
    }

    public String getLatitudes() {
        return ActivityLatitudes;
    }

    public void setActivityLatitudes(String latitudes) {
        this.ActivityLatitudes = latitudes;
    }

    public String getActivityLongitudes() {
        return ActivityLongitudes;
    }

    public void setActivityLongitudes(String longitudes) {
        this.ActivityLongitudes = longitudes;
    }

    @Override
    public String toString() {
        // Test
        String duration = Double.toString(ActivityDuration);
        int durationSeconds = 0;
        double durationSecondsDouble = 0;
        int durationSplit = duration.split("\\.").length;
        String[] durationArray = duration.split("\\.");
        if (durationSplit < 1 || ActivityDuration == 0) {
            duration = "";
        }
        if (durationSplit > 1) {
            String durationSecondStr = durationArray[1];
            durationSecondsDouble = Double.parseDouble("." + durationSecondStr);
            durationSecondsDouble = durationSecondsDouble *60;
            durationSeconds = (int)durationSecondsDouble;
            duration = durationArray[0];
        }
        String activityDate = "";
        DateFormat df = new SimpleDateFormat("HH:mm:ss MM/dd/yyyy");
        if (ActivityDateTime != null) {
            activityDate = df.format(ActivityDateTime);
        }
        String activity = "(ID: " + id + ")" +
                InputType + ": " +
                ActivityType + ", " +
                activityDate + "\n" +
                ActivityDistance + " Miles, " +
                duration + " Minutes " +
                durationSeconds + " Seconds";
        return activity;
    }
}
