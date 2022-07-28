package com.avit.apnamzpsathi.model;

public class DeliverySathiDayInfo {

    private String totalEarnings;
    private String earnings;
    private String incentives;
    private int noOfOrders;
    private boolean isMonthly;

    public DeliverySathiDayInfo(String totalEarnings, String earnings, String incentives, int noOfOrders, boolean isMonthly) {
        this.totalEarnings = totalEarnings;
        this.earnings = earnings;
        this.incentives = incentives;
        this.noOfOrders = noOfOrders;
        this.isMonthly = isMonthly;
    }

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

    public boolean isMonthly() {
        return isMonthly;
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
