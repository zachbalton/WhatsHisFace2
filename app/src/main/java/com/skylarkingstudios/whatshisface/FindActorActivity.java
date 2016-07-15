package com.skylarkingstudios.whatshisface;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.skylarkingstudios.whatshisface.model.Actor;
import com.skylarkingstudios.whatshisface.model.ActorList;
import com.skylarkingstudios.whatshisface.model.remote.TheMovieDBService;
import com.skylarkingstudios.whatshisface.model.remote.TheMovieDBServiceGenerator;
import com.skylarkingstudios.whatshisface.model.MovieList;
import com.skylarkingstudios.whatshisface.model.MovieResults;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindActorActivity extends AppCompatActivity {

    private ImageButton mAddMovieOneButton, mAddMovieTwoButton, mAddMovieThreeButton,
            mAddMovieFourButton;
    private TextView mMovieOneTextView, mMovieTwoTextView, mMovieThreeTextView, mMovieFourTextView;
    private FloatingActionButton mSearchButton;
    private LinearLayout mBackgroundLayout, mSecondMovieLayout, mSecondMovieRow, mThirdMovieLayout,
            mFourthMovieLayout;
    private EditText mUserInput;
    private FragmentManager fm;
    private MovieList mMovies;
    private Fragment fragment;
    private DataManager mDataManager;
    private RelativeLayout loadPanel;
    private ActorList mActorList;
    private List<Actor> mActors;
    private List<Integer> mActorIds;
    private Actor mActor;
    private String baseURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actor_find);

        baseURL = "http://image.tmdb.org/t/p/h632";

        mMovies = MovieList.get(this);

        if (mMovies != null) {
            mMovies.clearMovies();
        }

        mDataManager = new DataManager(this);

        mBackgroundLayout = (LinearLayout) findViewById(R.id.background_linear_layout);
        mSecondMovieLayout = (LinearLayout) findViewById(R.id.second_movie_layout);
        mThirdMovieLayout = (LinearLayout) findViewById(R.id.third_movie_layout);
        mFourthMovieLayout = (LinearLayout) findViewById(R.id.fourth_movie_layout);
        mSecondMovieRow = (LinearLayout) findViewById(R.id.second_movie_row);

        mUserInput = (EditText) findViewById(R.id.input_edit_text);

        mMovieOneTextView = (TextView) findViewById(R.id.first_movie_text_view);
        mMovieTwoTextView = (TextView) findViewById(R.id.second_movie_text_view);
        mMovieThreeTextView = (TextView) findViewById(R.id.third_movie_text_view);
        mMovieFourTextView = (TextView) findViewById(R.id.fourth_movie_text_view);

        mAddMovieOneButton = (ImageButton) findViewById(R.id.add_movie_button_1);
        mAddMovieOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPressed();
            }
        });

        mAddMovieTwoButton = (ImageButton) findViewById(R.id.add_movie_button_2);
        mAddMovieTwoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPressed();
            }
        });

        mAddMovieThreeButton = (ImageButton) findViewById(R.id.add_movie_button_3);
        mAddMovieThreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPressed();
            }
        });

        mAddMovieFourButton = (ImageButton) findViewById(R.id.add_movie_button_4);
        mAddMovieFourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPressed();
            }
        });

        mSearchButton = (FloatingActionButton) findViewById(R.id.fab);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMovies.getMovies().size() > 0) {
                    submitPressed();
                }
            }
        });

        // User hits submit on keyboard
        mUserInput.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    final InputMethodManager inputMethodManager = (InputMethodManager) getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(mUserInput.getWindowToken(), 0);
                    mUserInput.setVisibility(View.GONE);
                    searchMovie();
                    return true;
                }
                return false;
            }
        });



        // User presses search button
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPressed();
            }
        });

        resetView();

    }

    public void resetView() {
        // Called whenever the activity is recreated
        mUserInput.setVisibility(View.GONE);
        mSecondMovieLayout.setVisibility(View.GONE);
        mThirdMovieLayout.setVisibility(View.INVISIBLE);
        mFourthMovieLayout.setVisibility(View.GONE);
        mSearchButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
    // TODO fix back press not hiding the EditText
    }

    public void addPressed() {
        // Handle Keyboard Popup and Text Entry, then when submit is called, call searchMovie

        mUserInput.setVisibility(View.VISIBLE);
        mUserInput.setFocusableInTouchMode(true);
        mUserInput.requestFocus();

        final InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(mUserInput, InputMethodManager.SHOW_IMPLICIT);

    }

    public void searchMovie() {
        if (!mUserInput.getText().toString().equals("")) {

            // Hide this away and only expose class specifics
            TheMovieDBService client = TheMovieDBServiceGenerator.createService(TheMovieDBService.class);
            Call<MovieResults> call = client.getMovieSearchResults(mUserInput.getText().toString(), TheMovieDBService.API_KEY);
            call.enqueue(new Callback<MovieResults>() {

                @Override
                public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                    if (response.isSuccess() && response.body().getTotalResults() > 0) {
                        mUserInput.setText("");

                        // ---------------------------------------------------------------------------------------------------------------------------------------------------
                        // TODO Change this later, it's only adding the first option instead of giving options for possible doubles (Like The Thing (2011) vs. The Thing (1982))
                        // It should trigger a fragment which allows a user to select, and then THAT selection is added to the cast and movie list
                        mMovies.addMovie(response.body().getResults().get(0));

                        int movieTotal = mMovies.getMovies().size();
                        Log.d("Movie Poster URL", "Trying poster from URL: " + baseURL + response.body().getResults().get(0).getPosterURL());

                        // Create a movie list for these and do a For Each maybe instead of this silly switch
                        switch (movieTotal) {
                            case 1:
                                Picasso.with(getBaseContext()).load(baseURL + response.body().getResults().get(0).getPosterURL()).placeholder(R.drawable.posterplaceholder).into(mAddMovieOneButton);
                                mMovieOneTextView.setText(response.body().getResults().get(0).getTitle());
                                mSecondMovieLayout.setVisibility(View.VISIBLE);
                                break;
                            case 2:
                                Picasso.with(getBaseContext()).load(baseURL + response.body().getResults().get(0).getPosterURL()).placeholder(R.drawable.posterplaceholder).into(mAddMovieTwoButton);
                                mMovieTwoTextView.setText(response.body().getResults().get(0).getTitle());
                                mThirdMovieLayout.setVisibility(View.VISIBLE);
                                break;
                            case 3:
                                Picasso.with(getBaseContext()).load(baseURL + response.body().getResults().get(0).getPosterURL()).placeholder(R.drawable.posterplaceholder).into(mAddMovieThreeButton);
                                mMovieThreeTextView.setText(response.body().getResults().get(0).getTitle());
                                mFourthMovieLayout.setVisibility(View.VISIBLE);
                                break;
                            case 4:
                                Picasso.with(getBaseContext()).load(baseURL + response.body().getResults().get(0).getPosterURL()).placeholder(R.drawable.posterplaceholder).into(mAddMovieFourButton);
                                mMovieFourTextView.setText(response.body().getResults().get(0).getTitle());
                                break;
                        }

                        if (movieTotal > 0) {
                            mSearchButton.setVisibility(View.VISIBLE);
                        }





                        // Call the Credits API call and add the results for the added movie to the MovieLists entry
                        mDataManager.addCast(response.body().getResults().get(0).getId());
                        // ---------------------------------------------------------------------------------------------------------------------------------------------------

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

    }

    // Once submit is pressed show a loading dialog
    // Submit must call the methods for getting commonActors and the corresponding network calls
    // Set up a listener to check to see when all the calls are finished so I can start the next activity
    public void submitPressed() {

        mActorList = ActorList.get(this);
        mActorList.clear();

        Intent i = new Intent(this, ActorResultActivity.class);
        startActivity(i);

    }



}
