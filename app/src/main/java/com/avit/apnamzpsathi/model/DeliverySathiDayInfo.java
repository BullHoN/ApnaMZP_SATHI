package com.avit.apnamzpsathi.model;

public class DeliverySathiDayInfo {

    private String totalEarnings;
    private String earnings;
    private String incentives;
    private int noOfOrders;

    public DeliverySathiDayInfo(String totalEarnings, String earnings, String incentives, int noOfOrders) {
        this.totalEarnings = totalEarnings;
        this.earnings = earnings;
        this.incentives = incentives;
        this.noOfOrders = noOfOrders;
    }

    public DeliverySathiDayInfo(String totalEarnings, int noOfOrders) {
        this.totalEarnings = totalEarnings;
        this.noOfOrders = noOfOrders;
    }

    public String getEarnings() {
        return earnings;
    }

    public String getIncentives() {
        return incentives;
    }

    public String getTotalEarnings() {
        return totalEarnings;
    }

    public int getNoOfOrders() {
        return noOfOrders;
    }
}
