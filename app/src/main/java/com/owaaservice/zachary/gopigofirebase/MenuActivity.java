package com.owaaservice.zachary.gopigofirebase;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Zachary on 28/12/2016.
 */

public class MenuActivity extends AppCompatActivity {
    Button play_button;
    Button level_button;
    Button about_button;
    MediaPlayer sound;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_menu);
        addListenerOnButton();
    }

    public void addListenerOnButton() {

        sound = MediaPlayer.create(this, R.raw.menu);
        final Context context = this;


        play_button = (Button) findViewById(R.id.add_base);
        play_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                sound.start();

            }

        });

        level_button = (Button) findViewById(R.id.retrieve_base);

        level_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, RetrieveBaseActivity.class);
                startActivity(intent);
                sound.start();
            }

        });
        about_button = (Button) findViewById(R.id.show_graph);

        about_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(context, ShowGraphActivity.class);
                startActivity(intent);
                sound.start();
            }

        });

    }


}

