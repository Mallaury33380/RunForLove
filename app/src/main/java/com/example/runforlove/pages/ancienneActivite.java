package com.example.runforlove.pages;

import androidx.appcompat.app.AppCompatActivity;
import com.example.runforlove.R;
import com.example.runforlove.mesClasses.FirebaseActivite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ancienneActivite extends AppCompatActivity implements View.OnClickListener{
    SharedPreferences sharedPreferences;
    private static final String User_Values="User_Datas";

    Button supprimer;

    TextView ancienneActiviteNom;
    TextView ancienneActiviteInfos;
    TextView ancienneActiviteDistance;
    TextView ancienneActiviteDate;

    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ancienne_activite);
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

        ancienneActiviteNom=(TextView)findViewById(R.id.ancienneActiviteNom);
        ancienneActiviteInfos=(TextView)findViewById(R.id.ancienneActiviteInfos);
        ancienneActiviteDistance=(TextView)findViewById(R.id.ancienneActiviteDistance);
        ancienneActiviteDate=(TextView)findViewById(R.id.ancienneActiviteDate);
        supprimer=(Button)findViewById(R.id.supprimer);
        supprimer.setOnClickListener(this);

        bundle = getIntent().getExtras();

        //on affiche les données relatives à l'activité
        ancienneActiviteNom.setText(bundle.getString("Nom"));
        ancienneActiviteInfos.setText(bundle.getString("InfosComplementaires"));
        ancienneActiviteDistance.setText(getResources().getString(R.string.activiteCree)+" "+bundle.getString("dateActivite"));
        ancienneActiviteDate.setText(getResources().getString(R.string.parcouru)+" "+bundle.getFloat("Distance")+" km");
    }

    @Override
    public void onClick(View v) {
        Intent principal = new Intent(ancienneActivite.this, Principal.class);
        Intent event = new Intent(ancienneActivite.this, event.class);
        Intent activite = new Intent(ancienneActivite.this, activite.class);
        Intent discussion = new Intent(ancienneActivite.this, discussion.class);
        Intent profil = new Intent(ancienneActivite.this, profil.class);
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
            case R.id.supprimer:
                //On supprime l'activité
                sharedPreferences =getSharedPreferences(User_Values,MODE_PRIVATE);
                FirebaseActivite.getActvitesCollection().document("activites").collection(sharedPreferences.getString("User_Pseudo","")).document(bundle.getString("Nom")).delete();
                startActivity(activite);
                finish();
                break;
        }
    }
}
