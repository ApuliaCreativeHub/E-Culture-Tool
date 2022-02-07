package com.apuliacreativehub.eculturetool.data.network.zone;

import com.apuliacreativehub.eculturetool.data.entity.Zone;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RemoteZoneDAO {
    @GET("zone/getPlaceZones")
    Call<ArrayList<Zone>> GetAllPlaceZones(@Query("placeId") int placeId);

    @POST("zone/add")
    Call<Zone> AddZone(@Body Zone zone);

    @POST("zone/update")
    Call<Zone> EditZone(@Body Zone zone);

    @POST("zone/delete")
    Call<Void> DeleteZone(@Body Zone zone);
}
