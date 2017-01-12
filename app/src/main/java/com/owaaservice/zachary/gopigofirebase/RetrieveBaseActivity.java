package com.owaaservice.zachary.gopigofirebase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.owaaservice.zachary.gopigofirebase.dao.Dao;
import com.owaaservice.zachary.gopigofirebase.dao.DatabaseContract;
import com.owaaservice.zachary.gopigofirebase.dao.DatabaseHelper;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Zachary on 28/12/2016.
 */

public class RetrieveBaseActivity extends AppCompatActivity {
    private ListView mListView; //On déclare la liste qui sera lié à la listView "listView" du fichier xml
    private StableArrayAdapter mSimpleAdapter;

    private List<String> mDistances = new ArrayList<>(); //Pour stocker les données de "distance" dans la liste view on utilise l'ArrayList
    private Dao mLocaleStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_base); //On associe la vue du fichier xml activity_retrieve_base à cette activité

        //mRef = new Firebase("https://m2gopigo.firebaseio.com/distances/"); //Liaison de la variable mRef au path de la base de donnée à récupérer
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://m2gopigo.firebaseio.com/distances");

        mLocaleStorage = new Dao(getApplicationContext());
        mDistances.addAll(mLocaleStorage.getAll());
        mSimpleAdapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, mDistances);

        mListView = (ListView) findViewById(R.id.listView); //Liaison de mListView à l'id associé de la "listView" déclarée dans activity_retrieve_base.xml
        mListView.setAdapter(mSimpleAdapter);

        final FirebaseListAdapter<String> firebaseListAdapter = new FirebaseListAdapter<String>( // 4 paramètres
                this, //context
                String.class, //type
                android.R.layout.simple_list_item_1, //Layout
                databaseReference //d'où récupérer les datas
        ) {
            @Override
            protected void populateView(View v, String model, int position) {
                TextView textView = (TextView) v.findViewById(android.R.id.text1);
                textView.setText(model);
                mLocaleStorage.update(model);
                mSimpleAdapter.updateData(mLocaleStorage.getAll());
            }
        };
        //mListView.setAdapter(firebaseListAdapter);


        databaseReference.addChildEventListener(new com.google.firebase.database.ChildEventListener() {
            @Override
            public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                String value = dataSnapshot.getValue(String.class); //On récupère les données de la base de données
                mLocaleStorage.insertOrUpdate(value);
                mSimpleAdapter.updateData(mLocaleStorage.getAll());
                mListView.invalidate();
                Log.d("Debug", "onChildAdded :: " + value);
            }

            @Override
            public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                String value = dataSnapshot.getValue(String.class);
                mLocaleStorage.update(value);
                mSimpleAdapter.updateData(mLocaleStorage.getAll());
            }

            @Override
            public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class); //On récupère les données de la base de données
                mLocaleStorage.delete(value);
                mSimpleAdapter.updateData(mLocaleStorage.getAll());
            }

            @Override
            public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {
        List<String> mIdMap = new ArrayList<>();
        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (String value : objects) {
                mIdMap.add(value);
            }
        }

        @Nullable
        @Override
        public String getItem(int position) {
            try {
                return mIdMap.get(position);
            } catch (Exception e) {
                return "";
            }
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        public void updateData(List<String> objects) {
            mDistances.clear();
            mDistances.addAll(objects);
            notifyDataSetChanged();
        }
    }

}
