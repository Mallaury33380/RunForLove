package com.example.runforlove.pages;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.example.runforlove.R;
import com.example.runforlove.mesClasses.FirebaseUser;
import com.example.runforlove.mesClasses.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Objects;

public class connexion extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    private static final String User_Values="User_Datas";

    public static String val_pseudo;
    public static String val_mdp;

    public static User user=new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
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
                EditText pseudo =(EditText) findViewById(R.id.pseudo);
                EditText mdp=(EditText) findViewById(R.id.mdp);

                val_pseudo=pseudo.getText().toString();
                val_mdp=mdp.getText().toString();
                //on vérifie que le formulaire est bien rempli
                if(correctementRempli(val_pseudo,val_mdp))
                {
                    //Et on essaie d'authentifier la personne
                    authertifie(val_pseudo, val_mdp, new FirestoreCallback() {
                        @Override
                        public void onCallback(User user) {
                            if(val_pseudo.equals(user.getPseudo()) && val_mdp.toString().equals(user.getMdp()))
                            {
                                //Si il est authentifié on sauvegarde ses données
                                sharedPreferences =getSharedPreferences(User_Values,MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                editor.putString("User_Pseudo",val_pseudo);
                                editor.putInt("User_dateNaissance",user.getDateNaissance());
                                editor.putString("User_MBTI",user.getMBTI());
                                editor.putBoolean("User_Genre",user.getGenre());
                                editor.putString("User_Ville",user.getVille());
                                editor.putString("User_Mdp",user.getMdp());
                                editor.commit();

                                //Notification
                                creerNotification();

                                //Et on le redirige vers la page principale
                                Intent connexion = new Intent(connexion.this, Principal.class);
                                startActivity(connexion);
                                finish();
                            }else{
                                Toast.makeText(connexion.this,getString(R.string.identifiantsIncorrects),Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }


    public interface FirestoreCallback{
        void onCallback(User user);
    }
    //On authentifie la personne
    void authertifie(String val_pseudo, String val_mdp, final FirestoreCallback firestoreCallback)
    {
        //On récupère les informations relatives au pseudo entré
        DocumentReference docRef = FirebaseUser.getUsersCollection().document(val_pseudo);
        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            user=documentSnapshot.toObject(User.class);
                        }
                        else
                        {
                            Toast.makeText(connexion.this,getString(R.string.identifiantsIncorrects),Toast.LENGTH_LONG).show();
                        }
                        firestoreCallback.onCallback(user);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(connexion.this,getString(R.string.echec_authentification),Toast.LENGTH_LONG).show();
                    }
                });
        return;
    }
    //On vérifie que le formulaire est correctement rempli
    Boolean correctementRempli(String val_pseudo, String val_mdp)
    {
        if(val_pseudo.length()!=0 && val_mdp.length()!=0)
        {
            return true;
        }
        else
        {
            Context context = getApplicationContext();
            CharSequence text = getString(R.string.champ_vide);
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        return false;
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
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(connexion.this);
        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(connexion.this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.logo)
                .setContentTitle("RunForLove")
                .setContentText(getString(R.string.notifConnexion))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setPriority(Notification.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setAutoCancel(true);
        // notificationId est un identificateur unique par notification qu'il vous faut définir
        notificationManager.notify(2, notifBuilder.build());
    }

}
