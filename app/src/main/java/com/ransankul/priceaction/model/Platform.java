package com.ransankul.priceaction.model;

public class Platform {

    private int id;

    private String platform;

    public Platform() {
    }

    public Platform(int id, String platform) {
        this.id = id;
        this.platform = platform;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }


}
