package com.example.runforlove.pages;

import androidx.appcompat.app.AppCompatActivity;
import com.example.runforlove.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class event extends AppCompatActivity implements View.OnClickListener{
    Button nouvelEvent;
    Button mesEvents;
    Button dansVille;
    Button aVenir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
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

        //Gestion des pages des fonctionnalités liées aux événements
        nouvelEvent=(Button)findViewById(R.id.nouvelEvent);
        nouvelEvent.setOnClickListener(this);
        mesEvents=(Button)findViewById(R.id.mesEvents);
        mesEvents.setOnClickListener(this);
        dansVille=(Button)findViewById(R.id.dansVille);
        dansVille.setOnClickListener(this);
        aVenir=(Button)findViewById(R.id.aVenir);
        aVenir.setOnClickListener(this);
    }


    //Retour sur la home après appui sur le bouton retour
    @Override
    public void onBackPressed() {
        Intent principal = new Intent(event.this, Principal.class);
        startActivity(principal);
        finish();
        return;
    }

    @Override
    public void onClick(View v) {
        Intent principal = new Intent(event.this, Principal.class);
        Intent event = new Intent(event.this, event.class);
        Intent activite = new Intent(event.this, activite.class);
        Intent discussion = new Intent(event.this, discussion.class);
        Intent profil = new Intent(event.this, profil.class);
        Intent nouvelEvent = new Intent(event.this, nouvelEvent.class);
        Intent mesEvents = new Intent(event.this, mesEvents.class);
        Intent dansVille = new Intent(event.this, dansVille.class);
        Intent aVenir = new Intent(event.this, aVenir.class);
        switch (v.getId())
        {
            case R.id.vers_new_activite:
                startActivity(activite);
                finish();
                break;
            case R.id.vers_home:
                startActivity(principal);
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
            case R.id.nouvelEvent:
                startActivity(nouvelEvent);
                break;
            case R.id.mesEvents:
                startActivity(mesEvents);
                break;
            case R.id.dansVille:
                startActivity(dansVille);
                break;
            case R.id.aVenir:
                startActivity(aVenir);
                break;
        }
    }
}
