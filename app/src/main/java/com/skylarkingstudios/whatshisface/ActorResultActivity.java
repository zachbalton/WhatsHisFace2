package com.skylarkingstudios.whatshisface;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;

import com.skylarkingstudios.whatshisface.model.Actor;
import com.skylarkingstudios.whatshisface.model.ActorList;
import com.skylarkingstudios.whatshisface.model.MovieList;
import com.skylarkingstudios.whatshisface.model.RetrofitEvent;
import com.skylarkingstudios.whatshisface.model.remote.TheMovieDBService;
import com.skylarkingstudios.whatshisface.model.remote.TheMovieDBServiceGenerator;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActorResultActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private Button mNewSearch;
    private ActorList mFinalActorList;
    private List<Integer> mActorIds;
    private List<Actor> mActorList;
    private Actor mActor;
    private DataManager mDataManager;
    private MovieList mMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.screen_loading);

        mFinalActorList = ActorList.get(this);
        mFinalActorList.clear();

        mMovies = MovieList.get(this);

        mDataManager = new DataManager(this);
        mActorIds = mDataManager.getCommonActorIds(this);

        // if getCommonActorIds returns null then do not get actor bios, instead add a single actor to fill "No Results Found" page out
        if (mActorIds == null) {
            setNoResults();
        } else {
            mActorList = getActorBios(mActorIds);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void setNoResults() {
        setContentView(R.layout.no_results);
        mNewSearch = (Button) findViewById(R.id.new_search_button);

        mNewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), MainMenuActivity.class);
                startActivity(i);
            }
        });
    }

    // getActorBios(List<Integer>) returns a full list of bios (as a List<Actor>) given the set of actor ids
    public List<Actor> getActorBios(List<Integer> actorIds) {
        mActorList = new ArrayList<>();
        TheMovieDBService client = TheMovieDBServiceGenerator.createService(TheMovieDBService.class);

        for (Integer actorId : actorIds) {

            Call<Actor> call = client.getActorDetails(actorId, TheMovieDBService.API_KEY);
            call.enqueue(new Callback<Actor>() {

                @Override
                public void onResponse(Call<Actor> call, Response<Actor> response) {
                    if (response.isSuccess()) {
                        mFinalActorList.add(response.body());
                        Log.d("getActorBios(actorIds)", "Actor: " + response.body().getName() + " added to results.");

                        // If ActorList is now full i.e. this is the last call
                        if (mFinalActorList.getActors().size() == mActorIds.size()) {
                            setContentView(R.layout.activity_result_actor);

                            mViewPager = (ViewPager) findViewById(R.id.activity_result_actor_view_pager);

                            FragmentManager fm = getSupportFragmentManager();
                            mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
                                @Override
                                public Fragment getItem(int position) {
                                    Actor actor = mFinalActorList.getActors().get(position);
                                    return ActorResultFragment.newInstance(actor.getId());
                                }

                                @Override
                                public int getCount() {
                                    return mFinalActorList.getActors().size();
                                }
                            });
                        }

                    } else {
                        Log.e("Error", "Error loading actor bio");
                    }
                }

                @Override
                public void onFailure(Call<Actor> call, Throwable t) {
                    Log.e("getActorBios(actorIds)", t.getMessage());
                }

            });

        }

        Log.d("getActorDetails(actor)", "getActorDetails(actors) called");

        return mActorList;
    }



}
