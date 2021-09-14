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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class FirebaseEvent {
    private static final String DOCUMENT_DONNEES = "datas";
    public static  Boolean exist=false;

    public static CollectionReference getEventsCollection(){
        return FirebaseFirestore.getInstance().collection(DOCUMENT_DONNEES);
    }

        public interface FirestoreEventExistCallback{
            void onCallback(Boolean etre);
        }
    //Si l'événement existe
    public static void eventExists(String name_Event, final FirebaseEvent.FirestoreEventExistCallback firestoreEventExistCallback)
    {
        DocumentReference docRef = FirebaseActivite.getActvitesCollection().document("events").collection("toutes").document(name_Event);
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
                            firestoreEventExistCallback.onCallback(exist);
                        }else{
                            Log.e("eventExists","error");
                        }
                    }
                });
        return ;
    }


    public interface FirestoreCreateEventCallback{
        void onCallback();
    }
    //Crée un événement
    public static void createEvent(final Event event, final FirebaseEvent.FirestoreCreateEventCallback firestoreCreateEventCallback) {

        FirebaseActivite.getActvitesCollection().document("events").collection("toutes").document(event.getNomEvent()).set(event);
        firestoreCreateEventCallback.onCallback();
    }
    //Si des événements se déroule dans une ville
    public static  void eventVilleExist(String ville,final FirebaseEvent.FirestoreEventExistCallback firestoreEventExistCallback){
        CollectionReference docRef = FirebaseActivite.getActvitesCollection().document("events").collection("toutes");
        docRef.whereEqualTo("villeEvent",ville).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            Boolean ya=false;
                            for(DocumentSnapshot documentSnapshot:task.getResult()){
                                ya=true;
                            }
                            firestoreEventExistCallback.onCallback(ya);
                        }else {
                            Log.e("activiteExistantes","erreur");
                        }
                    }
                });
        return ;
    }

    public interface FirestoreEventsCreeCallback{
        void onCallback(ArrayList<Event> liste);
    }
    //Récupère les événements d'une ville
    public static  void eventVille(String ville,final FirebaseEvent.FirestoreEventsCreeCallback firestoreEventsCreeCallback){
        CollectionReference docRef = FirebaseActivite.getActvitesCollection().document("events").collection("toutes");
        docRef.whereEqualTo("villeEvent",ville).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            ArrayList<Event> maListe=new ArrayList<>();
                            for(DocumentSnapshot documentSnapshot:task.getResult()){
                                Event ancienne=documentSnapshot.toObject(Event.class);

                                try {
                                    String dateFromat="yyyy/MM/dd";
                                    SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
                                    sdf.setLenient(false);
                                    //if not valid, it will throw ParseException
                                    Date date = sdf.parse(ancienne.getDateEvent());

                                    String currentDate = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
                                    Date now=sdf.parse(currentDate);
                                    if(now.before(date)){
                                        maListe.add(ancienne);
                                    }
                                }catch (Exception e){}

                            }
                            firestoreEventsCreeCallback.onCallback(maListe);
                        }else {
                            Log.e("activiteExistantes","erreur");
                        }
                    }
                });
        return ;
    }
    //Récupère les prochains événeents
    public static  void eventProchains(final FirebaseEvent.FirestoreEventsCreeCallback firestoreEventsCreeCallback){
        CollectionReference docRef = FirebaseActivite.getActvitesCollection().document("events").collection("toutes");
        docRef.orderBy("dateEvent", Query.Direction.ASCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            ArrayList<Event> maListe=new ArrayList<>();
                            for(DocumentSnapshot documentSnapshot:task.getResult()){
                                Event ancienne=documentSnapshot.toObject(Event.class);

                                try {
                                    String dateFromat="yyyy/MM/dd";
                                    SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
                                    sdf.setLenient(false);
                                    //if not valid, it will throw ParseException
                                    Date date = sdf.parse(ancienne.getDateEvent());

                                    String currentDate = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
                                    Date now=sdf.parse(currentDate);
                                    if(now.before(date)){
                                        maListe.add(ancienne);
                                    }

                                }catch (Exception e){}

                            }
                            firestoreEventsCreeCallback.onCallback(maListe);
                        }else {
                            Log.e("activiteExistantes","erreur");
                        }
                    }
                });
        return ;
    }
    //Si il existe des événements
    public static void eventAvoirExist(final FirebaseEvent.FirestoreEventExistCallback firestoreEventExistCallback){
        CollectionReference docRef = FirebaseActivite.getActvitesCollection().document("events").collection("toutes");
        docRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            Boolean ya=false;
                            for(DocumentSnapshot documentSnapshot:task.getResult()){
                                ya=true;
                            }
                            firestoreEventExistCallback.onCallback(ya);
                        }else {
                            Log.e("activiteExistantes","erreur");
                        }
                    }
                });
        return ;
    }

    //Si l'utilisateur a créé des événements
    public static void eventMiensExist(String pseudo,final FirebaseEvent.FirestoreEventExistCallback firestoreEventExistCallback){
        CollectionReference docRef = FirebaseActivite.getActvitesCollection().document("events").collection("toutes");
        docRef.whereEqualTo("auteurEvent",pseudo).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            Boolean ya=false;
                            for(DocumentSnapshot documentSnapshot:task.getResult()){
                                ya=true;
                            }
                            firestoreEventExistCallback.onCallback(ya);
                        }else {
                            Log.e("activiteExistantes","erreur");
                        }
                    }
                });
        return ;
    }

    //Récupère les événements d'un utilisateur
    public static  void eventMiens(String pesudo,final FirebaseEvent.FirestoreEventsCreeCallback firestoreEventsCreeCallback){
        CollectionReference docRef = FirebaseActivite.getActvitesCollection().document("events").collection("toutes");
        docRef.whereEqualTo("auteurEvent",pesudo).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            ArrayList<Event> maListe=new ArrayList<>();
                            for(DocumentSnapshot documentSnapshot:task.getResult()){
                                Event ancienne=documentSnapshot.toObject(Event.class);
                                maListe.add(ancienne);
                            }
                            firestoreEventsCreeCallback.onCallback(maListe);
                        }else {
                            Log.e("activiteExistantes","erreur");
                        }
                    }
                });
        return ;
    }
}
