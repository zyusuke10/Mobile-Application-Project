package com.example.yorktheatre;

import java.io.Serializable;

public class Performance implements Serializable {
    private int id;
    private String name;
    private String image;
    private String location;
    private String time;
    private String accessibility;
    private String price;
    private int quantity;

    private int ticket_id;

    public Performance(int id, String name, String image, String location, String time, String accessibility, String price, int quantity, int ticket_id) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.location = location;
        this.time = time;
        this.accessibility = accessibility;
        this.price = price;
        this.quantity = quantity;
        this.ticket_id = ticket_id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getLocation() {
        return location;
    }

    public String getTime() {
        return time;
    }

    public String getAccessibility() {
        return accessibility;
    }

    public String getPrice(){return price;}

    public int getTicketQuantity(){return quantity;}

    public int getTicket_id(){return ticket_id;}




}
