package com.example.runforlove.pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.runforlove.R;
import com.example.runforlove.mesClasses.FirebaseUser;
import com.example.runforlove.mesClasses.User;

import java.util.Calendar;
import java.util.Objects;

public class inscription extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    private static final String User_Values="User_Datas";

    public static String val_pseudo;
    public static String val_mdp;
    public static String val_mdpConfirme;
    public static String val_dateNaissance;
    public static Boolean val_genre;
    public static String val_ville;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        try {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){
            Log.e("hide action bar",e.toString());
        }

        Button btnValidation= (Button) findViewById(R.id.validation);

        btnValidation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText dateNaissance =(EditText) findViewById(R.id.saisir_dateNaissance);
                EditText pseudo =(EditText) findViewById(R.id.saisir_pseudo);
                EditText mdp=(EditText) findViewById(R.id.saisir_mdp);
                EditText mdpConfirme=(EditText) findViewById(R.id.confirmer_mdp);
                RadioButton femme=(RadioButton) findViewById(R.id.femme);
                EditText ville=(EditText) findViewById(R.id.ville);

                val_pseudo=pseudo.getText().toString();
                val_mdp=mdp.getText().toString();
                val_mdpConfirme=mdpConfirme.getText().toString();
                val_dateNaissance=dateNaissance.getText().toString();
                val_genre=femme.isChecked();
                val_ville=ville.getText().toString();

                //Si le formulaire est correctement rempli
                if(correctementRempli(val_pseudo,val_mdp,val_mdpConfirme,val_dateNaissance,val_ville))
                {
                    FirebaseUser.userExists(val_pseudo, new FirebaseUser.FirestoreCallback() {
                        @Override
                        public void onCallback(Boolean exist) {
                            //Si aucun autre utilisateur a déjà ce pseudo
                            if(!exist)
                            {
                                //On crée l'utilisateur
                                User user = new User(val_pseudo, Integer.parseInt(val_dateNaissance), val_mdp, "", val_genre, val_ville);
                                FirebaseUser.createUser(user);

                                //Et on stocke les données relatives à son compte en local
                                sharedPreferences =getSharedPreferences(User_Values,MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("User_Pseudo",val_pseudo);
                                editor.putInt("User_dateNaissance",Integer.parseInt(val_dateNaissance));
                                editor.putString("User_MBTI","");
                                editor.putBoolean("User_Genre",val_genre);
                                editor.putString("User_Ville",val_ville);
                                editor.putString("User_Mdp",val_mdp);
                                editor.commit();

                                creerNotification();

                                Intent connexion = new Intent(inscription.this, Principal.class);
                                startActivity(connexion);
                                finish();
                            }else{
                                Toast.makeText(inscription.this,getString(R.string.pseudo_utilise),Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }


    private void creerNotification() {
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
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(inscription.this);
        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(inscription.this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.logo)
                .setContentTitle("RunForLove")
                .setContentText(getString(R.string.bienvenue))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setPriority(Notification.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setAutoCancel(true);
        // notificationId est un identificateur unique par notification qu'il vous faut définir
        notificationManager.notify(1, notifBuilder.build());
    }






    //si le formulaire est correctement rempli
    Boolean correctementRempli(String val_pseudo, String val_mdp, String val_mdpConfirme,String dateNaissance,String val_ville)
    {
        if(val_pseudo.length()!=0 && val_mdp.length()!=0 && val_mdpConfirme.length()!=0 && val_ville.length()!=0)
        {
            if(val_mdp.equals(val_mdpConfirme))
            {
                int année = Integer.parseInt(dateNaissance);
                int currentAnnée = Calendar.getInstance().get(Calendar.YEAR);
                //Si elle est majeure
                if((currentAnnée-année)>18)
                {
                    //Et que l'utilisateur a entré une vraie année de naissance
                    if(année>1950)
                    {
                        return true;
                    }
                    else
                    {
                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context, getString(R.string.age_faux), duration);
                        toast.show();
                    }
                }
                else
                {
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, getString(R.string.age_minimal), duration);
                    toast.show();
                }
            }
            else
            {
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(context, getString(R.string.mdp_non_identique), duration);
                toast.show();
            }
        }
        else
        {
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, getString(R.string.champ_vide), duration);
            toast.show();
        }
        return false;
    }
}
