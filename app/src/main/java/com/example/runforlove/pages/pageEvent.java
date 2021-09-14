package com.example.runforlove.pages;

import com.example.runforlove.R;
import com.example.runforlove.mesClasses.FirebaseActivite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class pageEvent extends AppCompatActivity implements View.OnClickListener{
    SharedPreferences sharedPreferences;
    private static final String User_Values="User_Datas";

    TextView pageEventNom;
    TextView pageEventAuteur;
    TextView pageEventVille;
    TextView pageEventInfos;
    TextView pageEventDate;

    ImageView personnaliteAuteurImage;

    Button supprimerEvent;

    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_event);
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

        pageEventNom=(TextView)findViewById(R.id.pageEventNom);
        pageEventAuteur=(TextView)findViewById(R.id.pageEventAuteur);
        pageEventVille=(TextView)findViewById(R.id.pageEventVille);
        pageEventInfos=(TextView)findViewById(R.id.pageEventInfos);
        pageEventDate=(TextView)findViewById(R.id.pageEventDate);

        personnaliteAuteurImage=(ImageView)findViewById(R.id.personnaliteAuteurImage);

        supprimerEvent=(Button)findViewById(R.id.supprimerEvent);
        supprimerEvent.setOnClickListener(this);

        bundle = getIntent().getExtras();

        pageEventNom.setText(bundle.getString("nomEvent"));
        pageEventAuteur.setText(bundle.getString("auteurEvent"));
        pageEventVille.setText(bundle.getString("villeEvent"));
        pageEventInfos.setText(bundle.getString("infosEvent"));
        pageEventDate.setText(bundle.getString("dateEvent"));

        //Si l'utilisateur n'a pas créé l'event on affiche la personnalité de la personne qui a créé l'event
        sharedPreferences =getSharedPreferences(User_Values,MODE_PRIVATE);
        if(!sharedPreferences.getString("User_Pseudo","").equals(bundle.getString("auteurEvent"))){
            LinearLayout a=(LinearLayout)findViewById(R.id.enleverEvent);
            a.removeView(supprimerEvent);
            personnaliteAuteurImage.setBackgroundResource(getResources().getIdentifier("mbti_"+bundle.getString("personnaliteEvent"),"drawable","com.example.runforlove"));
        }
    }

    @Override
    public void onClick(View v) {
        Intent principal = new Intent(pageEvent.this, Principal.class);
        Intent event = new Intent(pageEvent.this, event.class);
        Intent activite = new Intent(pageEvent.this, activite.class);
        Intent discussion = new Intent(pageEvent.this, discussion.class);
        Intent profil = new Intent(pageEvent.this, profil.class);
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
            case R.id.supprimerEvent:
                FirebaseActivite.getActvitesCollection().document("events").collection("toutes").document(bundle.getString("nomEvent")).delete();
                startActivity(event);
                finish();
                break;
        }
    }
}
