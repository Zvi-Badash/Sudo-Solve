package com.zvibadash.sudosolve.networking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestIdentify {
    @SerializedName("image")
    @Expose
    private String image;

    public RequestIdentify(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}