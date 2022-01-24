package com.apuliacreativehub.eculturetool.data.network.place;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RemotePlaceDAO {
    @Multipart
    @POST("place/add")
    Call<Void> AddPlace(@Part("name") RequestBody name,
                        @Part("address") RequestBody address,
                        @Part("description") RequestBody description,
                        @Part() MultipartBody.Part file);
}
