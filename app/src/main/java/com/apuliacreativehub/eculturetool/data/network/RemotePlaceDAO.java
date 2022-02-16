package com.apuliacreativehub.eculturetool.data.network;

import com.apuliacreativehub.eculturetool.data.entity.Place;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RemotePlaceDAO {
    @Multipart
    @POST("place/add")
    Call<Place> AddPlace(@Part("name") RequestBody name,
                         @Part("address") RequestBody address,
                         @Part("description") RequestBody description,
                         @Part() MultipartBody.Part file);

    @Headers("Content-Type: application/json")
    @GET("place/getYours")
    Call<ArrayList<Place>> GetYourPlaces();

    @Headers("Content-Type: application/json")
    @GET("place/getAll")
    Call<ArrayList<Place>> GetAllPlaces();

    @POST("place/delete")
    Call<Void> DeletePlace(@Body Place place);

    @Multipart
    @POST("place/update")
    Call<Place> EditPlace(@Part("id") RequestBody id,
                          @Part("name") RequestBody name,
                          @Part("address") RequestBody address,
                          @Part("description") RequestBody description,
                          @Part() MultipartBody.Part file);

    @Multipart
    @POST("place/update")
    Call<Place> EditPlaceNoImg(@Part("id") RequestBody id,
                               @Part("name") RequestBody name,
                               @Part("address") RequestBody address,
                               @Part("description") RequestBody description);
}
