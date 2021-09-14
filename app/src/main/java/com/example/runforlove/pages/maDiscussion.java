package com.example.runforlove.pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.runforlove.R;
import com.example.runforlove.mesClasses.Conversation;
import com.example.runforlove.mesClasses.Event;
import com.example.runforlove.mesClasses.FirebaseUser;
import com.example.runforlove.mesClasses.Message;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import javax.annotation.Nullable;

public class maDiscussion extends AppCompatActivity implements View.OnClickListener{
    private SharedPreferences sharedPreferences;
    private static final String User_Values="User_Datas";

    Button btn_envoyer;
    Button ajouterpersonne;
    Button supprimerconversation;
    EditText ecriretexte;
    Bundle bundle;
    TextView nomconversation;

    private CollectionReference currentDiscussion;

    LinearLayout lesMessages;
    ScrollView scrollMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ma_discussion);
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

        ajouterpersonne=(Button)findViewById(R.id.ajouterpersonne);
        ajouterpersonne.setOnClickListener(this);
        supprimerconversation=(Button)findViewById(R.id.supprimerconversation);
        supprimerconversation.setOnClickListener(this);
        btn_envoyer=(Button)findViewById(R.id.btn_envoyer);
        btn_envoyer.setOnClickListener(this);

        //On récupère les informations passées par l'extra
        sharedPreferences =getSharedPreferences(User_Values,MODE_PRIVATE);
        bundle = getIntent().getExtras();

        lesMessages=(LinearLayout)findViewById(R.id.lesMessages);
        scrollMessages=(ScrollView)findViewById(R.id.scrollMessages);

        nomconversation=(TextView)findViewById(R.id.nomconversation);
        nomconversation.setText(bundle.getString("Nom"));

        ecriretexte=(EditText)findViewById(R.id.ecriretexte);

        //On récupère le lien vers la collection de la conversation
        currentDiscussion=FirebaseFirestore.getInstance().collection("datas").document("discussions").collection(bundle.getString("Nom"));
    }

    @Override
    protected void onStart() {
        super.onStart();
        //On crée un listener pour mettre à jour en temps réel la conversation
        currentDiscussion.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                //S'il n'y a pas d'erreurs
                if(e!=null){
                    Toast.makeText(maDiscussion.this,"Erreur loading messages",Toast.LENGTH_SHORT).show();
                    Log.e("error",e.toString());
                    return;
                }
                //On récupère les nouveaux messages de la collection
                ArrayList<Message> maListe=new ArrayList<>();
                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            Message nouveau=dc.getDocument().toObject(Message.class);
                            maListe.add(nouveau);
                            scrollMessages.fullScroll(ScrollView.FOCUS_DOWN);
                            break;
                    }
                }
                //Et on les affiche
                afficherMessage(maListe);
            }
        });
    }

    //On affiche la discussion
    private void afficherDiscussion(){
        currentDiscussion.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    ArrayList<Message> maListe=new ArrayList<>();
                    for(DocumentSnapshot documentSnapshot:task.getResult()){
                        Message mienne=documentSnapshot.toObject(Message.class);
                        maListe.add(mienne);
                    }
                    afficherMessage(maListe);
                }else {
                    Log.e("activiteExistantes","erreur");
                }
            }
        });
    }

    //A partir d'une liste on affiche les nouveaux messages
    private void afficherMessage(ArrayList<Message> liste){
        Typeface face = ResourcesCompat.getFont(maDiscussion.this, R.font.simonetta_black);

        for(short i=0;i<liste.size();i++){
            final TextView a=new Button(maDiscussion.this);
            //Si c'est le message de l'utilisateur
            if(sharedPreferences.getString("User_Pseudo","").equals(liste.get(i).getAuteur()))
            {
                LinearLayout.LayoutParams paramButton = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,1000f);
                paramButton.setMargins(100,20,0,0);
                a.setLayoutParams(paramButton);
                a.setBackgroundResource(R.drawable.custom_btn_moi);
                a.setGravity(Gravity.CENTER | Gravity.RIGHT);
                a.setText(liste.get(i).getContenu()+"\n"+liste.get(i).getDateMessage());
            }
            //Message d'une autre personne
            else{
                LinearLayout.LayoutParams paramButton = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,1000f);
                paramButton.setMargins(0,20,100,0);
                a.setLayoutParams(paramButton);
                a.setBackgroundResource(R.drawable.custom_btn_autres);
                a.setGravity(Gravity.CENTER | Gravity.LEFT);
                a.setText(liste.get(i).getAuteur()+":\n"+liste.get(i).getContenu()+"\n"+liste.get(i).getDateMessage());
            }
            //On affecte au bouton des propriétés graphiques
            a.setTypeface(face);
            a.setTextColor(getResources().getColor(R.color.textesmessages));
            a.setTextSize(15);
            a.setHint(String.valueOf(i));
            //On ajoute le message
            lesMessages.addView(a);
        }
    }


    @Override
    public void onClick(View v) {
        Intent principal = new Intent(maDiscussion.this, Principal.class);
        Intent event = new Intent(maDiscussion.this, event.class);
        Intent mbti = new Intent(maDiscussion.this, Mbti.class);
        Intent activite = new Intent(maDiscussion.this, activite.class);
        Intent discussion = new Intent(maDiscussion.this, discussion.class);
        Intent profil = new Intent(maDiscussion.this, profil.class);
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
            case R.id.btn_envoyer:
                //On envoie le contenu du Edittext si il n'est pas vide
                if(!ecriretexte.getText().toString().equals("")){
                    Message premier=new Message();premier.setAuteur(sharedPreferences.getString("User_Pseudo",""));premier.setContenu(ecriretexte.getText().toString());premier.setDateMessage(new Date());
                    Random rand = new Random();
                    int n = rand.nextInt(9999999);
                    //On crée un nom pour le document du message avec un rand
                    //Si 2 utilisateurs envoient un message au même insatnt
                    String a=String.valueOf(new Date())+String.valueOf(rand.nextInt(9999999));
                    FirebaseFirestore.getInstance().collection("datas").document("discussions").collection(bundle.getString("Nom")).document(a).set(premier);
                    ecriretexte.setText("");
                }
                break;
            case R.id.supprimerconversation:
                //Si l'utilisateur veut supprimer la conversation
                //On ouvre une boite de dialogue pour qu'il confirme
                AlertDialog.Builder builder = new AlertDialog.Builder(maDiscussion.this);
                builder.setTitle(getString(R.string.certain));
                builder.setPositiveButton(getString(R.string.oui), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseFirestore.getInstance().collection("datas").document("mesdiscussions").collection(sharedPreferences.getString("User_Pseudo","")).document(bundle.getString("Nom")).delete();
                        Intent principal = new Intent(maDiscussion.this, discussion.class);
                        startActivity(principal);
                        finish();
                    }
                });

                // Set the alert dialog no button click listener
                builder.setNegativeButton(getString(R.string.non), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.ajouterpersonne:
                //Pour ajouter une personne à la conversation
                final EditText taskEditText = new EditText(maDiscussion.this);
                AlertDialog dialog2 = new AlertDialog.Builder(maDiscussion.this)
                        .setTitle(getString(R.string.ajouterPersonne))
                        .setView(taskEditText)
                        .setPositiveButton(getString(R.string.ajouter), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(!taskEditText.getText().toString().equals("")){
                                    FirebaseUser.userExists(taskEditText.getText().toString(), new FirebaseUser.FirestoreCallback() {
                                        @Override
                                        public void onCallback(Boolean etre) {
                                            if(etre){
                                                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                                                String date = df.format(new Date());
                                                Conversation nouvelle = new Conversation(bundle.getString("Nom"),date);
                                                FirebaseFirestore.getInstance().collection("datas").document("mesdiscussions").collection(taskEditText.getText().toString()).document(bundle.getString("Nom")).set(nouvelle);
                                                Toast.makeText(maDiscussion.this,getString(R.string.utilisateurAjoute),Toast.LENGTH_LONG).show();
                                            }else{
                                                Toast.makeText(maDiscussion.this,getString(R.string.utilisateurNon),Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                                }
                            }
                        })
                        .setNegativeButton(getString(R.string.annuler), null)
                        .create();
                dialog2.show();
                break;
        }
    }
}