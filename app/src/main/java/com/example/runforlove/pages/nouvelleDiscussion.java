package com.example.runforlove.pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.runforlove.R;
import com.example.runforlove.mesClasses.Conversation;
import com.example.runforlove.mesClasses.FirebaseUser;
import com.example.runforlove.mesClasses.Message;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class nouvelleDiscussion extends AppCompatActivity implements View.OnClickListener{
    private SharedPreferences sharedPreferences;
    private static final String User_Values="User_Datas";

    EditText nouvelleDiscussionNom;
    EditText nouvelleDiscussionPseudo;

    Button creerDiscussion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nouvelle_discussion);
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

        nouvelleDiscussionNom=(EditText)findViewById(R.id.nouvelleDiscussionNom);
        nouvelleDiscussionPseudo=(EditText)findViewById(R.id.nouvelleDiscussionPseudo);

        creerDiscussion=(Button) findViewById(R.id.creerDiscussion);
        creerDiscussion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Si le formulaire est valide
                if(bienRempli()){
                    conversationExists(nouvelleDiscussionNom.getText().toString(), new FirestoreDiscussionCallback() {
                        @Override
                        public void onCallback(Boolean exist) {
                            //Si une conversation portant ce nom n'existe pas déjà
                            if(!exist){
                                //On crée la conversation
                                FirebaseUser.userExists(nouvelleDiscussionPseudo.getText().toString(), new FirebaseUser.FirestoreCallback() {
                                    @Override
                                    public void onCallback(Boolean etre) {
                                        if(etre){
                                            creerConversation();
                                            Toast.makeText(nouvelleDiscussion.this,getString(R.string.utilisateurAjoute),Toast.LENGTH_LONG).show();
                                        }else{
                                            Toast.makeText(nouvelleDiscussion.this,getString(R.string.utilisateurNon),Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }else{
                                Toast.makeText(nouvelleDiscussion.this,getString(R.string.conversationExist),Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

            }
        });

    }

    //On crée la conversation
    private void creerConversation(){
        sharedPreferences =getSharedPreferences(User_Values,MODE_PRIVATE);
        //On ajoute la conversation aux personnes concernées

        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        String date = df.format(new Date());

        Conversation nouvelle = new Conversation(nouvelleDiscussionNom.getText().toString(),date);

        FirebaseFirestore.getInstance().collection("datas").document("mesdiscussions").collection(nouvelleDiscussionPseudo.getText().toString()).document(nouvelleDiscussionNom.getText().toString()).set(nouvelle);
        FirebaseFirestore.getInstance().collection("datas").document("mesdiscussions").collection(sharedPreferences.getString("User_Pseudo","")).document(nouvelleDiscussionNom.getText().toString()).set(nouvelle);

        //Notification
        creerNotificationNouvelleDiscussion(nouvelleDiscussionPseudo.getText().toString());

        //On crée la conversation et on insère le premier message
        Message premier=new Message();premier.setAuteur("Service");premier.setContenu("hi");premier.setDateMessage(new Date());
        FirebaseFirestore.getInstance().collection("datas").document("discussions").collection(nouvelleDiscussionNom.getText().toString()).document("First").set(premier);
        Intent discussion = new Intent(nouvelleDiscussion.this, discussion.class);
        startActivity(discussion);
        finish();
    }


    //Bon
    @Override
    public void onClick(View v) {
        Intent principal = new Intent(nouvelleDiscussion.this, Principal.class);
        Intent event = new Intent(nouvelleDiscussion.this, event.class);
        Intent activite = new Intent(nouvelleDiscussion.this, activite.class);
        Intent discussion = new Intent(nouvelleDiscussion.this, discussion.class);
        Intent profil = new Intent(nouvelleDiscussion.this, profil.class);
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

    //Si le formulaire est bien rempli
    private Boolean bienRempli(){
        if(nouvelleDiscussionNom.getText().toString().equals("") || nouvelleDiscussionPseudo.getText().toString().equals("")){
            Toast.makeText(nouvelleDiscussion.this,getString(R.string.champ_vide),Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    //Si l'activité existe
    public static void conversationExists(String conversation, final FirestoreDiscussionCallback firestoreCallback)
    {
        CollectionReference docRef = FirebaseFirestore.getInstance().collection("datas").document("discussions").collection(conversation);
        docRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            if(task.getResult().isEmpty()){
                                firestoreCallback.onCallback(false);
                            }else{
                                firestoreCallback.onCallback(true);
                            }
                        }else {
                            Log.e("activiteExistantes","erreur");
                        }
                    }
                });
        return ;
    }

    public interface FirestoreDiscussionCallback{
        void onCallback(Boolean exist);
    }

    private void creerNotificationNouvelleDiscussion(String pseudo) {
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
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(nouvelleDiscussion.this);
        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(nouvelleDiscussion.this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.logo)
                .setContentTitle("RunForLove")
                .setContentText(getString(R.string.notifNDiscussion)+" "+pseudo)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setPriority(Notification.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setAutoCancel(true);
        // notificationId est un identificateur unique par notification qu'il vous faut définir
        notificationManager.notify(1, notifBuilder.build());
    }
}


