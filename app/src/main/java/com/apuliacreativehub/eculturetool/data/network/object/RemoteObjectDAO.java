package com.apuliacreativehub.eculturetool.data.network.object;

import com.apuliacreativehub.eculturetool.data.entity.Object;
import com.apuliacreativehub.eculturetool.data.entity.Place;
import com.apuliacreativehub.eculturetool.data.entity.Zone;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RemoteObjectDAO {
    @Multipart
    @POST("object/add")
    Call<Void> AddObject(@Part("name") @Body RequestBody name,
                        @Part("description") RequestBody description,
                        @Part("zoneId") RequestBody zoneId,
                        @Part() MultipartBody.Part file);

    @GET("object/getByZoneAndPlace")
    Call<ArrayList<Object>> GetObjectByZoneAndPlace(@Body Zone zone, @Body Place place);
}
