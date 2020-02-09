package com.neuman.brutus.retrofit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ClusterResponse {
    @Expose
    @SerializedName("success")
    private String success;

    @Expose
    @SerializedName("status")
    private Integer status;

    @Expose
    @SerializedName("cluster")
    private ArrayList<Clusters> clusters;

    public ArrayList<Clusters> getClusters() { return clusters; }

    public String getSuccess() { return success; }
}
