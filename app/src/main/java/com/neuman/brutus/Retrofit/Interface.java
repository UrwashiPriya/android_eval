package com.neuman.brutus.Retrofit;

import com.google.gson.JsonObject;
import com.neuman.brutus.Retrofit.models.SimpleResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Interface {

    @POST("people/login")
    Call<SimpleResponse> user_login(@Body JsonObject user_creds);
}
