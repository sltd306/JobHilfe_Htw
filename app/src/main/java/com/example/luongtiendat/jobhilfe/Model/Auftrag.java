package com.example.luongtiendat.jobhilfe.Model;

/**
 * Created by Luong Tien Dat on 08.01.2018.
 */

public class Auftrag {

    private String titel;
    private String arbeitgerber;
    private String stellen_beschreibung;
    private String arbeit_ort;
    private String beginn_tatigkeit;
    private String arbeit_zeit;
    private String vergutung;
    private String status;

    public Auftrag() {
    }

    public Auftrag(String titel, String arbeitgerber, String stellen_beschreibung, String arbeit_ort, String beginn_tatigkeit, String arbeit_zeit, String vergutung, String status) {
        this.titel = titel;
        this.arbeitgerber = arbeitgerber;
        this.stellen_beschreibung = stellen_beschreibung;
        this.arbeit_ort = arbeit_ort;
        this.beginn_tatigkeit = beginn_tatigkeit;
        this.arbeit_zeit = arbeit_zeit;
        this.vergutung = vergutung;
        this.status = status;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getArbeitgerber() {
        return arbeitgerber;
    }

    public void setArbeitgerber(String arbeitgerber) {
        this.arbeitgerber = arbeitgerber;
    }

    public String getStellen_beschreibung() {
        return stellen_beschreibung;
    }

    public void setStellen_beschreibung(String stellen_beschreibung) {
        this.stellen_beschreibung = stellen_beschreibung;
    }

    public String getArbeit_ort() {
        return arbeit_ort;
    }

    public void setArbeit_ort(String arbeit_ort) {
        this.arbeit_ort = arbeit_ort;
    }

    public String getBeginn_tatigkeit() {
        return beginn_tatigkeit;
    }

    public void setBeginn_tatigkeit(String beginn_tatigkeit) {
        this.beginn_tatigkeit = beginn_tatigkeit;
    }

    public String getArbeit_zeit() {
        return arbeit_zeit;
    }

    public void setArbeit_zeit(String arbeit_zeit) {
        this.arbeit_zeit = arbeit_zeit;
    }

    public String getVergutung() {
        return vergutung;
    }

    public void setVergutung(String vergutung) {
        this.vergutung = vergutung;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}