package com.skylarkingstudios.whatshisface;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.skylarkingstudios.whatshisface.model.Actor;
import com.skylarkingstudios.whatshisface.model.ActorList;
import com.skylarkingstudios.whatshisface.model.remote.TheMovieDBService;
import com.skylarkingstudios.whatshisface.model.remote.TheMovieDBServiceGenerator;
import com.skylarkingstudios.whatshisface.model.MovieList;
import com.skylarkingstudios.whatshisface.model.MovieResults;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindActorActivityOld extends AppCompatActivity {

    private EditText mUserInput;
    private Button mAddButton;
    private Button mSearchButton;
    private FragmentManager fm;
    private MovieList mMovies;
    private Fragment fragment;
    private DataManager mDataManager;
    private RelativeLayout loadPanel;
    private ActorList mActorList;
    private List<Actor> mActors;
    private List<Integer> mActorIds;
    private Actor mActor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actor_find_old);
        loadPanel = (RelativeLayout) findViewById(R.id.loading_panel);

        mMovies = MovieList.get(this);

        if (mMovies != null) {
            mMovies.clearMovies();
        }

        mDataManager = new DataManager(this);

        mUserInput = (EditText) findViewById(R.id.find_actor_input);

        mAddButton = (Button) findViewById(R.id.find_actor_add_button);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPressed();
            }
        });

        mSearchButton = (Button) findViewById(R.id.find_actor_search_button);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMovies.getMovies().size() > 0) {
                    submitPressed();
                }
            }
        });

        fm = getSupportFragmentManager();
        fragment = fm.findFragmentById(R.id.find_actor_list_container);
        if (fragment == null) {
            fragment = new FindActorListFragment();
            fm.beginTransaction()
                    .add(R.id.find_actor_list_container, fragment)
                    .commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPanel.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void addPressed() {
        if (!mUserInput.getText().toString().equals("")) {

            // Hide this away and only expose class specifics
            TheMovieDBService client = TheMovieDBServiceGenerator.createService(TheMovieDBService.class);
            Call<MovieResults> call = client.getMovieSearchResults(mUserInput.getText().toString(), TheMovieDBService.API_KEY);
            call.enqueue(new Callback<MovieResults>() {

                @Override
                public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                    if (response.isSuccess() && response.body().getTotalResults() > 0) {

                        // TODO Change this later, it's only adding the first option instead of giving options for possible doubles (Like The Thing (2011) vs. The Thing (1982))
                        mMovies.addMovie(response.body().getResults().get(0));

                        // Call the Credits API call and add the results for the added movie to the MovieLists entry
                        mDataManager.addCast(response.body().getResults().get(0).getId());

                        FindActorListFragment fragment = (FindActorListFragment) getSupportFragmentManager().findFragmentById(R.id.find_actor_list_container);
                        fragment.updateUI();
                        mUserInput.setText("");
                    } else {
                        Toast.makeText(getBaseContext(), R.string.toast_movie_not_found, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MovieResults> call, Throwable t) {
                    Log.e("Comm Error", t.getMessage());
                }
            });

        } else {
            Toast.makeText(this, R.string.toast_no_title_entered, Toast.LENGTH_SHORT).show();
        }

        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

    }

    // Once submit is pressed show a loading dialog
    // Submit must call the methods for getting commonActors and the corresponding network calls
    // Set up a listener to check to see when all the calls are finished so I can start the next activity
    public void submitPressed() {

        loadPanel.setVisibility(View.VISIBLE);

        mActorList = ActorList.get(this);
        mActorList.clear();

        Intent i = new Intent(this, ActorResultActivity.class);
        startActivity(i);

    }

}
