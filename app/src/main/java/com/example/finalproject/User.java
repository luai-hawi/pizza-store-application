package com.example.finalproject;

public class User {
    private String email;
    private String phone;
    private String firstname;
    private String lastname;
    private boolean gender;
    private String password;
    private boolean usermode;
    private byte[] picture;

    public User(String email, String phone, String firstname, String lastname, boolean gender, String password, boolean usermode) {
        this.email = email;
        this.phone = phone;
        this.firstname = firstname;
        this.lastname = lastname;
        this.gender = gender;
        this.password = password;
        this.usermode=usermode;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public void setUsermode(boolean usermode) {
        this.usermode = usermode;
    }

    public boolean isUsermode() {
        return usermode;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public boolean isGender() {
        return gender;
    }

    public String getPassword() {
        return password;
    }
}
