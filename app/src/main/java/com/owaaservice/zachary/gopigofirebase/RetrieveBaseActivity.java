package com.owaaservice.zachary.gopigofirebase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;


/**
 * Created by Zachary on 28/12/2016.
 */

public class RetrieveBaseActivity extends AppCompatActivity {

    private ListView mListView; //On déclare la liste qui sera lié à la listView "listView" du fichier xml

    private ArrayList<String> mDistances = new ArrayList<>(); //Pour stocker les données de "distance" dans la liste view on utilise l'ArrayList


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_base); //On associe la vue du fichier xml activity_retrieve_base à cette activité

        //mRef = new Firebase("https://m2gopigo.firebaseio.com/distances/"); //Liaison de la variable mRef au path de la base de donnée à récupérer
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://m2gopigo.firebaseio.com/distances");
        mListView = (ListView) findViewById(R.id.listView); //Liaison de mListView à l'id associé de la "listView" déclarée dans activity_retrieve_base.xml

        FirebaseListAdapter<String> firebaseListAdapter = new FirebaseListAdapter<String>( // 4paramètre
                this, //context
                String.class, //type
                android.R.layout.simple_list_item_1, //Layout
                databaseReference //d'où récupérer les datas
        ) {
            @Override
            protected void populateView(View v, String model, int position) {
                TextView textView = (TextView) v.findViewById(android.R.id.text1);
                textView.setText(model);

            }
        };

        mListView.setAdapter(firebaseListAdapter);


    }

}
