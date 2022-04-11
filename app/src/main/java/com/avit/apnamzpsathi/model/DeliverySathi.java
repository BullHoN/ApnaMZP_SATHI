package com.avit.apnamzpsathi.model;

public class DeliverySathi {
    private String phoneNo;
    private String latitude;
    private String longitude;

    public DeliverySathi(String phoneNo, String latitude, String longitude) {
        this.phoneNo = phoneNo;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
