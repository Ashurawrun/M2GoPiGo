package com.owaaservice.zachary.gopigofirebase;

import android.app.Application;
import android.util.Log;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.owaaservice.zachary.gopigofirebase.dao.Dao;

/**
 * Created by Zachary on 27/12/2016.
 */

public class GoPiGoFirebase extends Application {



    @Override
    public void onCreate() {
        super.onCreate();
        //Version précédente de Firebase, utilisée pour ajouter des valeurs (dans MainActivity)
        Firebase.setAndroidContext(this);

        //Nouvelle version, utilisée pour récupérer les valeurs (dans RetrieveBaseActivity), l'ancienne version était utilisable mais causait des soucis lors des changements de valeurs.
        if(!FirebaseApp.getApps(this).isEmpty()){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }

        //Gestion stockage interne
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://m2gopigo.firebaseio.com/distances");
        final Dao localStorage = new Dao(this);
        databaseReference.addChildEventListener(new com.google.firebase.database.ChildEventListener() {
            @Override
            public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                String value = dataSnapshot.getValue(String.class); //On récupère les données de la base de données
                localStorage.insertOrUpdate(value); //Mise à jour stockage interne
            }

            @Override
            public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                String value = dataSnapshot.getValue(String.class); //On récupère les données de la base
                localStorage.insertOrUpdate(value); //Mise à jour stockage interne
            }

            @Override
            public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class); //On récupère les données de la base de données
                localStorage.insertOrUpdate(value); //Mise à jour stockage interne
            }

            @Override
            public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
