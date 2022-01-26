package com.apuliacreativehub.eculturetool.data.network.zone;

import com.apuliacreativehub.eculturetool.data.entity.Zone;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RemoteZoneDAO {
    @GET("zone/getPlaceZones")
    Call<ArrayList<Zone>> getAllPlaceZones(@Query("placeId") int placeId);
}
