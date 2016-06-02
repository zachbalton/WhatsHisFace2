package com.skylarkingstudios.whatshisface;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.skylarkingstudios.whatshisface.model.APIConfiguration;

public class MainMenuActivity extends AppCompatActivity {

    private ImageButton mFindActor;
    private ImageButton mFindMovie;
    private APIConfiguration apiConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        mFindActor = (ImageButton) findViewById(R.id.main_menu_movie_button);
        mFindActor.setColorFilter(Color.argb(255, 116, 100, 161));
        mFindActor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FindActorActivity.class);
                startActivity(intent);
            }
        });

        mFindMovie = (ImageButton) findViewById(R.id.main_menu_actor_button);
        mFindMovie.setColorFilter(Color.argb(255, 164, 148, 59));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

}
