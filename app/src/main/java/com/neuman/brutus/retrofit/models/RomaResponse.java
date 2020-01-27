package com.neuman.brutus.retrofit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class RomaResponse implements Serializable {
    @Expose
    @SerializedName("success")
    private String success;

    @Expose
    @SerializedName("status")
    private Integer status;

    @Expose
    @SerializedName("roma")
    private ArrayList<Roma> roma;

    public String getSuccess() {
        return success;
    }

    public ArrayList<Roma> getRoma() { return roma; }
}