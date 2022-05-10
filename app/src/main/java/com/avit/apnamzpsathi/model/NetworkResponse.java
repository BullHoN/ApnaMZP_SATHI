package com.avit.apnamzpsathi.model;

public class NetworkResponse {
    private boolean success;

    public NetworkResponse(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
