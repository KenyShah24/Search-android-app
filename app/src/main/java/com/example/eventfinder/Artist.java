package com.example.eventfinder;

import java.util.List;

public class Artist {
    private String artistId;

    public String getArtistImg() {
        return artistImg;
    }

    public void setArtistImg(String artistImg) {
        this.artistImg = artistImg;
    }

    private String artistImg;


    public Artist(String artistId, String followers, String name, String spotifyLink, String popularity, List<String> albums, String artistImg) {
        this.artistId = artistId;
        this.followers = followers;
        this.name = name;
        this.spotifyLink = spotifyLink;
        this.popularity = popularity;
        this.albums = albums;
        this.artistImg=artistImg;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpotifyLink() {
        return spotifyLink;
    }

    public void setSpotifyLink(String spotifyLink) {
        this.spotifyLink = spotifyLink;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public List<String> getAlbums() {
        return albums;
    }

    public void setAlbums(List<String> albums) {
        this.albums = albums;
    }

    private String followers;
    private String name;
    private String spotifyLink;
    private String popularity;
    private List<String> albums;
    public Artist() {
        this.artistId = null;
        this.followers = null;
        this.name = null;
        this.spotifyLink = null;
        this.popularity = null;
        this.artistImg=null;
    }
}
