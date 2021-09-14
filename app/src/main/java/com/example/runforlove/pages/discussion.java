package com.example.runforlove.pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.runforlove.R;
import com.example.runforlove.mesClasses.Conversation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class discussion extends AppCompatActivity implements View.OnClickListener{
    private SharedPreferences sharedPreferences;
    private static final String User_Values="User_Datas";

    LinearLayout mesConversations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);
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

        //Pour créer une nouvelle conversation
        Button nouvelle=(Button)findViewById(R.id.nouvelleConversation);
        nouvelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent principal = new Intent(discussion.this, nouvelleDiscussion.class);
                startActivity(principal);
            }
        });

        //On affiche toutes les conversations de l'utilisateur
        mesConversations=(LinearLayout)findViewById(R.id.mesConversations);
        afficherConversations();
    }

    //On affiche les conversations
    private void afficherConversations(){
        sharedPreferences =getSharedPreferences(User_Values,MODE_PRIVATE);
        conversationExistantes(sharedPreferences.getString("User_Pseudo", ""), new FirestoreDiscussionsCallback() {
            @Override
            public void onCallback(ArrayList<Conversation> liste) {
                for(short i=0;i<liste.size();i++){
                    creerBouton(liste.get(i),i);
                }
            }
        });
    }

    //On crée un bouton pour chaque conversation
    public void creerBouton(final Conversation current, int i){
        LinearLayout.LayoutParams paramButton = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,1000f);
        paramButton.setMargins(0,5,0,5);
        Typeface face = ResourcesCompat.getFont(discussion.this, R.font.simonetta_black);

        final Button a=new Button(discussion.this);
        a.setBackgroundResource(R.drawable.custom_btn1);
        a.setLayoutParams(paramButton);
        a.setText(current.getNom());
        a.setTypeface(face);
        a.setTextColor(getResources().getColor(R.color.couleurAuthentification));
        a.setTextSize(15);
        a.setHint(String.valueOf(i));
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ancienneActivite = new Intent(discussion.this, maDiscussion.class);
                int j= Integer.parseInt(a.getHint().toString());
                ancienneActivite.putExtra("Nom",current.getNom());

                startActivity(ancienneActivite);
            }
        });
        mesConversations.addView(a);
    }

    public interface FirestoreDiscussionsCallback{
        void onCallback(ArrayList<Conversation> liste);
    }
    //On récupère les conversations de l'utilisateur
    //Chque utilisateur possède une liste des noms des conversations auxquels il a accès
    public static void conversationExistantes(String pseudo, final FirestoreDiscussionsCallback firestoreDiscussionsCallback)
    {
        CollectionReference docRef = FirebaseFirestore.getInstance().collection("datas").document("mesdiscussions").collection(pseudo);
        docRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            ArrayList<Conversation> maListe=new ArrayList<>();
                            for(DocumentSnapshot documentSnapshot:task.getResult()){
                                Conversation miennes=documentSnapshot.toObject(Conversation.class);
                                maListe.add(miennes);
                            }
                            firestoreDiscussionsCallback.onCallback(maListe);
                        }else {
                            Log.e("activiteExistantes","erreur");
                        }
                    }
                });
        return ;
    }

    //Retour sur la home après appui sur le bouton retour
    @Override
    public void onBackPressed() {
        Intent principal = new Intent(discussion.this, Principal.class);
        startActivity(principal);
        finish();
        return;
    }

    @Override
    public void onClick(View v) {
        Intent principal = new Intent(discussion.this, Principal.class);
        Intent event = new Intent(discussion.this, event.class);
        Intent activite = new Intent(discussion.this, activite.class);
        Intent discussion = new Intent(discussion.this, discussion.class);
        Intent profil = new Intent(discussion.this, profil.class);
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
            case R.id.vers_profil:
                startActivity(profil);
                finish();
                break;
        }
    }
}
