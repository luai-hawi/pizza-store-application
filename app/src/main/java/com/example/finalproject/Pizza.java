package com.example.finalproject;

public class Pizza {
    String name;
    String size;
    double price;
    String category;
    byte[] picture;


    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public byte[] getPicture() {
        return picture;
    }

    public Pizza(String name, String size, double price, String category) {
        this.name = name;
        this.size = size;
        this.price = price;
        this.category = category;
    }
    public Pizza(String name, String size, double price, String category, byte[] picture) {
        this.name = name;
        this.size = size;
        this.price = price;
        this.category = category;
        this.picture=picture;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public String getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }
}
