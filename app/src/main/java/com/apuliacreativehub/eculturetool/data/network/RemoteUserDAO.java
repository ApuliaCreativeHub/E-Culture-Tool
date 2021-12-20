package com.apuliacreativehub.eculturetool.data.network;

import com.apuliacreativehub.eculturetool.data.entity.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RemoteUserDAO {
    @POST("user/add")
    Call<User> RegisterUser(@Body User user);
}
