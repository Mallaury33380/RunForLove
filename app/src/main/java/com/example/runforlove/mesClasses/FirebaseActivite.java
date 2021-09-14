package com.example.runforlove.mesClasses;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class FirebaseActivite {
    private static final String DOCUMENT_ACTIVITES = "datas";
    public static  Boolean exist=false;


    public static CollectionReference getActvitesCollection(){
        return FirebaseFirestore.getInstance().collection(DOCUMENT_ACTIVITES);
    }


    public interface FirestoreCreateCallback{
        void onCallback();
    }

    //Créer une activité
    public static void createActivite(final Activite activite, final String pseudo, final FirestoreCreateCallback firestoreCreateCallback) {

        activiteExists(activite.getNom(), pseudo, new FirestoreCallback() {
            @Override
            public void onCallback(Boolean etre) {
                if(!exist)
                {
                    FirebaseActivite.getActvitesCollection().document("activites").collection(pseudo).document(activite.getNom()).set(activite);
                    firestoreCreateCallback.onCallback();
                }
                return;
            }
        });

    }

    public interface FirestoreCallback{
        void onCallback(Boolean etre);
    }
    //Si une activité existe
    public static void activiteExists(String name_activite, String pseudo, final FirestoreCallback firestoreCallback)
    {
        DocumentReference docRef = FirebaseActivite.getActvitesCollection().document("activites").collection(pseudo).document(name_activite);
        docRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document=task.getResult();
                            if(document.exists()){
                                exist=true;
                            }else{
                                exist=false;
                            }
                            firestoreCallback.onCallback(exist);
                        }else{
                            Log.e("activiteExists","error");
                        }
                    }
                });
        return ;
    }



    public interface FirestoreExistantesCallback{
        void onCallback(ArrayList<Activite> liste);
    }
    //Récupération des activités d'un utilisateur
    public static void activiteExistantes(String pseudo, final FirestoreExistantesCallback firestoreExistantesCallback)
    {
        CollectionReference docRef = FirebaseActivite.getActvitesCollection().document("activites").collection(pseudo);
        docRef.orderBy("dateActivite", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            ArrayList<Activite> maListe=new ArrayList<>();
                            for(DocumentSnapshot documentSnapshot:task.getResult()){
                                Activite ancienne=documentSnapshot.toObject(Activite.class);
                                maListe.add(ancienne);
                            }
                            firestoreExistantesCallback.onCallback(maListe);
                        }else {
                            Log.e("activiteExistantes","erreur");
                        }
                    }
                });
        return ;
    }

    //Si l'utilisateur n'a pas d'activité
    public static void activiteVide(String pseudo, final FirestoreCallback firestoreCallback)
    {
        CollectionReference docRef = FirebaseActivite.getActvitesCollection().document("activites").collection(pseudo);
        docRef.orderBy("dateActivite", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            Boolean a=false;
                            if(task.getResult().isEmpty()){
                                a=true;
                            }
                            firestoreCallback.onCallback(a);
                        }else {
                            Log.e("activiteExistantes","erreur");
                        }
                    }
                });
        return ;
    }


}
