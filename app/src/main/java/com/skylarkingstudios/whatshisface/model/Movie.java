package com.skylarkingstudios.whatshisface.model;


import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Movie {

    // Using OMDB SerializedNames retrieved from their API's JSON output

    @SerializedName("title")
    private String title;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("overview")
    private String plot;

    @SerializedName("id")
    private int id;

    @SerializedName("cast")
    private List<Actor> cast = new ArrayList<>();

    @SerializedName("poster_path")
    private String posterURL;

    private String castArray[];


    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getFormattedReleaseDate() {
        releaseDate = releaseDate.substring(0,4);
        return ("("+ releaseDate +")");
    }

    public int getId() {
        return id;
    }

    public List<Actor> getCast() {
        return cast;
    }

    public void setCast(List<Actor> cast){
        this.cast = cast;
    }

    public String getPosterURL() {
        return posterURL;

    }
}
