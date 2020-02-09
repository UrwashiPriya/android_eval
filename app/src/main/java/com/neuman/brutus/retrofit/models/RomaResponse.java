package com.neuman.brutus.retrofit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.neuman.brutus.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class RomaResponse implements Serializable {
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

    public ArrayList<Roma> getRoma() { return roma; }

    public ArrayList<String> getCodeList() {

        ArrayList<String> codes = new ArrayList<>(); Iterator<Roma> i = roma.iterator();

        while(i.hasNext()) { codes.add(i.next().getCode()); }

        return codes;
    }

    public ArrayList<String> getEqTypeList() {
        ArrayList<String> types = new ArrayList<>();
        Iterator<Roma> i = roma.iterator();

        while(i.hasNext()) {
            types.add(i.next().getAttribute("Equipment Type"));
        }

        return types;
    }

    public ArrayList<String> getImageList() {
        ArrayList<String> images = new ArrayList<>();
        Iterator<Roma> i = roma.iterator();

        while(i.hasNext()) {
            images.add(i.next().getAttribute("Equipment Image"));
        }

        return images;
    }
}