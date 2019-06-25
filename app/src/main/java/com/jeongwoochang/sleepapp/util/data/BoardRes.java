package com.jeongwoochang.sleepapp.util.data;

import com.google.gson.annotations.SerializedName;
import com.jeongwoochang.sleepapp.util.data.Board;

import java.util.ArrayList;

public class BoardRes {
    @SerializedName("status")
    private boolean status;
    @SerializedName("data")
    private ArrayList<Board> data;

    public BoardRes(boolean status, ArrayList<Board> data) {
        this.status = status;
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ArrayList<Board> getData() {
        return data;
    }

    public void setData(ArrayList<Board> data) {
        this.data = data;
    }
}
