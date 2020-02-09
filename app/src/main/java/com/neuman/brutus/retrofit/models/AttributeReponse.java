package com.neuman.brutus.retrofit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AttributeReponse {
    @Expose
    @SerializedName("success")
    private String success;

    @Expose
    @SerializedName("status")
    private Integer status;

    @Expose
    @SerializedName("attribute")
    ArrayList<Attributes> attributes = new ArrayList<>();

    public String getSuccess() { return success; }

    public ArrayList<Attributes> getAttributes() {
        return attributes;
    }
}
