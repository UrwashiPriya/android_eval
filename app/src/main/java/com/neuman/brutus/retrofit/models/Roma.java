package com.neuman.brutus.retrofit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Roma {
    @Expose
    @SerializedName("id")
    Integer id;

    @Expose
    @SerializedName("code")
    String code;

    @Expose
    @SerializedName("added_by")
    Integer added_by;

    @Expose
    @SerializedName("username")
    String username;

    @Expose
    @SerializedName("account")
    String account;

    @Expose
    @SerializedName("added_on")
    String added_on;

    @Expose
    @SerializedName("account_id")
    Integer account_id;

    @Expose
    @SerializedName("updated_on")
    String updated_on;

    @Expose
    @SerializedName("is_alive")
    Integer is_alive;

    @Expose
    @SerializedName("roma_module_id")
    Integer roma_module_id;

    @Expose
    @SerializedName("attributes")
    ArrayList<Attributes> attributes;

    @Expose
    @SerializedName("clusters")
    ArrayList<Clusters> clusters;

    public String getCode() { return code; }
}
