package com.skylarkingstudios.whatshisface.model.remote;

import com.skylarkingstudios.whatshisface.model.Actor;
import com.skylarkingstudios.whatshisface.model.Movie;
import com.skylarkingstudios.whatshisface.model.MovieResults;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TheMovieDBService {

    String API_KEY = "57496200a8ef62021f33eafb841a2219";

    // Get movie credits by id (int)
    @GET("movie/{id}/credits")
    Call<Movie> getMovieCast(@Path("id") int movieId,
                             @Query("api_key") String apiKey);

    // Search for movie by title (String)
    @GET("search/movie")
    Call<MovieResults> getMovieSearchResults(@Query("query") String title,
                                             @Query("api_key") String apiKey);

    // Get actor details by id (int)
    @GET("person/{id}")
    Call<Actor> getActorDetails(@Path("id") int actorId,
                                @Query("api_key") String apiKey);

    // Get actor poster by id (int)
    @GET("person/{id}/images")
    Call<String> getActorPoster(@Path("id") int actorId,
                                @Query("api_key") String apiKey);

    // Get API Configuration values
    @GET("configuration")
    Call<String> getConfig(@Query("api_key") String apiKey);
}
