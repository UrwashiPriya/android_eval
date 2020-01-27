package com.neuman.brutus.retrofit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Attributes {
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
