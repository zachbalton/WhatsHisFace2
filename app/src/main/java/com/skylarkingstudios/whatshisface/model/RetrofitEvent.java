package com.skylarkingstudios.whatshisface.model;

public class RetrofitEvent {

    private boolean isRetrofitCompleted;

    public RetrofitEvent(boolean isRetrofitCompleted) {
        this.isRetrofitCompleted = isRetrofitCompleted;
    }

    public boolean isRetrofitCompleted() {
        return isRetrofitCompleted;
    }

    public void setRetrofitCompleted(boolean isRetrofitCompleted) {
        this.isRetrofitCompleted = isRetrofitCompleted;
    }

}
