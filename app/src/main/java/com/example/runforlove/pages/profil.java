package com.example.runforlove.pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.runforlove.R;
import com.example.runforlove.mesClasses.FirebaseUser;
import com.example.runforlove.mesClasses.User;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Objects;

public class profil extends AppCompatActivity implements View.OnClickListener{
    SharedPreferences sharedPreferences;
    private static final String User_Values="User_Datas";

    ImageView profilGenre;
    EditText profilPseudo;
    EditText profilAnnee;
    EditText profilMdp;
    EditText profilMdpConfirme;
    EditText profilVille;

    TextView personnaliteNom;
    TextView personnaliteDescription;
    Button personnaliteImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
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

        //Profil
        sharedPreferences =getSharedPreferences(User_Values,MODE_PRIVATE);

        //widgets
        profilGenre=(ImageView)findViewById(R.id.profilGenre);
        sharedPreferences =getSharedPreferences(User_Values,MODE_PRIVATE);
        if(sharedPreferences.getBoolean("User_Genre",true))
        {
            profilGenre.setImageResource(R.drawable.ic_femme);
        }

        profilPseudo=(EditText)findViewById(R.id.profilPseudo);
        profilPseudo.setText(sharedPreferences.getString("User_Pseudo",""));
        profilAnnee=(EditText)findViewById(R.id.profilAnnee);
        profilAnnee.setText(String.valueOf(sharedPreferences.getInt("User_dateNaissance",0)));
        profilMdp=(EditText)findViewById(R.id.profilMdp);
        profilMdp.setHint("************");
        profilMdpConfirme=(EditText)findViewById(R.id.profilMdpConfirme);
        profilMdpConfirme.setHint("************");
        profilVille=(EditText)findViewById(R.id.profilVille);
        profilVille.setText(sharedPreferences.getString("User_Ville",""));

        Button modifier=(Button)findViewById(R.id.modifier);
        modifier.setOnClickListener(this);
        Button deconnexion=(Button)findViewById(R.id.deconnexion);
        deconnexion.setOnClickListener(this);

        //Profil
        personnaliteNom=(TextView)findViewById(R.id.personnaliteNom);
        personnaliteDescription=(TextView)findViewById(R.id.personnaliteDescription);
        personnaliteImage=(Button)findViewById(R.id.personnaliteImage);
        personnaliteImage.setOnClickListener(this);
        if(sharedPreferences.getString("User_MBTI","").toString().equals("")){
            personnaliteNom.setText(R.string.inconnu);
            personnaliteDescription.setText(R.string.abrege_inconnu);
            personnaliteImage.setBackgroundResource(R.drawable.mbti_inconnu);

        }
        else{
            personnaliteNom.setText(getResources().getIdentifier(sharedPreferences.getString("User_MBTI","").toString(),"string","com.example.runforlove"));
            personnaliteDescription.setText(getResources().getIdentifier("abrege_"+sharedPreferences.getString("User_MBTI","").toString(),"string","com.example.runforlove"));
            personnaliteImage.setBackgroundResource(getResources().getIdentifier("mbti_"+sharedPreferences.getString("User_MBTI","").toString(),"drawable","com.example.runforlove"));
        }

    }

    //Retour sur la home après appui sur le bouton retour
    @Override
    public void onBackPressed() {
        Intent principal = new Intent(profil.this, Principal.class);
        startActivity(principal);
        finish();
        return;
    }

    @Override
    public void onClick(View v) {
        Intent principal = new Intent(profil.this, Principal.class);
        Intent event = new Intent(profil.this, event.class);
        Intent mbti = new Intent(profil.this, Mbti.class);
        Intent activite = new Intent(profil.this, activite.class);
        Intent discussion = new Intent(profil.this, discussion.class);
        Intent profil = new Intent(profil.this, profil.class);
        switch (v.getId())
        {
            case R.id.modifier:
                modifier();
                break;
            case R.id.deconnexion:
                deconnecter();
                finish();
                break;
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
            case R.id.personnaliteImage:
                startActivity(mbti);
                break;
        }
    }

    //Pour se déconnecter
    void deconnecter()
    {
        //On met à "" la valeur du pseudo, l'utilisateur ne sera donc pas détecté comme identifié
        sharedPreferences =getSharedPreferences(User_Values,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("User_Pseudo","");
        editor.commit();

        creerNotificationDeco();

        Intent connexion = new Intent(profil.this, SplashScreenActivity.class);
        startActivity(connexion);
        finish();
    }

    //Pour modifier le profil
    void modifier()
    {
        //Si les champs ne sont pas vides et que le mot de passe est confirmé
        if(!profilMdpConfirme.getText().toString().equals("") && profilMdpConfirme.getText().toString().equals(profilMdp.getText().toString()))
        {
            int currentAnnée = Calendar.getInstance().get(Calendar.YEAR);
            //Que la personne est majeure
            if(currentAnnée-Integer.parseInt(profilAnnee.getText().toString())>18 && 1950<Integer.parseInt(profilAnnee.getText().toString()))
            {
                //Que les champs ne sont pas vides
                if(!profilPseudo.getText().toString().equals("") && !profilVille.getText().toString().equals(""))
                {
                    sharedPreferences =getSharedPreferences(User_Values,MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    //On modifie l'utilisateur
                    User user = new User(profilPseudo.getText().toString(), Integer.parseInt(profilAnnee.getText().toString()),
                            profilMdp.getText().toString(), sharedPreferences.getString("User_MBTI",""),
                            sharedPreferences.getBoolean("User_Genre",false), profilVille.getText().toString());
                    FirebaseUser.createUser(user);

                    creerNotificationModif();

                    editor.putString("User_Pseudo",profilPseudo.getText().toString());
                    editor.putInt("User_dateNaissance",Integer.parseInt(profilAnnee.getText().toString()));
                    editor.putString("User_Ville",profilVille.getText().toString());
                    editor.commit();
                }
                else
                {
                    Toast.makeText(profil.this,getString(R.string.champ_vide),Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                Toast.makeText(profil.this,getString(R.string.echec_modifierAge),Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(profil.this,getString(R.string.echec_modifierMDP),Toast.LENGTH_LONG).show();
        }
    }

    private void creerNotificationDeco() {
        final String CHANNEL_ID = "CHANNEL_ID";
        // Créer le NotificationChannel, seulement pour API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification channel name";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription("Notification channel description");
            // Enregister le canal sur le système : attention de ne plus rien modifier après
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
        }
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(profil.this);
        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(profil.this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.logo)
                .setContentTitle("RunForLove")
                .setContentText(getString(R.string.notifDeco))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setPriority(Notification.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setAutoCancel(true);
        // notificationId est un identificateur unique par notification qu'il vous faut définir
        notificationManager.notify(1, notifBuilder.build());
    }

    private void creerNotificationModif() {
        final String CHANNEL_ID = "CHANNEL_ID";
        // Créer le NotificationChannel, seulement pour API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification channel name";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription("Notification channel description");
            // Enregister le canal sur le système : attention de ne plus rien modifier après
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
        }
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(profil.this);
        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(profil.this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.logo)
                .setContentTitle("RunForLove")
                .setContentText(getString(R.string.notifModif))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setPriority(Notification.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setAutoCancel(true);
        // notificationId est un identificateur unique par notification qu'il vous faut définir
        notificationManager.notify(1, notifBuilder.build());
    }
}