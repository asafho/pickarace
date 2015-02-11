package com.example.asafh.myapplication;


import java.util.ArrayList;

public class Events {

    private String eventName, thumbnailUrl,eventLocation;
    private String eventDate;
    private ArrayList<String> genre;

    public Events() {
    }

    public Events(String name, String thumbnailUrl, String eventDate, String location) {
        this.eventName = name;
        this.thumbnailUrl = thumbnailUrl;
        this.eventDate = eventDate;
        this.eventLocation = location;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String name) {
        this.eventName = name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

}
