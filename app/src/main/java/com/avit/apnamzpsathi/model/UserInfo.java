package com.avit.apnamzpsathi.model;

public class UserInfo {
    private String name;
    private String latitude;
    private String longitude;
    private String phoneNo;
    private String rawAddress;
    private String landmark;
    private String houseNo;
    private boolean isVissible;

    public UserInfo(String name, String latitude, String longitude, String phoneNo,
                    String rawAddress, String landmark, String houseNo, boolean isVissible) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phoneNo = phoneNo;
        this.rawAddress = rawAddress;
        this.landmark = landmark;
        this.houseNo = houseNo;
        this.isVissible = isVissible;
    }

    public UserInfo(String name, String latitude, String longitude, String phoneNo, String rawAddress) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phoneNo = phoneNo;
        this.rawAddress = rawAddress;
        this.isVissible = false;
    }

    public String getLandmark() {
        return landmark;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public boolean isVissible() {
        return isVissible;
    }

    public void setVissible(boolean vissible) {
        isVissible = vissible;
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
