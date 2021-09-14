package com.example.runforlove.pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.runforlove.R;
import com.example.runforlove.mesClasses.Activite;
import com.example.runforlove.mesClasses.FirebaseActivite;

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
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class nouvelleActivite extends AppCompatActivity implements View.OnClickListener{
    SharedPreferences sharedPreferences;
    private static final String User_Values="User_Datas";

    Button Valider;
    EditText Nom;
    EditText Infos;
    EditText Distance;

    String nom;
    String infos;
    String dateActivite;
    float distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nouvelle_activite);
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

        Valider=(Button)findViewById(R.id.enregistrer);
        Valider.setOnClickListener(this);
        Nom=(EditText)findViewById(R.id.nouvelleActiviteNom);
        Infos=(EditText)findViewById(R.id.nouvelleActiviteInfos);
        Distance=(EditText)findViewById(R.id.nouvelleActiviteDistance);

        //On récupère les valeurs stockées dans le bundle
        if(savedInstanceState != null)
        {
            nom=savedInstanceState.getString("Nom");
            infos=savedInstanceState.getString("Infos");
            distance=savedInstanceState.getFloat("Distance");
        }
    }

    //On sauvegarde les variables dans le bundle
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        savedInstanceState.putString("Nom", nom);
        savedInstanceState.putString("Infos", infos);
        savedInstanceState.putFloat("Distance", distance);

        super.onSaveInstanceState(savedInstanceState);
    }

    public void onClick(View v) {
        Intent principal = new Intent(nouvelleActivite.this, Principal.class);
        Intent event = new Intent(nouvelleActivite.this, event.class);
        Intent activite = new Intent(nouvelleActivite.this, activite.class);
        Intent discussion = new Intent(nouvelleActivite.this, discussion.class);
        Intent profil = new Intent(nouvelleActivite.this, profil.class);
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
            case R.id.enregistrer:
                //Si le formulaire est correctement rempli
                if(valide()){
                    sharedPreferences =getSharedPreferences(User_Values,MODE_PRIVATE);
                    Activite a=new Activite(nom,infos,dateActivite,distance);
                    //Si une activité du même nom n'existe pas déjà
                    FirebaseActivite.activiteExists(nom, sharedPreferences.getString("User_Pseudo", ""), new FirebaseActivite.FirestoreCallback() {
                        @Override
                        public void onCallback(Boolean exist) {
                            if(!exist){
                                //on crée l'activité
                                Activite a=new Activite(nom,infos,dateActivite,distance);
                                FirebaseActivite.createActivite(a, sharedPreferences.getString("User_Pseudo", ""), new FirebaseActivite.FirestoreCreateCallback() {
                                    @Override
                                    public void onCallback() {
                                        creerNotificationNouvelleActivite(distance);
                                        Intent activite = new Intent(nouvelleActivite.this, activite.class);
                                        startActivity(activite);
                                        finish();
                                    }
                                });
                            }else{
                                Toast.makeText(nouvelleActivite.this,getString(R.string.same),Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                break;
        }
    }

    //Si le formulaire est valide
    public Boolean valide(){
        if(Nom.getText().toString().equals("")  || Distance.getText().toString().equals("")){
            Toast.makeText(nouvelleActivite.this,getString(R.string.champ_vide),Toast.LENGTH_LONG).show();
            return false;
        }
        else{
            nom=Nom.getText().toString();
            infos=Infos.getText().toString();
            distance= Float.parseFloat(Distance.getText().toString());
            Date now = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
            dateActivite=formatter.format(now);
        }
        return true;
    }

    private void creerNotificationNouvelleActivite(float km) {
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
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(nouvelleActivite.this);
        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(nouvelleActivite.this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.logo)
                .setContentTitle("RunForLove")
                .setContentText(getString(R.string.notifNActivite)+" "+String.valueOf(km)+" km")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setPriority(Notification.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setAutoCancel(true);
        // notificationId est un identificateur unique par notification qu'il vous faut définir
        notificationManager.notify(1, notifBuilder.build());
    }
}
