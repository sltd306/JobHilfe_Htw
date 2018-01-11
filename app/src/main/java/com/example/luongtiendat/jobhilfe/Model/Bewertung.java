package com.example.luongtiendat.jobhilfe.Model;

/**
 * Created by Luong Tien Dat on 11.01.2018.
 */

public class Bewertung {

    private String from;
    private String rate;
    private String text;

    public Bewertung() {
    }

    public Bewertung(String from, String rate, String text) {
        this.from = from;
        this.rate = rate;
        this.text = text;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
