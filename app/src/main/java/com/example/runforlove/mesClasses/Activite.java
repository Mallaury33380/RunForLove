package com.example.runforlove.mesClasses;

public class Activite {
    private String Nom;
    private String InfosComplementaires;
    private String DateActivite;
    private float Distance;


    public Activite(){
        Nom="";
        InfosComplementaires="";
        Distance=0;
    };

    public Activite(String nom,String infosComplementaires,String datActivite,float distance){
        Nom=nom;
        InfosComplementaires=infosComplementaires;
        DateActivite=datActivite;
        Distance=distance;
    }

    //Getters
    public String getNom(){
        return Nom;
    }
    public String getInfosComplementaires(){
        return InfosComplementaires;
    }
    public  String getDateActivite(){
        return DateActivite;
    }
    public float getDistance(){
        return Distance;
    }


    //Setters
    public void setNom(String nom){
        Nom=nom;
        return;
    }
    public void setInfosComplementaires(String infosComplementaires) {
        InfosComplementaires = infosComplementaires;
        return;
    }
    public void setDateActivite(String dateActivite) {
        DateActivite = dateActivite;
        return;
    }
    public void setDistance(float distance) {
        Distance = distance;
        return;
    }
}
