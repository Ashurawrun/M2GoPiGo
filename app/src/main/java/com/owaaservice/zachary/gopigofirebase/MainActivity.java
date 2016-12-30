package com.owaaservice.zachary.gopigofirebase;

import android.content.DialogInterface;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.Locale;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    //Premier bouton + textfields
    private Button mAddBtn;
    private EditText mKeyValue;

    //Second bouton + textfields
    private EditText mIdentifiantValue;
    private EditText mJourValue;
    private EditText mDistanceValue;
    private ArrayList<String> mDistances = new ArrayList<>(); //Pour stocker les données de "distance" dans la liste view on utilise l'ArrayList


    private Firebase mRootRef; //Sera utilisé pour récupérer un path https://m2gopigo.firebaseio.com/distances


    Calendar calendar = Calendar.getInstance(); //Sera utilisé pour récupérer le jour de la semaine.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Liaison à ma Firebase directement dans les distances (permet d'ajouter un enfant distance directement)
        mRootRef = new Firebase ("https://m2gopigo.firebaseio.com/distances");

        //Liaison boutons et textfields
        mAddBtn = (Button) findViewById(R.id.addBtn);
        mKeyValue = (EditText) findViewById(R.id.keyValue);
        //Seconde partie des textfields et second bouton
        mIdentifiantValue = (EditText) findViewById(R.id.identifiantValue);
        mJourValue = (EditText) findViewById(R.id.jourValue);
        mDistanceValue = (EditText) findViewById(R.id.distanceValue);

        ///////////////////////////////////////////////////////////////////////////////////////////
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mDistances);//this = context, simple_List_item_1 = layout par défaut

        mRootRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String value = dataSnapshot.getValue(String.class); //On récupère les données de la base de données
                mDistances.add(value); //Ajoute les variable à mon ArrayList
                arrayAdapter.notifyDataSetChanged();
                Log.d("Liste de données: ", mDistances.toString());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(FirebaseError firebaseError) {}

        });

        ///////////////////////////////////////////////////////////////////////////////////////////


        //Début gestion premier bouton
        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Va récupérer l'input entré par l'utilisateur dans les textField et les convertir en string
                //Devra être renommé distance_(x+1) dans le futur et le (x+1) sera récupéré depuis la base de donnée
                String key = mKeyValue.getText().toString();
                String lastElement = "laissé vide" ;

                String identifiant = mIdentifiantValue.getText().toString();
                String jour = mJourValue.getText().toString();
                String valeur = mDistanceValue.getText().toString();

                if(!mDistances.isEmpty()) { // Ce if sert à calculer l'identifiant du dernier élément et à lui à ajouter 1 puis à le refaire passer en String
                    String lastIdentifiant = mDistances.get(mDistances.size()-1);
                    StringTokenizer tokens = new StringTokenizer(lastIdentifiant, "|");
                    String last = tokens.nextToken();// // Contiendra la Première partie du string après la "|"
                    last = last.replace(" ", "");
                    Log.v("Le last id est : ", last);
                    int i = Integer.parseInt(last);
                    i = i +1;
                    lastElement = Integer.toString(i);
                }

                if(key.isEmpty()){//Si la key de la donnée (correspond à son identifiant mais est cachée pour l'utilisateur) est vide, alors on lui donne l'id du dernier élément +1
                    key = lastElement;
                }

                Firebase childRef = mRootRef.child(key); //La key doit être un string sous la forme "distance_x" avec x un int qui s'incrémente.


                if(!identifiant.isEmpty() && !jour.isEmpty() && !valeur.isEmpty()) {//Si rien est vide alors on ajoute la valeur et on Log toutes les variables indiquées par l'utilisateur
                    childRef.setValue(identifiant + " | " + jour + " | " + valeur + " | ");
                    Log.v("E_VALUE", "identifiant : " + identifiant);
                    Log.v("E_VALUE", "jour : " + jour);
                    Log.v("E_VALUE", "valeur : " + valeur);
                }
                else { //Si des choses sont restées vides, on les remplis
                    Log.v("E_VALUE", "Des strings sont vides");
                    if(identifiant.isEmpty()){ //Si l'indentifiant a été laissé vide le remplir en lui donnant l'id du dernier élément +1
                        identifiant = lastElement;
                    }
                    if(jour.isEmpty()){ //Si jour a été laissé vide, le remplir en lui mettant le jour d'aujourd'hui au format "lundi/mardi/mercredi/jeudi/vendredi..."
                        Calendar calendar = Calendar.getInstance();
                        DateFormat df = new SimpleDateFormat("EEEE", Locale.FRANCE);//Permet d'afficher le format de la date sous forme de jour & en sans abbréviation
                        jour = df.format(Calendar.getInstance());
                    }
                    if(valeur.isEmpty()){//Si la valeur est vide on lui met 0 par défaut
                        valeur = "0";
                    }

                    childRef.setValue(identifiant + " | " + jour + " | " + valeur + " | "); //Ajout de la valeur dans la base de données
                }

            }
        });//Fin gestion premier bouton

    }
}
