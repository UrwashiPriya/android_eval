package com.neuman.brutus.Retrofit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RomaResponse {
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
}


class Roma {
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
}

class Attributes {
    @Expose
    @SerializedName("id")
    Integer id;

    @Expose
    @SerializedName("name")
    String name;

    @Expose
    @SerializedName("placeholder")
    String placeholder;

    @Expose
    @SerializedName("var_type")
    String var_type;

    @Expose
    @SerializedName("attr_data_id")
    Integer attr_data_id;

    @Expose
    @SerializedName("attr_data_value")
    String attr_data_value;

    @Expose
    @SerializedName("raw_data")
    String raw_data;

    @Expose
    @SerializedName("enums")
    ArrayList<Enums> enums;
}

class Clusters {
    @Expose
    @SerializedName("id")
    Integer id;

    @Expose
    @SerializedName("cluster")
    String cluster;
}

class Enums {
    @Expose
    @SerializedName("id")
    Integer id;

    @Expose
    @SerializedName("value")
    String value;

    @Expose
    @SerializedName("format")
    String format;
}