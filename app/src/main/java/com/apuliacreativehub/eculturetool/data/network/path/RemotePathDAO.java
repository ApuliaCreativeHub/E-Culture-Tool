package com.apuliacreativehub.eculturetool.data.network.path;

import com.apuliacreativehub.eculturetool.data.entity.Path;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RemotePathDAO {
    @POST("path/add")
    Call<Path> addPath(@Body Path path);

    @GET("path/getYourPaths")
    Call<List<Path>> getYourPaths(@Query("userId") int userId);

    @GET("path/getPlacePaths")
    Call<List<Path>> getPlacePaths(@Query("placeId") int placeId);

    @GET("path/getCuratorPlacePaths")
    Call<List<Path>> getCuratorPlacePaths(@Query("placeId") int placeId);

    @POST("path/update")
    Call<Path> editPath(@Body Path path);

    @POST("path/delete")
    Call<Void> deletePath(@Body Path path);
}
