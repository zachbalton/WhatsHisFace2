package com.skylarkingstudios.whatshisface.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MovieResults {

    @SerializedName("results")
    private List<Movie> results = new ArrayList<>();

    @SerializedName("total_results")
    private int totalResults;

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }

    public int getTotalResults() {
        return totalResults;
    }
}
