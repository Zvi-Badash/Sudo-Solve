package com.zvibadash.sudosolve.networking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseSolved {
    @SerializedName("solved")
    @Expose
    private String solved;

    public String getSolved() {
        return solved;
    }

    public void setSolved(String solved) {
        this.solved = solved;
    }
}