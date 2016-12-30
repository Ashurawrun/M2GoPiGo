package com.owaaservice.zachary.gopigofirebase;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import static android.R.attr.x;

/**
 * Created by Zachary on 28/12/2016.
 */

public class ShowGraphActivity extends AppCompatActivity {

    private ListView mListView; //On déclare la liste qui sera lié à la listView "listView" du fichier xml
    private ArrayList<String> mDistances = new ArrayList<>(); //Pour stocker les données de "distance" dans la liste view on utilise l'ArrayList
    private Firebase mRef; //On déclare la variable qui va stocker le lien vers la base de donnée
    private Button myButton;
    LineChart myChart;
    ArrayList<Entry> myAXES = new ArrayList<>();
    String[] dataObjects = new String[0];
    List<Entry> entries = new ArrayList<Entry>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_graph); //On associe la vue du fichier xml activity_retrieve_base à cette activité

        //Gestion bouton test
        myButton = (Button) findViewById(R.id.log);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Liste valeurs de base: ",mDistances.toString());
                String myTestString =  "";
                if(mDistances.size()>0) {
                    myTestString = mDistances.get(0);
                    StringTokenizer tokens = new StringTokenizer(myTestString, "|");
                    String first = tokens.nextToken();// // Contiendra la Première partie du string après la "|"
                    first = first.replace(" ", "");
                    String second = tokens.nextToken();// Contiendra la seconde partie du string après la "|"
                    second = second.replace(" ", "");
                    String third = tokens.nextToken();// Contiendra la troisième partie du string après la "|"
                    third = third.replace(" ", "");
                    Log.d("Liste log @0: ", mDistances.get(0));
                    Log.d("first: ", first);
                    Log.d("second: ", second);
                    Log.d("third", third);
                }

                //Un LineChart est initialisé depuis activity_show_graph.xml
                myChart = (LineChart) findViewById(R.id.chart);

                ////////////////////////////////////////////////////////////////////////////
                if(mDistances.size()>0) {
                    for (int i = 0; i < mDistances.size(); i++) {
                        myTestString = mDistances.get(i);
                        StringTokenizer token = new StringTokenizer(myTestString, "|"); //Choix du délimiteur pour couper la chaine de caractère
                        String premier = token.nextToken(); // Contiendra la première partie du string après la "|"
                        premier = premier.replace(" ", ""); //On vire les espaces
                        Float f1 = Float.parseFloat(premier); //On transforme le String qui correspondait à l'identifiant en Float
                        String deuxieme = token.nextToken();// Contiendra la seconde partie du string après la "|"
                        deuxieme = deuxieme.replace(" ", ""); //On vire les espaces
                        String troisieme = token.nextToken();// Contiendra la troisième partie du string après la "|"
                        troisieme = troisieme.replace(" ", ""); //On vire les espaces
                        Float f2 = Float.parseFloat(troisieme); //On transforme le String qui correspondait à la valeur en float


                        Log.d("Debug", "Value f1:" + Float.toString(f1));
                        Log.d("Debug", "Value f2:" + Float.toString(f2));

                        myAXES.add(new Entry(f1, f2));
                    }


                    ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();

                    LineDataSet lineDataSet1 = new LineDataSet(myAXES, "distance");
                    lineDataSet1.setDrawCircles(false);
                    lineDataSet1.setColor(Color.BLUE);

                    lineDataSets.add(lineDataSet1);
                    // enable description text
                    myChart.getDescription().setEnabled(true);

                    // enable touch gestures
                    myChart.setTouchEnabled(true);

                    // enable scaling and dragging
                    myChart.setDragEnabled(true);
                    myChart.setScaleEnabled(true);
                    myChart.setDrawGridBackground(false);

                    // if disabled, scaling can be done on x- and y-axis separately
                    myChart.setPinchZoom(true);
                    Description test = new Description();
                    test.setText("Graphique représentant la distance pour chaque id relevé");
                    myChart.setData(new LineData(lineDataSets));
                    myChart.setDescription(test);
                    myChart.invalidate(); // refresh
                    myChart.notifyDataSetChanged();
                    myButton.setVisibility(View.GONE);
                }
                ////////////////////////////////////////////////////////////////////////////

            }
        });//Fin gestion bouton test



        mRef = new Firebase("https://m2gopigo.firebaseio.com/distances"); //Liaison de la variable mRef au path de la base de donnée à récupérer
       // mListView = (ListView) findViewById(R.id.listView); //Liaison de mListView à l'id associé de la "listView" déclarée dans activity_retrieve_base.xml
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mDistances);//this = context, simple_List_item_1 = layout par défaut

        //mListView.setAdapter(arrayAdapter); //On set l'adapter


        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String value = dataSnapshot.getValue(String.class); //On récupère les données de la base de données

                mDistances.add(value); //Ajoute les variable à mon ArrayList

                arrayAdapter.notifyDataSetChanged();
                Log.d("Liste 2: ", mDistances.toString());

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });
    }
}



