package com.skylarkingstudios.whatshisface.model;

import com.google.gson.annotations.SerializedName;

public class Actor {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }


}
