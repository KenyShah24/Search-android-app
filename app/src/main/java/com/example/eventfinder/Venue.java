package com.example.eventfinder;

public class Venue {
    private String name;

    public String getCityState() {
        return cityState;
    }

    public void setCityState(String cityState) {
        this.cityState = cityState;
    }

    private String cityState;
    private String address;

    public Venue(String name, String address, String phone, String lat, String lng, String openHours, String genRule, String childRule, String cityState) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.lat = lat;
        this.lng = lng;
        this.openHours = openHours;
        this.genRule = genRule;
        this.childRule = childRule;
        this.cityState=cityState;
    }

    public Venue() {
        this.name = null;
        this.address = null;
        this.phone = null;
        this.lat = "";
        this.lng = "";
        this.openHours = null;
        this.genRule = null;
        this.childRule = null;
        this.cityState = null;
    }

    private String phone;
    private String lat;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getOpenHours() {
        return openHours;
    }

    public void setOpenHours(String openHours) {
        this.openHours = openHours;
    }

    public String getGenRule() {
        return genRule;
    }

    public void setGenRule(String genRule) {
        this.genRule = genRule;
    }

    public String getChildRule() {
        return childRule;
    }

    public void setChildRule(String childRule) {
        this.childRule = childRule;
    }

    private String lng;
    private String openHours;
    private String genRule;
    private String childRule;
}
