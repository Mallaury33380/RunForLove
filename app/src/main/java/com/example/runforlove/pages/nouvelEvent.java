package com.example.runforlove.pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.runforlove.R;
import com.example.runforlove.mesClasses.Event;
import com.example.runforlove.mesClasses.FirebaseEvent;

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
import java.util.Locale;
import java.util.Objects;

public class nouvelEvent extends AppCompatActivity implements View.OnClickListener{
    SharedPreferences sharedPreferences;
    private static final String User_Values="User_Datas";

    EditText nomEvent;
    EditText villeEvent;
    EditText infosEvent;
    EditText dateEvent;

    Button creerEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nouvel_event);
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

        nomEvent=(EditText)findViewById(R.id.nouvelEventNom);
        villeEvent=(EditText)findViewById(R.id.nouvelEventVille);
        infosEvent=(EditText)findViewById(R.id.nouvelEventInfos);
        dateEvent=(EditText)findViewById(R.id.nouvelEventDate);

        creerEvent=(Button)findViewById(R.id.nouvelEventCreer);
        creerEvent.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent principal = new Intent(nouvelEvent.this, Principal.class);
        Intent event = new Intent(nouvelEvent.this, event.class);
        Intent activite = new Intent(nouvelEvent.this, activite.class);
        Intent discussion = new Intent(nouvelEvent.this, discussion.class);
        Intent profil = new Intent(nouvelEvent.this, profil.class);
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
            case R.id.nouvelEventCreer:
                //On crée un nouvel événement
                creerEvent();
                break;
        }
    }

    //On crée un nouvel événement
    public void creerEvent(){
        //Si le formulaire est correctement rempli
        if(correctementRempli()){
            sharedPreferences =getSharedPreferences(User_Values,MODE_PRIVATE);

            final Event a=new Event(sharedPreferences.getString("User_Pseudo", ""),sharedPreferences.getString("User_MBTI", ""),nomEvent.getText().toString(),villeEvent.getText().toString(),infosEvent.getText().toString(),dateEvent.getText().toString());
            //Si un événement du même nom n'existe pas déjà
            FirebaseEvent.eventExists(a.getNomEvent(), new FirebaseEvent.FirestoreEventExistCallback() {
                @Override
                public void onCallback(Boolean exist) {
                    if(!exist){
                        //On crée l'événement
                        FirebaseEvent.createEvent(a, new FirebaseEvent.FirestoreCreateEventCallback() {
                            @Override
                            public void onCallback() {
                                //Notification
                                creerNotificationNouvelleEvent(nomEvent.getText().toString());

                                Toast.makeText(nouvelEvent.this,getResources().getString(R.string.eventAEteCrees),Toast.LENGTH_LONG).show();
                                Intent mesEvents = new Intent(nouvelEvent.this, mesEvents.class);
                                startActivity(mesEvents);
                                finish();
                            }
                        });

                    }else{
                        Toast.makeText(nouvelEvent.this,getResources().getString(R.string.eventExist),Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    //Si le formulaire est correctement rempli
    public Boolean correctementRempli(){
        if(nomEvent.getText().toString().equals("") || villeEvent.getText().toString().equals("") || infosEvent.getText().toString().equals("") || dateEvent.getText().toString().equals("")){
            Toast.makeText(nouvelEvent.this,getResources().getString(R.string.champ_vide),Toast.LENGTH_LONG).show();
            return false;
        }else if(!dateValide(dateEvent.getText().toString())){
            Toast.makeText(nouvelEvent.this,getResources().getString(R.string.datePasValide),Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    //Si la date de l'événement est valide
    public boolean dateValide(String dateToValidate){

        if(dateToValidate == null){
            return false;
        }

        try {
            String dateFromat="yyyy/MM/dd";
            SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
            sdf.setLenient(false);
            //if not valid, it will throw ParseException
            Date date = sdf.parse(dateToValidate);

            String currentDate = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
            Date now=sdf.parse(currentDate);
            if(now.after(date)){
                return false;
            }

        } catch (ParseException e) {

            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void creerNotificationNouvelleEvent(String event) {
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
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(nouvelEvent.this);
        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(nouvelEvent.this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.logo)
                .setContentTitle("RunForLove")
                .setContentText(event+" "+getString(R.string.notifNEvent))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setPriority(Notification.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setAutoCancel(true);
        // notificationId est un identificateur unique par notification qu'il vous faut définir
        notificationManager.notify(1, notifBuilder.build());
    }

}
