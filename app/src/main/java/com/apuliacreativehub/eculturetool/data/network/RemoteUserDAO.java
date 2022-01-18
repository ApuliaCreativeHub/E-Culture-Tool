package com.apuliacreativehub.eculturetool.data.network;

import com.apuliacreativehub.eculturetool.data.entity.User;
import com.apuliacreativehub.eculturetool.data.entity.UserWithToken;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RemoteUserDAO {
    @Headers("Content-Type: application/json")
    @POST("user/add")
    Call<Void> RegisterUser(@Body User user);

    @Headers("Content-Type: application/json")
    @POST("user/update")
    Call<User> UpdateUser(@Body User user);

    @Headers("Content-Type: application/json")
    @POST("user/auth")
    Call<UserWithToken> LoginUser(@Body UserWithToken user);

    @Headers("Content-Type: application/json")
    @GET("user/logout")
    Call<Void> LogoutUser();

    @Headers("Content-Type: application/json")
    @POST("user/changepassword")
    Call<Void> ChangePassword(@Body User user);

    @Headers("Content-Type: application/json")
    @POST("user/delete")
    Call<Void> DeleteUser(@Body User user);
}