package com.avit.apnamzpsathi.model;

public class UserInfo {
    private String name;
    private String latitude;
    private String longitude;
    private String phoneNo;
    private String rawAddress;

    public UserInfo(String name, String latitude, String longitude, String phoneNo, String rawAddress) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phoneNo = phoneNo;
        this.rawAddress = rawAddress;
    }

    public String getName() {
        return name;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getRawAddress() {
        return rawAddress;
    }
}
