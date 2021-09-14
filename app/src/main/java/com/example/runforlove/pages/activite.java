package com.example.runforlove.pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.runforlove.R;
import com.example.runforlove.mesClasses.Activite;
import com.example.runforlove.mesClasses.FirebaseActivite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class activite extends AppCompatActivity implements View.OnClickListener{
    SharedPreferences sharedPreferences;
    private static final String User_Values="User_Datas";

    LinearLayout mesActivites;
    Button nouvelleActivite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activite);
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

        nouvelleActivite=(Button)findViewById(R.id.nouvelleActivite);
        nouvelleActivite.setOnClickListener(this);

        //Affciher les activités créés par l'utilisateur
        genererAnciennesActites();
    }

    //Retour sur la home après appui sur le bouton retour
    @Override
    public void onBackPressed() {
        Intent principal = new Intent(activite.this, Principal.class);
        startActivity(principal);
        finish();
        return;
    }

    @Override
    public void onClick(View v) {
        Intent principal = new Intent(activite.this, Principal.class);
        Intent event = new Intent(activite.this, event.class);
        Intent discussion = new Intent(activite.this, discussion.class);
        Intent profil = new Intent(activite.this, profil.class);
        Intent NouvelleActivite = new Intent(activite.this, nouvelleActivite.class);
        switch (v.getId())
        {
            case R.id.vers_event:
                startActivity(event);
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
            case R.id.nouvelleActivite:
                startActivity(NouvelleActivite);
                finish();
                break;
        }
    }

    //Affciher les activités créés par l'utilisateur
    void genererAnciennesActites()
    {
        mesActivites=(LinearLayout)findViewById(R.id.mesActivites);
        sharedPreferences =getSharedPreferences(User_Values,MODE_PRIVATE);

        //On récupère ses activités
        FirebaseActivite.activiteExistantes(sharedPreferences.getString("User_Pseudo", ""), new FirebaseActivite.FirestoreExistantesCallback() {
            @Override
            public void onCallback(final ArrayList<Activite> liste) {
                LinearLayout.LayoutParams paramButton = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,1000f);
                paramButton.setMargins(0,5,0,5);
                Typeface face = ResourcesCompat.getFont(activite.this, R.font.simonetta_black);

                //On les affiches en tant que bouttons
                for(short i=0;i<liste.size();i++){
                    final Button a=new Button(activite.this);
                    a.setBackgroundResource(R.drawable.custom_btn1);
                    a.setLayoutParams(paramButton);
                    a.setText(liste.get(i).getNom());
                    a.setTypeface(face);
                    a.setTextColor(getResources().getColor(R.color.couleurAuthentification));
                    a.setTextSize(15);
                    a.setHint(String.valueOf(i));
                    a.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent ancienneActivite = new Intent(activite.this, ancienneActivite.class);
                            int j= Integer.parseInt(a.getHint().toString());
                            ancienneActivite.putExtra("Nom",liste.get(j).getNom());
                            ancienneActivite.putExtra("InfosComplementaires",liste.get(j).getInfosComplementaires());
                            ancienneActivite.putExtra("dateActivite",liste.get(j).getDateActivite());
                            ancienneActivite.putExtra("Distance",liste.get(j).getDistance());

                            startActivity(ancienneActivite);
                        }
                    });
                    //On affiche à la suite les boutons
                    mesActivites.addView(a);
                }
            }
        });
    }
}
