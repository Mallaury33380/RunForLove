package com.example.runforlove.mesClasses;

public class User {

    private String Pseudo;
    private int dateNaissance;
    private String Mdp;
    private String MBTI;
    private Boolean Genre;
    private String Ville;

    public User(){
        Pseudo="";
        dateNaissance=0;
        Mdp="";
        MBTI="";
        Genre=true;
        Ville="";

    };
    public User(String pseudo,int naissance, String mdp, String mbti,Boolean genre,String ville)
    {
        this.Pseudo=pseudo;
        this.dateNaissance=naissance;
        this.Mdp=mdp;
        this.MBTI=mbti;
        this.Genre=genre;
        this.Ville=ville;
    }

    //Getters
    public String getPseudo(){return Pseudo;}
    public int getDateNaissance(){return dateNaissance;}
    public String getMdp(){return Mdp;}
    public String getMBTI(){return MBTI;}
    public Boolean getGenre(){return Genre;}
    public String getVille(){return Ville;}

    //Setters
    public void setPseudo(String pseudo){this.Pseudo=pseudo;}
    public void setDateNaissance(int naissance){this.dateNaissance=naissance;}
    public void setMdp(String mdp){this.Mdp=mdp;}
    public void setMBTI(String mbti){this.MBTI=mbti;}
    public void setGenre(Boolean genre){this.Genre=genre;}
    public void setVille(String ville){this.Ville=ville;}

}
