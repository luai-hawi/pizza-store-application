package com.example.finalproject;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Orders {
    int id;
    String email;
    double price;
    String time;
    ArrayList<PizzaNS> ordersExtend;

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public ArrayList<PizzaNS> getOrdersExtend() {
        return ordersExtend;
    }

    public String getEmail() {
        return email;
    }


    public void setOrdersExtend(ArrayList<PizzaNS> ordersExtend) {
        this.ordersExtend = ordersExtend;
    }

    public Orders() {
    }

    public double getPrice() {
        return price;
    }

    public String getTime() {
        return time;
    }


    public void setPrice(double price) {
        this.price = price;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Orders(int id, String email, double price, String time) {
        this.id=id;
        this.email = email;
        this.price = price;
        this.time = time;
    }

    public Orders(String email, double price, ArrayList<PizzaNS> ordersExtend) {
        this.email = email;
        this.price = price;
        this.ordersExtend = ordersExtend;
    }
}
