package com.example.runforlove.pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.runforlove.R;
import com.example.runforlove.mesClasses.FirebaseUser;
import com.example.runforlove.mesClasses.User;


public class Mbti extends AppCompatActivity implements View.OnClickListener{
    private SharedPreferences sharedPreferences;
    private static final String User_Values="User_Datas";

    private LinearLayout mbti;

    int i=0;
    int tableau[]={0,0,0,0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mbti);
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

        //On récupère les valeurs stockées dans le bundle
        if(savedInstanceState != null)
        {
            tableau=savedInstanceState.getIntArray("tableau");
            i=savedInstanceState.getInt("i");
        }

        sharedPreferences =getSharedPreferences(User_Values,MODE_PRIVATE);

        //Si l'utilisatuer n'a pas tester sa personnalité
        mbti=(LinearLayout)findViewById(R.id.mbti);
        if(sharedPreferences.getString("User_MBTI","").toString().equals("")){
            testMbti();
        }
        //Sinon on l'affiche
        else{
            afficherMbti();
        }
    }

    //On sauvegarde les variables dans le bundle
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        savedInstanceState.putIntArray("tableau", tableau);
        savedInstanceState.putInt("i", i);

        super.onSaveInstanceState(savedInstanceState);
    }

    //Test MBTI
    void testMbti(){
        //On crée des paramètres graphiques
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(150, 150);
        params.setMargins(0,20,0,10);
        Typeface face = ResourcesCompat.getFont(this, R.font.simonetta_black);

        //On crée un linéar layout pour le titre
        LinearLayout ligne=new LinearLayout(this);
        ligne.setOrientation(LinearLayout.HORIZONTAL);
        ligne.setLayoutParams(params);
        ligne.setGravity(Gravity.CENTER);

        //On affiche le titre avec le numéro de la question
        TextView nom=new TextView(this);
        nom.setId(R.id.Nomquestions);
        nom.setLayoutParams(params);
        nom.setGravity(Gravity.CENTER_VERTICAL |Gravity.CENTER);
        nom.setText(getResources().getString(R.string.test)+" "+String.valueOf(i+1)+" sur 16");
        nom.setTextColor(getResources().getColor(R.color.couleurTexteAuthentification));
        nom.setTextSize(30);
        nom.setBackground(getResources().getDrawable(R.drawable.custom_edittext));
        nom.setTypeface(face);

        //On affiche le lien vers l'ensemble des types de personnalité
        Button infosPersos=new Button(this);
        infosPersos.setBackground(getResources().getDrawable(R.drawable.ic_infos));
        infosPersos.setText("i");
        infosPersos.setGravity(Gravity.CENTER);
        infosPersos.setTypeface(face);
        infosPersos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent persos = new Intent(Mbti.this, persos.class);
                startActivity(persos);
            }
        });

        //On ajoute les composants au layout
        ligne.addView(nom,params);
        ligne.addView(infosPersos,params2);
        //On ajoute le layout
        mbti.addView(ligne,params);

        //On crée le textview de la question
        TextView question=new TextView(this);
        question.setId(R.id.questions);
        question.setLayoutParams(params);
        question.setGravity(Gravity.CENTER_VERTICAL |Gravity.CENTER);
        question.setText(getResources().getIdentifier("q"+String.valueOf(i),"string","com.example.runforlove"));
        question.setTextColor(getResources().getColor(R.color.couleurTexteAuthentification));
        question.setTextSize(30);
        question.setBackground(getResources().getDrawable(R.drawable.custom_edittext));
        question.setTypeface(face);
        //On ajoute le textview de la question à la page
        mbti.addView(question,params);

        //On crée des composants graphiques
        LinearLayout.LayoutParams paramButton = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1000f);
        params.setMargins(0,50,0,100);
        LinearLayout.LayoutParams paramText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,0.1f);
        params.setMargins(0,50,0,100);

        //On crée l'esnsemble des éléments du layout pour sélectionner la réponse
        //Textview orientation de la valeur de la réponse
        TextView Accord=new TextView(this);
        Accord.setLayoutParams(paramText);
        Accord.setGravity(Gravity.CENTER_VERTICAL |Gravity.CENTER);
        Accord.setText(getResources().getString(R.string.oui));
        Accord.setTextColor(getResources().getColor(R.color.couleurTexteAuthentification));
        Accord.setTextSize(15);
        Accord.setBackground(getResources().getDrawable(R.drawable.custom_edittext));
        Accord.setTypeface(face);

        //Textview orientation de la valeur opposée de la réponse
        TextView PasAccord=new TextView(this);
        PasAccord.setLayoutParams(paramText);
        PasAccord.setGravity(Gravity.CENTER_VERTICAL |Gravity.CENTER);
        PasAccord.setText(getResources().getString(R.string.non));
        PasAccord.setTextColor(getResources().getColor(R.color.couleurTexteAuthentification));
        PasAccord.setTextSize(15);
        PasAccord.setBackground(getResources().getDrawable(R.drawable.custom_edittext));
        PasAccord.setTypeface(face);

        //Ensemble des boutons
        Button n0=new Button(this);
        n0.setId(R.id.n0);
        n0.setBackgroundResource(R.drawable.button_mbti_1);
        n0.setLayoutParams(paramButton);
        n0.setOnClickListener(this);

        Button n1=new Button(this);
        n1.setId(R.id.n1);
        n1.setBackgroundResource(R.drawable.button_mbti_2);
        n1.setLayoutParams(paramButton);
        n1.setOnClickListener(this);

        Button n2=new Button(this);
        n2.setId(R.id.n2);
        n2.setBackgroundResource(R.drawable.button_mbti_3);
        n2.setLayoutParams(paramButton);
        n2.setOnClickListener(this);

        Button n3=new Button(this);
        n3.setId(R.id.n3);
        n3.setBackgroundResource(R.drawable.button_mbti_2);
        n3.setLayoutParams(paramButton);
        n3.setOnClickListener(this);

        Button n4=new Button(this);
        n4.setId(R.id.n4);
        n4.setBackgroundResource(R.drawable.button_mbti_1);
        n4.setLayoutParams(paramButton);
        n4.setOnClickListener(this);


        LinearLayout boutons=new LinearLayout(this);
        boutons.setOrientation(LinearLayout.HORIZONTAL);
        boutons.setId(R.id.boutons);
        boutons.setLayoutParams(paramButton);
        question.setGravity(Gravity.CENTER_VERTICAL |Gravity.CENTER);

        //On ajoute dans l'ordre de l'affichage vertical les éléments
        boutons.addView(Accord);
        boutons.addView(n0);
        boutons.addView(n1);
        boutons.addView(n2);
        boutons.addView(n3);
        boutons.addView(n4);
        boutons.addView(PasAccord);
        //On ajoute le layout des réponses à la page
        mbti.addView(boutons);
    }

    //On affiche les données relatives à la personnalité de l'utilisateur
    void afficherMbti(){
        //Elements graphiques
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(100, 100);
        params.setMargins(0,20,0,10);
        Typeface face = ResourcesCompat.getFont(this, R.font.simonetta_black);

        //Layout horizontal du nom de la page
        LinearLayout ligne=new LinearLayout(this);
        ligne.setOrientation(LinearLayout.HORIZONTAL);
        ligne.setLayoutParams(params);
        ligne.setGravity(Gravity.CENTER);


        TextView nom=new TextView(this);
        nom.setLayoutParams(params);
        nom.setGravity(Gravity.CENTER_VERTICAL );
        nom.setText(getResources().getIdentifier(sharedPreferences.getString("User_MBTI","").toString(),"string","com.example.runforlove"));
        nom.setTextColor(getResources().getColor(R.color.couleurTexteAuthentification));
        nom.setTextSize(40);
        nom.setBackground(getResources().getDrawable(R.drawable.custom_edittext));
        nom.setTypeface(face);

        Button infosPersos=new Button(this);
        infosPersos.setBackground(getResources().getDrawable(R.drawable.ic_infos));
        infosPersos.setText("i");
        infosPersos.setGravity(Gravity.CENTER);
        infosPersos.setTypeface(face);
        infosPersos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent persos = new Intent(Mbti.this, persos.class);
                startActivity(persos);
            }
        });

        //On ajoute les éléments au layout
        ligne.addView(nom,params);
        ligne.addView(infosPersos,params2);
        //On ajoute le layout à la page
        mbti.addView(ligne,params);

        //O affiche l'image correspondant à la personnalité
        ImageView image=new ImageView(this);
        image.setLayoutParams(params);
        image.setBackgroundResource(getResources().getIdentifier("mbti_"+sharedPreferences.getString("User_MBTI","").toString(),"drawable","com.example.runforlove"));
        mbti.addView(image,params);

        //On affiche le résumé de la personnalité
        TextView abrege=new TextView(this);
        abrege.setLayoutParams(params);
        abrege.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER);
        abrege.setText(getResources().getIdentifier("abrege_"+sharedPreferences.getString("User_MBTI","").toString(),"string","com.example.runforlove"));
        abrege.setTextColor(getResources().getColor(R.color.couleurTexteAuthentification));
        abrege.setTextSize(20);
        abrege.setBackground(getResources().getDrawable(R.drawable.custom_edittext));
        abrege.setTypeface(face);
        mbti.addView(abrege,params);

        //On affiche les explications de la personnalité
        TextView description=new TextView(this);
        description.setLayoutParams(params);
        description.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER);
        description.setText(getResources().getIdentifier("description_"+sharedPreferences.getString("User_MBTI","").toString(),"string","com.example.runforlove"));
        description.setTextColor(getResources().getColor(R.color.couleurTexteAuthentification));
        description.setTextSize(20);
        description.setBackground(getResources().getDrawable(R.drawable.custom_edittext));
        description.setTypeface(face);
        mbti.addView(description,params);
    }

    @Override
    public void onClick(View v) {
        Intent principal = new Intent(Mbti.this, Principal.class);
        Intent event = new Intent(Mbti.this, event.class);
        Intent activite = new Intent(Mbti.this, activite.class);
        Intent discussion = new Intent(Mbti.this, discussion.class);
        Intent profil = new Intent(Mbti.this, profil.class);

        TextView question=new TextView(this);
        question=(TextView)findViewById(R.id.questions);

        TextView nom=new TextView(this);
        nom=(TextView)findViewById(R.id.Nomquestions);

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
            case R.id.n0:
                tableau[i%4]+=2;
                break;
            case R.id.n1:
                tableau[i%4]++;
                break;
            case R.id.n2:
                break;
            case R.id.n3:
                tableau[i%4]-=1;
                break;
            case R.id.n4:
                tableau[i%4]-=2;
                break;
        }

        //Si on appuie sur un bouton réponse sa passe à la question suivante
        i++;
        if(sharedPreferences.getString("User_MBTI","").toString().equals("")){
            if(i<16){
                question.setText(getResources().getIdentifier("q"+String.valueOf(i),"string","com.example.runforlove"));
                nom.setText(getResources().getString(R.string.test)+" "+String.valueOf(i+1)+" sur 16");
            }else if(i==16){
                sauvegardeMbti();
            }
        }
    }

    //A la fin on défini le type de personnalité et on la sauvegarde
    void sauvegardeMbti()
    {
        String personnalite="";
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(tableau[0]>0)
        {
            personnalite+="e";
        }else{
            personnalite+="i";
        }
        if(tableau[1]>0)
        {
            personnalite+="s";
        }else{
            personnalite+="n";
        }
        if(tableau[2]>0)
        {
            personnalite+="t";
        }else{
            personnalite+="f";
        }
        if(tableau[3]>0)
        {
            personnalite+="j";
        }else{
            personnalite+="p";
        }
        Toast.makeText(Mbti.this,personnalite,Toast.LENGTH_LONG).show();

        editor.putString("User_MBTI",personnalite);
        editor.commit();

        User user = new User(sharedPreferences.getString("User_Pseudo",""), sharedPreferences.getInt("User_dateNaissance",0), sharedPreferences.getString("User_Mdp",""), personnalite, sharedPreferences.getBoolean("User_Genre",true), sharedPreferences.getString("User_Ville",""));
        FirebaseUser.createUser(user);

        mbti.removeAllViews();
        afficherMbti();
    }

}

