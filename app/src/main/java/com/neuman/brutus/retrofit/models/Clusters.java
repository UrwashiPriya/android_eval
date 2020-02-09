package com.neuman.brutus.retrofit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Clusters implements Serializable {
    @Expose
    @SerializedName("id")
    Integer id;

    @Expose
    @SerializedName("cluster")
    String cluster;

    @Expose
    @SerializedName("account_id")
    Integer account_id;

    public Integer getId() { return id; }

    public String getCluster() { return cluster; }
}
