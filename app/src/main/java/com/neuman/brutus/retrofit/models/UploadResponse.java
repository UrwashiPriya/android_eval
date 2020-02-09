package com.neuman.brutus.retrofit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadResponse {
    @Expose
    @SerializedName("success")
    private String success;

    @Expose
    @SerializedName("status")
    private Integer status;

    @Expose
    @SerializedName("file")
    private String file;

    public String getSuccess() { return success; }

    public Integer getStatus() { return status; }

    public String getFile() { return file; }
}
