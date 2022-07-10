package com.avit.apnamzpsathi.model;

public class LoginPostData {
    private String phoneNo;
    private String password;

    public LoginPostData(String phoneNo, String password) {
        this.phoneNo = phoneNo;
        this.password = password;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getPassword() {
        return password;
    }
}
