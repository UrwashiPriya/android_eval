package com.neuman.brutus.retrofit;

import com.google.gson.JsonObject;
import com.neuman.brutus.retrofit.models.RomaResponse;
import com.neuman.brutus.retrofit.models.SimpleResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Interface {

    @POST("people/login")
    Call<SimpleResponse> user_login(@Body JsonObject user_creds);

    @POST("roma/fetch")
    Call<RomaResponse> roma_fetch(@Body JsonObject roma_search);
}
