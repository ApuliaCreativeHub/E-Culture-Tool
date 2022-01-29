package com.apuliacreativehub.eculturetool.data.network.object;

import com.apuliacreativehub.eculturetool.data.entity.Object;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface RemoteObjectDAO {
    @Multipart
    @POST("object/add")
    Call<Object> AddObject(@Part("name") RequestBody name,
                         @Part("description") RequestBody description,
                         @Part("zoneId") RequestBody zoneId,
                         @Part() MultipartBody.Part file);

    @GET("object/getZoneObjects")
    Call<ArrayList<Object>> GetObjectByZoneAndPlace(@Query("zoneId") int zoneId);

    @Multipart
    @POST("object/update")
    Call<Object> EditObject(@Part("id") RequestBody id,
                          @Part("name") RequestBody name,
                          @Part("description") RequestBody description,
                          @Part("zoneId") RequestBody zoneId,
                          @Part() MultipartBody.Part file);

    @Multipart
    @POST("object/update")
    Call<Object> EditObjectNoImg(@Part("id") RequestBody id,
                               @Part("name") RequestBody name,
                               @Part("description") RequestBody description,
                               @Part("zoneId") RequestBody zoneId);
}
