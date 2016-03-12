package com.skylarkingstudios.whatshisface.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class MovieList {

    private static MovieList sMovieList;

    private List<Movie> mMovies;

    public static MovieList get(Context context) {
        if (sMovieList == null) {
            sMovieList = new MovieList(context);
        }
        return sMovieList;
    }

    private MovieList(Context context) {
        mMovies = new ArrayList<>();
    }

    public List<Movie> getMovies() {
        return mMovies;
    }

    public void addMovie(Movie movie) {
        mMovies.add(movie);
    }

    public void removeMovie(Movie movie) {
        mMovies.remove(movie);
    }

    public Movie getMovie(String title) {
        for (Movie movie : mMovies) {
            if (movie.getTitle().equals(title)) {
                return movie;
            }
        }
        return null;
    }

    public Movie getMovie(int id) {
        for (Movie movie : mMovies) {
            if (movie.getId() == id) {
                return movie;
            }
        }
        return null;
    }

    public void addMovieCast(Movie movie, List<Actor> cast) {
        mMovies.get(mMovies.indexOf(movie)).setCast(cast);
    }

    public void clearMovies() {
        mMovies.clear();
    }

}




