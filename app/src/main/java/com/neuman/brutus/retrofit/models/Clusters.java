package com.neuman.brutus.retrofit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Clusters {
    @Expose
    @SerializedName("id")
    Integer id;

    @Expose
    @SerializedName("cluster")
    String cluster;
}
