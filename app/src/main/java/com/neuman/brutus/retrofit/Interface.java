package com.neuman.brutus.retrofit;

import com.google.gson.JsonObject;
import com.neuman.brutus.retrofit.models.AttributeReponse;
import com.neuman.brutus.retrofit.models.ClusterResponse;
import com.neuman.brutus.retrofit.models.Clusters;
import com.neuman.brutus.retrofit.models.RomaResponse;
import com.neuman.brutus.retrofit.models.SimpleResponse;
import com.neuman.brutus.retrofit.models.UploadResponse;

import org.w3c.dom.Attr;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Interface {

    // Done
    @POST("people/login")
    Call<SimpleResponse> user_login(@Body JsonObject user_creds);

    // Done
    @POST("roma/fetch")
    Call<RomaResponse> roma_fetch(@Body JsonObject roma_search);

    // Done
    @POST("roma/attribute/fetch")
    Call<AttributeReponse> fetch_roma_mod_attrs(@Body JsonObject response);

    // Done
    @POST("account/cluster/create")
    Call<ClusterResponse> account_cluster_create(@Body JsonObject response);

    // Done
    @POST("account/cluster/fetch")
    Call<ClusterResponse> account_cluster_fetch(@Body JsonObject response);

    @POST("roma/create")
    Call<SimpleResponse> create_roma(@Body JsonObject response);

    // Done
    @POST("roma/update")
    Call<SimpleResponse> update_roma(@Body JsonObject response);

//    @Multipart
//    @POST("upload")
//    Call<UploadResponse> upload_file(@Part MultipartBody.Part media, @Header("account") String account);

    @Multipart
    @POST("upload/roma")
    Call<UploadResponse> upload_file_as_attribute(@Part MultipartBody.Part media, @Header("account") String account, @Header("code") String roma_code, @Header("attr_id") String attr_id);
}
