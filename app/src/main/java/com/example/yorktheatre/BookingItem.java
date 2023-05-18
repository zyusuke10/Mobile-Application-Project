package com.example.yorktheatre;

import java.io.Serializable;

public class BookingItem implements Serializable {
    private String title;
    private int id;
    private String image;

    private String time;

    private float price;

    private int quantity;

    private int selectedQty;

    public BookingItem(int id, String title, String image, String time, float price, int quantity, int selectedQty) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.time = time;
        this.price = price;
        this.quantity = quantity;
        this.selectedQty = selectedQty;
    }

    public int getId(){return id;}
    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getTime(){return time;}

    public float getPrice(){return price;}

    public int getQuantity(){return quantity;}

    public int getSelectedQuantity() {
        return selectedQty;
    }
}








