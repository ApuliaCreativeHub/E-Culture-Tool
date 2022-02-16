package com.apuliacreativehub.eculturetool.data.network;

import com.apuliacreativehub.eculturetool.data.entity.user.User;
import com.apuliacreativehub.eculturetool.data.entity.user.UserWithToken;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RemoteUserDAO {
    @Headers("Content-Type: application/json")
    @POST("user/add")
    Call<Void> registerUser(@Body User user);

    @Headers("Content-Type: application/json")
    @POST("user/update")
    Call<User> updateUser(@Body User user);

    @Headers("Content-Type: application/json")
    @POST("user/auth")
    Call<UserWithToken> loginUser(@Body UserWithToken user);

    @Headers("Content-Type: application/json")
    @GET("user/logout")
    Call<Void> logoutUser();

    @Headers("Content-Type: application/json")
    @POST("user/changepassword")
    Call<Void> changePassword(@Body User user);

    @Headers("Content-Type: application/json")
    @POST("user/delete")
    Call<Void> deleteUser(@Body User user);
}