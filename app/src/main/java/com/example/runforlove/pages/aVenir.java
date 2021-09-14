package com.example.runforlove.pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.runforlove.R;
import com.example.runforlove.mesClasses.Event;
import com.example.runforlove.mesClasses.FirebaseEvent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class aVenir extends AppCompatActivity implements View.OnClickListener{
    SharedPreferences sharedPreferences;
    private static final String User_Values="User_Datas";

    LinearLayout eventProchains;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_venir);
        try {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){
            Log.e("hide action bar",e.toString());
        }
        Button ic_activite=(Button)findViewById(R.id.vers_new_activite);
        ic_activite.setOnClickListener(this);
        Button ic_event=(Button)findViewById(R.id.vers_event);
        ic_event.setOnClickListener(this);
        Button ic_home=(Button)findViewById(R.id.vers_home);
        ic_home.setOnClickListener(this);
        Button ic_discussion=(Button)findViewById(R.id.vers_discussion);
        ic_discussion.setOnClickListener(this);
        Button ic_profil=(Button)findViewById(R.id.vers_profil);
        ic_profil.setOnClickListener(this);

        //On affiche les événements futurs
        afficherEventsProchains();
    }
    //On affiche les événements futurs
    public void afficherEventsProchains(){
        sharedPreferences =getSharedPreferences(User_Values,MODE_PRIVATE);

        FirebaseEvent.eventAvoirExist(new FirebaseEvent.FirestoreEventExistCallback() {
            @Override
            public void onCallback(Boolean ya) {
                //Si il existe des événements
                if(ya){
                    //On les récupère
                    FirebaseEvent.eventProchains( new FirebaseEvent.FirestoreEventsCreeCallback() {
                        @Override
                        public void onCallback(final ArrayList<Event> liste) {
                            //Et on affiche les événements sous forme de boutons
                            eventProchains=(LinearLayout)findViewById(R.id.eventProchains);
                            LinearLayout.LayoutParams paramButton = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,1000f);
                            paramButton.setMargins(0,5,0,5);
                            Typeface face = ResourcesCompat.getFont(aVenir.this, R.font.simonetta_black);

                            for(short i=0;i<liste.size();i++){
                                final Button a=new Button(aVenir.this);
                                a.setBackgroundResource(R.drawable.custom_btn1);
                                a.setLayoutParams(paramButton);

                                if(liste.get(i).getPersonnaliteEvent().equals("")){
                                    a.setText(liste.get(i).getNomEvent());
                                }else{
                                    a.setText(getResources().getIdentifier(liste.get(i).getPersonnaliteEvent(),"string","com.example.runforlove"));
                                    a.setText(a.getText().toString()+": "+liste.get(i).getNomEvent());
                                }

                                a.setTypeface(face);
                                a.setTextColor(getResources().getColor(R.color.couleurAuthentification));
                                a.setTextSize(15);
                                a.setHint(String.valueOf(i));
                                a.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent pageEvent = new Intent(aVenir.this, pageEvent.class);
                                        int j= Integer.parseInt(a.getHint().toString());
                                        pageEvent.putExtra("auteurEvent",liste.get(j).getAuteurEvent());
                                        pageEvent.putExtra("personnaliteEvent",liste.get(j).getPersonnaliteEvent());
                                        pageEvent.putExtra("nomEvent",liste.get(j).getNomEvent());
                                        pageEvent.putExtra("villeEvent",liste.get(j).getVilleEvent());
                                        pageEvent.putExtra("infosEvent",liste.get(j).getInfosEvent());
                                        pageEvent.putExtra("dateEvent",liste.get(j).getDateEvent());

                                        startActivity(pageEvent);
                                    }
                                });
                                //On ajoute les boutons
                                eventProchains.addView(a);
                            }
                        }
                    });
                }else{
                    Toast.makeText(aVenir.this,getResources().getString(R.string.pasProchain),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent principal = new Intent(aVenir.this, Principal.class);
        Intent event = new Intent(aVenir.this, event.class);
        Intent activite = new Intent(aVenir.this, activite.class);
        Intent discussion = new Intent(aVenir.this, discussion.class);
        Intent profil = new Intent(aVenir.this, profil.class);
        switch (v.getId())
        {
            case R.id.vers_new_activite:
                startActivity(activite);
                break;
            case R.id.vers_event:
                startActivity(event);
                break;
            case R.id.vers_home:
                startActivity(principal);
                break;
            case R.id.vers_discussion:
                startActivity(discussion);
                break;
            case R.id.vers_profil:
                startActivity(profil);
                break;
        }
    }
}