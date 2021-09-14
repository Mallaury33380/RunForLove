package com.example.runforlove.pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.runforlove.R;
import com.example.runforlove.mesClasses.Event;
import com.example.runforlove.mesClasses.FirebaseEvent;

import java.util.ArrayList;

public class mesEvents extends AppCompatActivity implements View.OnClickListener{
    SharedPreferences sharedPreferences;
    private static final String User_Values="User_Datas";

    LinearLayout eventAMoi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mes_events);
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

        //On affiche mes événements
        afficherMesProchains();
    }

    //On affiche mes événements
    public void afficherMesProchains(){
        sharedPreferences =getSharedPreferences(User_Values,MODE_PRIVATE);
        //Si j'ai des événements
        FirebaseEvent.eventMiensExist(sharedPreferences.getString("User_Pseudo",""),new FirebaseEvent.FirestoreEventExistCallback() {
            @Override
            public void onCallback(Boolean ya) {
                if(ya){
                    //On récupère les événements
                    FirebaseEvent.eventMiens(sharedPreferences.getString("User_Pseudo",""), new FirebaseEvent.FirestoreEventsCreeCallback() {
                        @Override
                        public void onCallback(final ArrayList<Event> liste) {
                            eventAMoi=(LinearLayout)findViewById(R.id.eventAMoi);
                            LinearLayout.LayoutParams paramButton = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,1000f);
                            paramButton.setMargins(0,5,0,5);
                            Typeface face = ResourcesCompat.getFont(mesEvents.this, R.font.simonetta_black);
                            //On affiche les événements
                            for(short i=0;i<liste.size();i++){
                                final Button a=new Button(mesEvents.this);
                                a.setBackgroundResource(R.drawable.custom_btn1);
                                a.setLayoutParams(paramButton);

                                a.setText(liste.get(i).getNomEvent());

                                a.setTypeface(face);
                                a.setTextColor(getResources().getColor(R.color.couleurAuthentification));
                                a.setTextSize(15);
                                a.setHint(String.valueOf(i));
                                a.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent pageEvent = new Intent(mesEvents.this, pageEvent.class);
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
                                //Ajoute les événements
                                eventAMoi.addView(a);
                            }
                        }
                    });
                }else{
                    Toast.makeText(mesEvents.this,getResources().getString(R.string.pasMiens),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent principal = new Intent(mesEvents.this, Principal.class);
        Intent event = new Intent(mesEvents.this, event.class);
        Intent activite = new Intent(mesEvents.this, activite.class);
        Intent discussion = new Intent(mesEvents.this, discussion.class);
        Intent profil = new Intent(mesEvents.this, profil.class);
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
