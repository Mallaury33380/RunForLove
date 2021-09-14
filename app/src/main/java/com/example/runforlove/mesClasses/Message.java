package com.example.runforlove.mesClasses;

import java.util.Date;

public class Message {
    private String Auteur;
    private String Contenu;
    private Date DateMessage;

    //Sets
    public void setAuteur(String auteur){
        Auteur=auteur;
    }
    public void setContenu(String contenu){
        Contenu=contenu;
    }
    public void setDateMessage(Date date){
        DateMessage=date;
    }

    //Gets
    public String getAuteur(){
        return Auteur;
    }
    public String getContenu(){
        return Contenu;
    }
    public Date getDateMessage(){
        return DateMessage;
    }

}
