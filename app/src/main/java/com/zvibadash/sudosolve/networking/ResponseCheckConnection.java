package com.zvibadash.sudosolve.networking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseCheckConnection {
    @SerializedName("connected")
    @Expose
    private Boolean connected;

    public Boolean getConnected() {
        return connected;
    }

    public void setConnected(Boolean connected) {
        this.connected = connected;
    }
}