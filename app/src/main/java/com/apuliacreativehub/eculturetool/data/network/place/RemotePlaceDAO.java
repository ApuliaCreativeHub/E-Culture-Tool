package com.apuliacreativehub.eculturetool.data.network.place;

import com.apuliacreativehub.eculturetool.data.entity.Place;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RemotePlaceDAO {
    @POST("user/add")
    Call<Void> AddPlace(@Body Place place);
}
