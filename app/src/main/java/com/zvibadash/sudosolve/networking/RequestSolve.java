package com.zvibadash.sudosolve.networking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestSolve {
    @SerializedName("unsolved")
    @Expose
    private String unsolved;

    public RequestSolve(String board) {
        this.unsolved = board;
    }

    public String getUnsolved() {
        return unsolved;
    }

    public void setUnsolved(String unsolved) {
        this.unsolved = unsolved;
    }
}