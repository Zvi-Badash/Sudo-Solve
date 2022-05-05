package com.zvibadash.sudosolve.networking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseIdentify {
    @SerializedName("board")
    @Expose
    private String board;

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }
}