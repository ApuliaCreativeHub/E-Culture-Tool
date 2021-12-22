package com.apuliacreativehub.eculturetool.data.network;

import com.apuliacreativehub.eculturetool.data.entity.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RemoteUserDAO {
    @Headers("Content-Type: application/json")
    @POST("user/add")
    Call<Void> RegisterUser(@Body User user);
}
