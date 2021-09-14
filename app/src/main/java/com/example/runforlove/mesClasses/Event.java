package com.example.runforlove.mesClasses;

public class Event {
    private String auteurEvent;
    private String personnaliteEvent;
    private String nomEvent;
    private String villeEvent;
    private String infosEvent;
    private String dateEvent;

    public Event(){}
    public Event(String auteur,String personnalite,String nom,String ville,String infos,String dateevent){
        auteurEvent=auteur;
        personnaliteEvent=personnalite;
        nomEvent=nom;
        villeEvent=ville;
        infosEvent=infos;
        dateEvent=dateevent;
    }

    //Gets
    public String getAuteurEvent(){return auteurEvent;}
    public String getPersonnaliteEvent(){return personnaliteEvent;}
    public String getNomEvent(){return nomEvent;}
    public String getVilleEvent(){return villeEvent;}
    public String getInfosEvent(){return infosEvent;}
    public String getDateEvent(){return dateEvent;}

    //Sets
    public void setAuteurEvent(String auteur){
        auteurEvent=auteur;
    }
    public void setPersonnaliteEvent(String personnalite){
        personnaliteEvent=personnalite;
    }
    public void setNomEvent(String nom){
        nomEvent=nom;
    }
    public void setVilleEvent(String ville){
        villeEvent=ville;
    }
    public void setInfosEvent(String infos){
        infosEvent=infos;
    }
    public void setDateEvent(String dateevent){
        dateEvent=dateevent;
    }
}
