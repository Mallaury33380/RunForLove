package com.example.runforlove.mesClasses;

import java.util.Date;

public class Conversation {
    private String Nom;
    private String date;

    public void setDate(String date) {
        this.date = date;
    }

    public void setNom(String nom) {
        Nom = nom;
    }

    public String getDate() {
        return date;
    }

    public String getNom() {
        return Nom;
    }
    public Conversation(){}
    public Conversation(String nom,String date2){
        Nom=nom;
        date=date2;
    }
}
