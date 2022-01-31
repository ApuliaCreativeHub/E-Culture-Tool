package com.apuliacreativehub.eculturetool.data.network.path;

import com.apuliacreativehub.eculturetool.data.entity.Path;
import com.apuliacreativehub.eculturetool.data.entity.PathWithObjects;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RemotePathDAO {
    @POST("path/add")
    Call<PathWithObjects> addPath(@Body PathWithObjects path);

    @GET("path/getYourPaths")
    Call<List<PathWithObjects>> getYourPaths(@Query("userId") int userId);

    @GET("path/getPlacePaths")
    Call<List<PathWithObjects>> getPlacePaths(@Query("placeId") int placeId);

    @POST("path/update")
    Call<PathWithObjects> editPath(@Body PathWithObjects path);

    @POST("path/delete")
    Call<Void> deletePath(@Body Path path);
}
