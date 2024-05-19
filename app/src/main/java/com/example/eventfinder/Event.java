package com.example.eventfinder;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Event {
    private String name;

    public String getEventImageUrl() {
        return eventImageUrl;
    }

    public void setEventImageUrl(String eventImageUrl) {
        this.eventImageUrl = eventImageUrl;
    }

    private String eventImageUrl;

    public void setDate(String date) {
        this.date = date;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    private String eventId;
    private String venue;
    private String date;
    private String category;

    private String time;
    private Boolean isFavorite;

    private List<String> artistSpotifyList;

    public List<String> getArtistSpotifyList() {
        return artistSpotifyList;
    }

    public void setArtistSpotifyList(List<String> artistSpotifyList) {
        this.artistSpotifyList = artistSpotifyList;
    }

    private List<String> artistTeamList;
    private String artistTeam = "";
    private String genre = "";
    private String priceRange;
    private String ticketStatus;
    private String ticketUrl;
    private String seatmap;
    public Event(String name, String venue,String date, String time, String category) {
        this.name = name;
        this.venue = venue;
        this.date = date;
        this.time = time;
        this.category = category;
        isFavorite = false;
        this.artistSpotifyList=new ArrayList<>();
    }
    public String getName() {
        return name;
    }

    public String getVenue() {
        return venue;
    }

    public String getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public String getFavoriteString() {
        if(isFavorite){
            return "true";
        }
        else{
            return "false";
        }
    }

    public Boolean getIsFavorite() {
        return isFavorite;
    }

    public String getArtistsTeams() {
        return artistTeam;
    }

    public String getCategoryString() {
        return genre;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public String getTicketUrl() {
        return ticketUrl;
    }

    public String getSeatmap() {
        return seatmap;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setIsFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public List<String> getArtistTeamList() {
        return artistTeamList;
    }

    public void setArtistTeamList(List<String> artistTeamList) {
        this.artistTeamList = artistTeamList;
    }

    public void setArtistsTeams(List<String> artistTeamArray) {
        this.artistTeamList = artistTeamArray;
        for(int k = 0; k < artistTeamArray.size(); ++k){
            if(artistTeamArray.get(k) != null) {
                this.artistTeam += artistTeamArray.get(k);
                if(k < artistTeamArray.size()-1) {
                    this.artistTeam += " | ";
                }
            }
        }
        Log.d("artists_teams_string", "setArtistsTeams: " + artistTeam);
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setCategoryDetail(List<String> categoryList) {
        for(int k = 0; k < categoryList.size(); ++k){
            if(categoryList.get(k) != null  && !categoryList.get(k).equals("Undefined")) {
                if(!this.genre.contains(categoryList.get(k))){
                    if(k==0){
                        this.genre += categoryList.get(k);
                    }else {
                        this.genre += " | " + categoryList.get(k);
                    }
                    System.out.println(this.genre);
                }
            }
        }
        Log.d("category_detail", "category_detail: " + genre);
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }


    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public void setTicketUrl(String ticketUrl) {
        this.ticketUrl = ticketUrl;
    }

    public void setSeatmap(String seatmap) {
        this.seatmap = seatmap;
    }

}

