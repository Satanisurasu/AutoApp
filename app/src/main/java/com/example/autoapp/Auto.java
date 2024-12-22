package com.example.autoapp;

public class Auto {

    private String producer;
    private String model;
    private int price;
    private String imageUrl;

    public Auto(String producer, String model, int price, String imageUrl) {
        this.producer = producer;
        this.model = model;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    // Геттеры и сеттеры

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
