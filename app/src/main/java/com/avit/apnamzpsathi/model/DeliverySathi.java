package com.avit.apnamzpsathi.model;

public class DeliverySathi {
    private String phoneNo;
    private String latitude;
    private String longitude;
    private String fcmId;
    private Number earnings;
    private Number incentives;
    private Number deliveries;

    public DeliverySathi(String phoneNo, String latitude, String longitude) {
        this.phoneNo = phoneNo;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public DeliverySathi(String phoneNo, Number earnings, Number incentives, Number deliveries) {
        this.phoneNo = phoneNo;
        this.earnings = earnings;
        this.incentives = incentives;
        this.deliveries = deliveries;
    }

    public Number getEarnings() {
        return earnings;
    }

    public Number getIncentives() {
        return incentives;
    }

    public Number getDeliveries() {
        return deliveries;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public DeliverySathi(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public void setFcmId(String fcmId) {
        this.fcmId = fcmId;
    }

    public String getFcmId() {
        return fcmId;
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
