package com.neuman.brutus.retrofit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SimpleResponse {

    @Expose
    @SerializedName("success")
    private String success;

    @Expose
    @SerializedName("status")
    private String status;

    public String getSuccess() {
        return success;
    }
}
