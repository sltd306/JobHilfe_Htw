package com.example.luongtiendat.jobhilfe.Model;

/**
 * Created by Luong Tien Dat on 08.01.2018.
 */

public class OfferUser {
    private String name;
    private String image;
    private String userId;

    public OfferUser() {
    }

    public OfferUser(String name, String image, String userId) {
        this.name = name;
        this.image = image;
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
