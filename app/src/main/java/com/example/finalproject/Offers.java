package com.example.finalproject;

import java.util.ArrayList;

public class Offers {
    int id;
    String period;
    String date;
    double price;
    double oldPrice;
    byte[] picture;
    ArrayList<PizzaNS> pizzas;

    public Offers(int id, String period, String date, double price, double oldPrice, byte[] picture) {
        this.id = id;
        this.period = period;
        this.date = date;
        this.price = price;
        this.oldPrice = oldPrice;
        this.picture = picture;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setOldPrice(double oldPrice) {
        this.oldPrice = oldPrice;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public void setPizzas(ArrayList<PizzaNS> pizzas) {
        this.pizzas = pizzas;
    }

    public String getPeriod() {
        return period;
    }

    public String getDate() {
        return date;
    }

    public double getPrice() {
        return price;
    }

    public double getOldPrice() {
        return oldPrice;
    }

    public byte[] getPicture() {
        return picture;
    }

    public ArrayList<PizzaNS> getPizzas() {
        return pizzas;
    }
}
