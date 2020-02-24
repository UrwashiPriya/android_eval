package com.neuman.brutus.retrofit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.w3c.dom.Attr;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Roma implements Serializable {
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

    public Integer getAdded_by() { return added_by; }

    public String getAttribute(String attribute) {

        for (Attributes nxt : attributes) {
            if (nxt.getName().equals(attribute)) {
                return nxt.getAttr_data_value() == null ? nxt.getRaw_data() : nxt.getAttr_data_value();
            }
        }
        return "";
    }

    public List<String> fetchClustersAsList() {
        List<String> cluster_val = new ArrayList<>();

        for (Clusters clu : clusters) {
            cluster_val.add(clu.getCluster());
        }
        return cluster_val;
    }

    public ArrayList<Attributes> getAttributes() {
        return attributes;
    }
}
