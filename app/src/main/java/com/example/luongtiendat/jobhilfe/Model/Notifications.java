package com.example.luongtiendat.jobhilfe.Model;

/**
 * Created by Luong Tien Dat on 11.01.2018.
 */

public class Notifications {

    private String from;
    private String type;

    public Notifications(String from, String type) {
        this.from = from;
        this.type = type;
    }

    public Notifications() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
