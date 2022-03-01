package com.zvibadash.sudosolve.networking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestGeneration {
    @SerializedName("diffLevel")
    @Expose
    private DifficultyLevel diffLevel;

    public RequestGeneration(DifficultyLevel level) {
        this.diffLevel = level;
    }

    public DifficultyLevel getDiffLevel() {
        return diffLevel;
    }

    public void setDiffLevel(DifficultyLevel diffLevel) {
        this.diffLevel = diffLevel;
    }
}