package com.example.runforlove.pages;

import androidx.appcompat.app.AppCompatActivity;
import com.example.runforlove.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

//On affiche une personnalité
public class perso extends AppCompatActivity implements View.OnClickListener{

    TextView persoNom;
    TextView persoBrief;
    TextView persoText;

    ImageView persoImage;

    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perso);
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

        //On récupère les données passées avec l'extra et on les affiches
        bundle = getIntent().getExtras();
        persoNom=(TextView)findViewById(R.id.persoNom);
        persoNom.setText(getResources().getIdentifier(bundle.getString("perso"),"string","com.example.runforlove"));

        persoBrief=(TextView)findViewById(R.id.persoBrief);
        persoBrief.setText(getResources().getIdentifier("abrege_"+bundle.getString("perso"),"string","com.example.runforlove"));

        persoText=(TextView)findViewById(R.id.persoText);
        persoText.setText(getResources().getIdentifier("description_"+bundle.getString("perso"),"string","com.example.runforlove"));

        persoImage=(ImageView)findViewById(R.id.persoImage);
        persoImage.setBackgroundResource(getResources().getIdentifier("mbti_"+bundle.getString("perso"),"drawable","com.example.runforlove"));


    }

    public void onClick(View v) {
        Intent principal = new Intent(perso.this, Principal.class);
        Intent event = new Intent(perso.this, event.class);
        Intent mbti = new Intent(perso.this, Mbti.class);
        Intent activite = new Intent(perso.this, activite.class);
        Intent discussion = new Intent(perso.this, discussion.class);
        Intent profil = new Intent(perso.this, profil.class);
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
