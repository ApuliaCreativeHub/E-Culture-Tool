package com.apuliacreativehub.eculturetool.data.network.zone;

import com.apuliacreativehub.eculturetool.data.entity.Zone;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RemoteZoneDAO {
    @GET("zone/getPlaceZones")
    Call<ArrayList<Zone>> getAllPlaceZones();
}
