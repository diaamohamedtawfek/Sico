package com.example.sico.Models;

public class HistoryData {
    String id_item,id_user,date;

    public HistoryData(String id_item, String id_user, String date) {
        this.id_item = id_item;
        this.id_user = id_user;
        this.date = date;
    }

    public HistoryData() {
    }

    public String getId_item() {
        return id_item;
    }

    public void setId_item(String id_item) {
        this.id_item = id_item;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
