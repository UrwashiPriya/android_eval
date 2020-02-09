package com.neuman.brutus.retrofit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Enums implements Serializable {
    @Expose
    @SerializedName("id")
    Integer id;

    @Expose
    @SerializedName("value")
    String value;

    @Expose
    @SerializedName("format")
    String format;

    public Integer getId() { return id; }

    public String getValue() { return value; }
}
