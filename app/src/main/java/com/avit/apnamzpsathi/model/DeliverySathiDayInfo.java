package com.avit.apnamzpsathi.model;

public class DeliverySathiDayInfo {

    private String totalEarnings;
    private int noOfOrders;

    public DeliverySathiDayInfo(String totalEarnings, int noOfOrders) {
        this.totalEarnings = totalEarnings;
        this.noOfOrders = noOfOrders;
    }

    public String getTotalEarnings() {
        return totalEarnings;
    }

    public int getNoOfOrders() {
        return noOfOrders;
    }
}
