package com.neuman.brutus.retrofit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Attributes implements Serializable {
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

    public Integer getId() { return id; }

    public String getName() { return name; }

    public Integer getAttr_data_id() { return attr_data_id; }

    public String getVar_type() { return var_type; }

    public String getPlaceholder() { return placeholder; }

    public ArrayList<Enums> getEnums() { return enums; }

    public ArrayList<String> getEnumsValues() {
        ArrayList<String> enumarr = new ArrayList<>();
        for (Enums enms:enums) { enumarr.add(enms.getValue()); }
        return enumarr;
    }

    public int valToId(String value) {
        for (Enums enm: enums) {
            if(enm.getValue().equals(value)) {
                return enm.getId();
            }
        }
        return -1;
    }

    public String getAttr_data_value() { return attr_data_value; }

    public String getRaw_data() { return raw_data; }
}
