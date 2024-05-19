package com.example.eventfinder;

public class EventListCard {
    private String eventId;
    private String name;
    private String venue;
    private String date;
    private String genre;
    private String imageUrl;
    private String time;
    private Boolean isFavorite;

    public EventListCard(String eventId, String name, String venue, String date, String genre, String imageUrl, String time, Boolean isFavorite) {
        this.eventId = eventId;
        this.name = name;
        this.venue = venue;
        this.date = date;
        this.genre = genre;
        this.imageUrl = imageUrl;
        this.time = time;
        this.isFavorite = isFavorite;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
