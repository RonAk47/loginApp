package com.login.aimware.loginapp;


public class UserProfile {

    private String userAge;
    private String userEmail;
    private String userName;

    public UserProfile(){
    }

    public UserProfile(String userAge, String userEmail, String userName) {
        this.userAge = userAge;
        this.userEmail = userEmail;
        this.userName = userName;
    }
    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
