package com.example.asafh.pickarace;


import java.util.ArrayList;
import java.util.HashMap;

public class Events {

    private String eventName, thumbnailUrl,eventLocation,eventDetails,eventDate;
    private ArrayList<String> racesTypeArray;
    private HashMap<String,String> raceDistanceLink;

    public Events() {
    }

    public Events(String name, String thumbnailUrl, String eventDate, String location, String details, ArrayList<String> racesType ,HashMap<String, String> raceDistLink) {
        this.eventName = name;
        this.thumbnailUrl = thumbnailUrl;
        this.eventDate = eventDate;
        this.eventLocation = location;
        this.eventDetails = details;
        this.racesTypeArray = racesType;
        this.raceDistanceLink = raceDistLink;
    }

    public void  setRaceDistanceLink(HashMap<String,String> raceDistLink)
    {
        this.raceDistanceLink = raceDistLink;
    }

    public HashMap<String,String> getRaceDistanceLink(){
        return this.raceDistanceLink;
    }

    public ArrayList<String> getRacesType() {
        return racesTypeArray;
    }

    public void setRacesType(ArrayList<String> racesType) {
        this.racesTypeArray = racesType;
    }

    public String getEventDetails(){
        return eventDetails;
    }

    public void setEventDetails(String details){
        this.eventDetails = details;
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
