package com.skylarkingstudios.whatshisface.model;

import com.google.gson.annotations.SerializedName;
import com.skylarkingstudios.whatshisface.model.remote.TheMovieDBService;
import com.skylarkingstudios.whatshisface.model.remote.TheMovieDBServiceGenerator;

public class APIConfiguration {

    @SerializedName("base_url")
    private static String baseURL;

    public String getBaseURL() {
        return baseURL;
    }


}
