package com.skylarkingstudios.whatshisface;

import android.content.Context;
import android.util.Log;

import com.skylarkingstudios.whatshisface.model.Actor;
import com.skylarkingstudios.whatshisface.model.ActorList;
import com.skylarkingstudios.whatshisface.model.Movie;
import com.skylarkingstudios.whatshisface.model.MovieList;
import com.skylarkingstudios.whatshisface.model.RetrofitEvent;
import com.skylarkingstudios.whatshisface.model.remote.TheMovieDBService;
import com.skylarkingstudios.whatshisface.model.remote.TheMovieDBServiceGenerator;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// DataManager holds the methods that fetch data and make changes accordingly to the model
public class DataManager {

    private MovieList mMovies;
    private List<Actor> actors;
    private int mId;
    private Actor actor;

    public DataManager(Context context) {
        updateRef(context);
    }

    public void updateRef(Context context) {
        mMovies = MovieList.get(context);
    }

    public void addCast(int id) {
        mId = id;
        TheMovieDBService client = TheMovieDBServiceGenerator.createService(TheMovieDBService.class);
        Call<Movie> call = client.getMovieCast(id, TheMovieDBService.API_KEY);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                // Maybe do a check to see if an empty set can be returned
                if (response.isSuccess()) {
                    mMovies.addMovieCast(mMovies.getMovie(mId), response.body().getCast());
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.e("Comm Error", t.getMessage());
            }
        });
    }

    // HashMap doesn't consider the actor's the same so it is counting them twice. Need to someone go to actorIds and then get the Actor back from that
//    public List<Actor> getCommonActors(Context context) {
//
//        updateRef(context);
//
//        List<Actor> actorPool = new ArrayList<>();
//        List<Actor> actorResults = new ArrayList<>();
//        Map<Actor, Integer> map = new HashMap<>();
//        int maxFrequency = 0;
//        int counter = 0;
//
//        if (mMovies.getMovies().size() > 1) {
//            for (Movie mov : mMovies.getMovies()) {
//                Log.d("getCommonActors()", "Adding Cast From Movie: " + mov.getTitle());
//                for (Actor actor : mov.getCast()) {
//                    actorPool.add(actor);
//                }
//            }
//            for (Actor actor : actorPool) {
//                Integer count = map.get(actor);
//                map.put(actor, (count == null) ? 1 : count + 1);
//                Log.d("getCommonActors()", "Actor: " + actor.getName() + " has been seen " + ((count == null) ? 1 : count + 1) + " time(s). MaxFreq: " + maxFrequency);
//                if (map.get(actor) > maxFrequency) {
//                    maxFrequency = map.get(actor);
//                }
//            }
//            for (Actor actor : map.keySet()) {
//                if (map.get(actor).equals(maxFrequency)) {
//                    counter+=1;
//                    if (counter <= 10) {
//                        actorResults.add(actor);
//                        Log.d("getCommonActors()", "Actor added to Results: " + actor.getName());
//                    } else {
//                        break;
//                    }
//                }
//            }
//        } else {
//            for (Actor actor : mMovies.getMovies().get(0).getCast()) {
//                counter+=1;
//                if (counter <= 4) {
//                    actorResults.add(actor);
//                } else {
//                    break;
//                }
//            }
//        }
//        return actorResults;
//    }

    /* getCommonActorIds takes the current MovieList's casts and adds each of their actor's IDs to a
        common List<Integer> pool. Each item is added to a HashMap to account for frequency and
        highest frequency. Finally all map entries whose value matches the highest frequency are
        added to the result list and the common actor Ids are returned. If only one Movie is in
        MovieList only the first 4 results are returned.

        TODO | add some more checks:
            If more than one movie is searched and none have any ids in common inform the user in
            the results that they may be one of these actors but the movies don't share actors.
    */
    public List<Integer> getCommonActorIds(Context context) {

        updateRef(context);

        List<Integer> actorIdPool = new ArrayList<>();
        List<Integer> actorIdResults = new ArrayList<>();
        Map<Integer, Integer> map = new HashMap<>();
        int maxFrequency = 0;
        int counter = 0;

        if (mMovies.getMovies().size() > 1) {

            for (Movie mov : mMovies.getMovies()) {
                Log.d("getCommonActors()", "Adding Cast From Movie: " + mov.getTitle());
                for (Actor actor : mov.getCast()) {
                    actorIdPool.add(actor.getId());
                }
            }

            for (Integer actorId : actorIdPool) {
                Integer count = map.get(actorId);
                map.put(actorId, (count == null) ? 1 : count + 1);
                if (map.get(actorId) > maxFrequency) {
                    maxFrequency = map.get(actorId);
                }
                Log.d("getCommonActors()", "ActorID: " + actorId + " has been seen " + ((count == null) ? 1 : count + 1) + " time(s). MaxFreq: " + maxFrequency);
            }

            for (Integer actorId : map.keySet()) {
                if (map.get(actorId).equals(maxFrequency)) {
                    counter+=1;
                    if (counter <= 10) {
                        actorIdResults.add(actorId);
                        Log.d("getCommonActors()", "Results have added ID: " + actorId);
                    } else {
                        break;
                    }
                }
            }

        // If one movie is present just show top 4 billed actors
        } else {
            for (Actor actor : mMovies.getMovies().get(0).getCast()) {
                counter+=1;
                if (counter <= 4) {
                    actorIdResults.add(actor.getId());
                } else {
                    break;
                }
            }
        }

        // If more than one movie is cross checked but no actor appears in more than one cast, return null, else return the list
        if (maxFrequency == 1 && mMovies.getMovies().size() > 1) {
            return null;
        } else {
            return actorIdResults;
        }
    }

    // TODO make API Call to fetch Actor details given an ActorId. Return completed Actor.

    // getActorBios(List<Integer>) returns a full list of bios (as a List<Actor>) given the set of actor ids
    public List<Actor> getActorBios(List<Integer> actorIds) {
        Log.e("getActorBios(actorIds)", "THIS IS GETTING CALLED FOR SOME REASON");
        actors = new ArrayList<>();
        TheMovieDBService client = TheMovieDBServiceGenerator.createService(TheMovieDBService.class);

        for (Integer actorId : actorIds) {

            Call<Actor> call = client.getActorDetails(actorId, TheMovieDBService.API_KEY);
            call.enqueue(new Callback<Actor>() {

                @Override
                public void onResponse(Call<Actor> call, Response<Actor> response) {
                    if (response.isSuccess()) {
                        actors.add(response.body());
                        Log.d("getActorBios(actorIds)", "Actor: " + response.body().getName() + " added to results.");
                        EventBus.getDefault().post(new RetrofitEvent(true));
                    } else {
                        EventBus.getDefault().post(new RetrofitEvent(false));
                    }
                }

                @Override
                public void onFailure(Call<Actor> call, Throwable t) {
                    Log.e("getActorBios(actorIds)", t.getMessage());
                }

            });

        }

        Log.d("getActorDetails(actor)", "getActorDetails(actors) called");

        return actors;
    }

    // Return singular actor given actorId
    public Actor getActorBio(Integer actorId) {
        TheMovieDBService client = TheMovieDBServiceGenerator.createService(TheMovieDBService.class);

        Call<Actor> call = client.getActorDetails(actorId, TheMovieDBService.API_KEY);
        call.enqueue(new Callback<Actor>() {

            @Override
            public void onResponse(Call<Actor> call, Response<Actor> response) {
                if (response.isSuccess()) {
                    actor = response.body();
                    Log.d("getActorBio(actorId)", "Actor: " + response.body().getName() + " added to results.");
                }
            }

            @Override
            public void onFailure(Call<Actor> call, Throwable t) {
                Log.e("getActorBio(actorId)", t.getMessage());
            }
        });

        return actor;
    }


}
