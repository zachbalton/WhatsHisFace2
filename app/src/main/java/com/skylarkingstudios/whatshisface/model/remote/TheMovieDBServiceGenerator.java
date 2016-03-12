package com.skylarkingstudios.whatshisface.model.remote;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Retrofit;

public class TheMovieDBServiceGenerator {

    public static final String API_BASE_URL = "https://api.themoviedb.org/3/";

    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder().addInterceptor(logging);
    private static Retrofit builder = new Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build();

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder;
        return retrofit.create(serviceClass);
    }

}
