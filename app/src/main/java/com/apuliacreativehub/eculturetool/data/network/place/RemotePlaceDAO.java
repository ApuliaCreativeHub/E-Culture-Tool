package com.apuliacreativehub.eculturetool.data.network.place;

import com.apuliacreativehub.eculturetool.data.entity.Place;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RemotePlaceDAO {
    @Multipart
    @POST("user/add")
    Call<Void> AddPlace( @Part("name")RequestBody name,
                        @Part("address")RequestBody address,
                        @Part("description")RequestBody description,
                         @Part("file") RequestBody file);
}
