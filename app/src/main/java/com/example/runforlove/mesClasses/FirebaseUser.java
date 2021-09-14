package com.example.runforlove.mesClasses;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseUser {

    private static final String COLLECTION_NAME = "users";
    private static Boolean exist=true;
    private static String pseudo="";

    public FirebaseUser(){};

    //Get
    public static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    //Create
    public static void createUser(User user) {
        FirebaseUser.getUsersCollection().document(user.getPseudo()).set(user);
        return;
    }


    public interface FirestoreCallback{
        void onCallback(Boolean etre);
    }
    //Si l'utilisateur existe
    public static Boolean userExists(String name_user,final FirestoreCallback firestoreCallback)
    {
        DocumentReference docRef = FirebaseUser.getUsersCollection().document(name_user);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(!documentSnapshot.exists())
                {
                   exist=false;

                }
                else
                {
                    exist =true;
                }
                firestoreCallback.onCallback(exist);
            }
        });
        return exist;
    }
}
