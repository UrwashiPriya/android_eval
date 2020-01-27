package com.neuman.brutus.retrofit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Enums {
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
