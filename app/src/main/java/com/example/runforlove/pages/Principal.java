package com.example.runforlove.pages;

import androidx.appcompat.app.AppCompatActivity;
import com.example.runforlove.R;
import com.example.runforlove.mesClasses.FirebaseActivite;
import com.example.runforlove.mesClasses.FirebaseEvent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Principal extends AppCompatActivity implements View.OnClickListener{
    SharedPreferences sharedPreferences;
    private static final String User_Values="User_Datas";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

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

        final Button button_event=(Button)findViewById(R.id.exemple_event);
        button_event.setOnClickListener(this);
        final Button button_activite=(Button)findViewById(R.id.exemple_activite_recente);
        button_activite.setOnClickListener(this);
        Button button_discussion=(Button)findViewById(R.id.exemple_discussion);
        button_discussion.setOnClickListener(this);
        Button button_personnalite = findViewById(R.id.exemple_personnalite);
        button_personnalite.setOnClickListener(this);

        //Si l'utilisateur a réalisé le test de personnalité on affiche le nom
        sharedPreferences =getSharedPreferences(User_Values,MODE_PRIVATE);
        if(!sharedPreferences.getString("User_MBTI","").toString().equals("")){
            button_personnalite.setText(getResources().getIdentifier(sharedPreferences.getString("User_MBTI",""),"string","com.example.runforlove"));
            String a=getResources().getString(R.string.etreUn)+" "+  button_personnalite.getText();
            button_personnalite.setText(a);
        }

        //Si l'utilisateur a déjà créé une activité
        FirebaseActivite.activiteVide(sharedPreferences.getString("User_Pseudo", ""), new FirebaseActivite.FirestoreCallback() {
            @Override
            public void onCallback(Boolean etre) {
                if(!etre){
                    button_activite.setText(getString(R.string.ajouterActivite));
                }
            }
        });

        //Si l'utilisateur a déjà créé un événement
        FirebaseEvent.eventMiensExist(sharedPreferences.getString("User_Pseudo", ""), new FirebaseEvent.FirestoreEventExistCallback() {
            @Override
            public void onCallback(Boolean etre) {
                if(etre){
                    button_event.setText(getString(R.string.ajouterEvent));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent principal = new Intent(Principal.this, Principal.class);
        Intent event = new Intent(Principal.this, event.class);
        Intent mbti = new Intent(Principal.this, Mbti.class);
        Intent activite = new Intent(Principal.this, activite.class);
        Intent discussion = new Intent(Principal.this, discussion.class);
        Intent profil = new Intent(Principal.this, profil.class);
        switch (v.getId())
        {
            case R.id.vers_new_activite:
                startActivity(activite);
                finish();
                break;
            case R.id.vers_event:
                startActivity(event);
                finish();
                break;
            case R.id.vers_discussion:
                startActivity(discussion);
                finish();
                break;
            case R.id.vers_profil:
                startActivity(profil);
                finish();
                break;
            case R.id.exemple_event:
                startActivity(event);
                break;
            case R.id.exemple_activite_recente:
                startActivity(activite);
                break;
            case R.id.exemple_discussion:
                startActivity(discussion);
                break;
            case R.id.exemple_personnalite:
                startActivity(mbti);
                break;
        }
    }
}
