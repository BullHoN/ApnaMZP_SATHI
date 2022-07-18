package com.avit.apnamzpsathi.model;

public class NetworkResponse {
    private boolean success;
    private String desc;
    private String data;
    private int status;

    public NetworkResponse(boolean success) {
        this.success = success;
    }

    public NetworkResponse(boolean success, String desc) {
        this.success = success;
        this.desc = desc;
    }

    public NetworkResponse(boolean success, String desc, String data) {
        this.success = success;
        this.desc = desc;
        this.data = data;
    }

    public NetworkResponse(boolean success, String desc, int status) {
        this.success = success;
        this.desc = desc;
        this.status = status;
    }

    public NetworkResponse(boolean success, String desc, int status, String data) {
        this.success = success;
        this.desc = desc;
        this.status = status;
        this.data = data;
    }

    public int getStatus() {
        return status;
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
