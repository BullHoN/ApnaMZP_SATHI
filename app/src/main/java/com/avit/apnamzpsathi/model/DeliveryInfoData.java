package com.avit.apnamzpsathi.model;

public class DeliveryInfoData {
    private String message;
    private String value;

    public DeliveryInfoData(String message, String value) {
        this.message = message;
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public String getValue() {
        return value;
    }
}
