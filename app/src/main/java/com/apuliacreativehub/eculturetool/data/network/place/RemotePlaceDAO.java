package com.apuliacreativehub.eculturetool.data.network.place;

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
    Call<Void> addPlace(@Part("name") RequestBody name,
                        @Part("address") RequestBody address,
                        @Part("description") RequestBody description,
                        @Part() MultipartBody.Part file);

    @Headers("Content-Type: application/json")
    @GET("place/getYours")
    Call<ArrayList<Place>> getYourPlaces();

    @Headers("Content-Type: application/json")
    @GET("place/getAll")
    Call<ArrayList<Place>> getAllPlaces();

    @POST("place/delete")
    Call<Void> DeletePlace(@Body Place place);

    @Multipart
    @POST("place/update")
    Call<Void> EditPlace(@Part("id") RequestBody id,
                        @Part("name") RequestBody name,
                        @Part("address") RequestBody address,
                        @Part("description") RequestBody description,
                        @Part() MultipartBody.Part file);

    @Multipart
    @POST("place/update")
    Call<Void> EditPlaceNoImg(@Part("id") RequestBody id,
                              @Part("name") RequestBody name,
                              @Part("address") RequestBody address,
                              @Part("description") RequestBody description);
}
