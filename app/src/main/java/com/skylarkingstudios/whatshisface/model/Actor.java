package com.skylarkingstudios.whatshisface.model;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Actor {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("birthday")
    private String dob;

    @SerializedName("biography")
    private String bio;

    @SerializedName("profile_path")
    private String imageUrl;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // Return "MMM d, yyyy" instead of "yyyy-mm-dd"
    public String getDob() throws ParseException {
        if (dob != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
            Date date = sdf.parse(dob);
            sdf.applyPattern("MMM d, yyyy");
            return sdf.format(date);
        } else {
            return "";
        }
    }

    public String getBio() {
        return bio;
    }

    public String getImageURL() {
        return imageUrl;
    }

}
