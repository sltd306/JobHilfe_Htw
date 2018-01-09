package com.example.luongtiendat.jobhilfe.Model;

/**
 * Created by Luong Tien Dat on 09.01.2018.
 */

public class Bewerbung {
    private String UserId;
    private String datum;
    private String status;
    private String AuftragId;
    private String AuftragTitel;
    private String request_type;

    public Bewerbung() {
    }

    public Bewerbung(String userId, String datum, String status, String auftragId, String auftragTitel, String request_type) {
        UserId = userId;
        this.datum = datum;
        this.status = status;
        AuftragId = auftragId;
        AuftragTitel = auftragTitel;
        this.request_type = request_type;
    }

    public String getAuftragTitel() {
        return AuftragTitel;
    }

    public void setAuftragTitel(String auftragTitel) {
        AuftragTitel = auftragTitel;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuftragId() {
        return AuftragId;
    }

    public void setAuftragId(String auftragId) {
        AuftragId = auftragId;
    }

    public String getRequest_type() {
        return request_type;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }
}
