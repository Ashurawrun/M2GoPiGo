package com.owaaservice.zachary.gopigofirebase;

import android.app.Application;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

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
    }
}
