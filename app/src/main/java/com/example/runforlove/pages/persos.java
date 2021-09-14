package com.example.runforlove.pages;

import androidx.appcompat.app.AppCompatActivity;
import com.example.runforlove.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class persos extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persos);
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


        //Pour chaque persoonalité possible on crée u bouton vers la page pour afficher les caractéristiques

        Button mbti_enfj=(Button)findViewById(R.id.mbti_enfj);
        mbti_enfj.setOnClickListener(this);
        Button mbti_enfp=(Button)findViewById(R.id.mbti_enfp);
        mbti_enfp.setOnClickListener(this);
        Button mbti_entj=(Button)findViewById(R.id.mbti_entj);
        mbti_entj.setOnClickListener(this);
        Button mbti_entp=(Button)findViewById(R.id.mbti_entp);
        mbti_entp.setOnClickListener(this);
        Button mbti_esfj=(Button)findViewById(R.id.mbti_esfj);
        mbti_esfj.setOnClickListener(this);
        Button mbti_esfp=(Button)findViewById(R.id.mbti_esfp);
        mbti_esfp.setOnClickListener(this);
        Button mbti_estj=(Button)findViewById(R.id.mbti_estj);
        mbti_estj.setOnClickListener(this);
        Button mbti_estp=(Button)findViewById(R.id.mbti_estp);
        mbti_estp.setOnClickListener(this);

        Button mbti_infj=(Button)findViewById(R.id.mbti_infj);
        mbti_infj.setOnClickListener(this);
        Button mbti_infp=(Button)findViewById(R.id.mbti_infp);
        mbti_infp.setOnClickListener(this);
        Button mbti_intj=(Button)findViewById(R.id.mbti_intj);
        mbti_intj.setOnClickListener(this);
        Button mbti_intp=(Button)findViewById(R.id.mbti_intp);
        mbti_intp.setOnClickListener(this);
        Button mbti_isfj=(Button)findViewById(R.id.mbti_isfj);
        mbti_isfj.setOnClickListener(this);
        Button mbti_isfp=(Button)findViewById(R.id.mbti_isfp);
        mbti_isfp.setOnClickListener(this);
        Button mbti_istj=(Button)findViewById(R.id.mbti_istj);
        mbti_istj.setOnClickListener(this);
        Button mbti_istp=(Button)findViewById(R.id.mbti_istp);
        mbti_istp.setOnClickListener(this);
    }

    //On passe en extra le nom du type de personnalité
    public void onClick(View v) {
        Intent principal = new Intent(persos.this, Principal.class);
        Intent event = new Intent(persos.this, event.class);
        Intent mbti = new Intent(persos.this, Mbti.class);
        Intent activite = new Intent(persos.this, activite.class);
        Intent discussion = new Intent(persos.this, discussion.class);
        Intent profil = new Intent(persos.this, profil.class);
        Intent perso = new Intent(persos.this, perso.class);
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
            case R.id.mbti_enfj:
                perso.putExtra("perso","enfj");
                startActivity(perso);
                break;
            case R.id.mbti_enfp:
                perso.putExtra("perso","enfp");
                startActivity(perso);
                break;
            case R.id.mbti_entj:
                perso.putExtra("perso","entj");
                startActivity(perso);
                break;
            case R.id.mbti_entp:
                perso.putExtra("perso","entp");
                startActivity(perso);
                break;
            case R.id.mbti_esfj:
                perso.putExtra("perso","esfj");
                startActivity(perso);
                break;
            case R.id.mbti_esfp:
                perso.putExtra("perso","esfp");
                startActivity(perso);
                break;
            case R.id.mbti_estj:
                perso.putExtra("perso","estj");
                startActivity(perso);
                break;
            case R.id.mbti_estp:
                perso.putExtra("perso","estp");
                startActivity(perso);
                break;
            case R.id.mbti_infj:
                perso.putExtra("perso","infj");
                startActivity(perso);
                break;
            case R.id.mbti_infp:
                perso.putExtra("perso","infp");
                startActivity(perso);
                break;
            case R.id.mbti_intj:
                perso.putExtra("perso","intj");
                startActivity(perso);
                break;
            case R.id.mbti_intp:
                perso.putExtra("perso","intp");
                startActivity(perso);
                break;
            case R.id.mbti_isfj:
                perso.putExtra("perso","isfj");
                startActivity(perso);
                break;
            case R.id.mbti_isfp:
                perso.putExtra("perso","isfp");
                startActivity(perso);
                break;
            case R.id.mbti_istj:
                perso.putExtra("perso","istj");
                startActivity(perso);
                break;
            case R.id.mbti_istp:
                perso.putExtra("perso","istp");
                startActivity(perso);
                break;
        }
    }
}
