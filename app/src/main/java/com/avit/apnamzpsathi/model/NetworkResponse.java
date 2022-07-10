package com.avit.apnamzpsathi.model;

public class NetworkResponse {
    private boolean success;
    private String desc;
    private String data;

    public NetworkResponse(boolean success, String desc, String data) {
        this.success = success;
        this.desc = desc;
        this.data = data;
    }

    public NetworkResponse(boolean success) {
        this.success = success;
    }

    public String getDesc() {
        return desc;
    }

    public String getData() {
        return data;
    }

    public boolean isSuccess() {
        return success;
    }
}
